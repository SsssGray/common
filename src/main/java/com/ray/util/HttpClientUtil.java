package com.ray.util;

import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.SocketConfig;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HttpClientUtil {

    public static String requestRead(HttpServletRequest request){
        BufferedReader br = null;
        StringBuilder sb = new StringBuilder("");
        try
        {
            br = request.getReader();
            String str;
            while ((str = br.readLine()) != null)
            {
                sb.append(str);
            }
            br.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            if (null != br)
            {
                try
                {
                    br.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }
        return sb.toString();
    }

    public static List<String> requestReadLine(HttpServletRequest request) {
        List<String> list = new ArrayList<String>();
        BufferedReader br = null;
        try
        {
            br = request.getReader();
            String str;
            while ((str = br.readLine()) != null)
            {
                list.add(str);
            }
            br.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            if (null != br)
            {
                try
                {
                    br.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }
        return list;
    }

    public static String httpGet(String url) {
        try {
            String html = null;//网页内容
            int socketTimeout = 15000;//读取数据超时
            int connectTimeout = 15000;//链接超时
            SocketConfig socketConfig = SocketConfig.custom()
                    .setSoKeepAlive(false)
                    .setSoLinger(1)
                    .setSoReuseAddress(true)
                    .setSoTimeout(10000)
                    .setTcpNoDelay(true).build();
            RequestConfig config = RequestConfig.custom()
                    .setConnectTimeout(connectTimeout)
                    .setConnectionRequestTimeout(connectTimeout)
                    .setSocketTimeout(socketTimeout).build();
            CloseableHttpClient httpClient = HttpClientBuilder.create()
                    .setDefaultSocketConfig(socketConfig)
                    .setDefaultRequestConfig(config).build();
            HttpGet httpGet = new HttpGet(url);
            httpGet.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:50.0) Gecko/20100101 Firefox/50.0");

            HttpResponse response = httpClient.execute(httpGet);

            HttpEntity entity = response.getEntity();
            if (entity != null) {
                html = EntityUtils.toString(entity, "UTF-8");
                EntityUtils.consume(entity);//关闭内容流
            }
            //释放链接
            httpClient.close();
            return html;
        } catch (Exception ex) {
            ex.printStackTrace();
            return ex.getMessage();
        }
    }

    public static String httpGet(String url,Map<String,String> headerMap) {
        try {
            String html = null;//网页内容
            int socketTimeout = 15000;//读取数据超时
            int connectTimeout = 15000;//链接超时
            SocketConfig socketConfig = SocketConfig.custom()
                    .setSoKeepAlive(false)
                    .setSoLinger(1)
                    .setSoReuseAddress(true)
                    .setSoTimeout(10000)
                    .setTcpNoDelay(true).build();
            RequestConfig config = RequestConfig.custom()
                    .setConnectTimeout(connectTimeout)
                    .setConnectionRequestTimeout(connectTimeout)
                    .setSocketTimeout(socketTimeout).build();
            CloseableHttpClient httpClient = HttpClientBuilder.create()
                    .setDefaultSocketConfig(socketConfig)
                    .setDefaultRequestConfig(config).build();

            HttpGet httpGet = new HttpGet(url);
            httpGet.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:50.0) Gecko/20100101 Firefox/50.0");
            if(null!=headerMap){
                for(Map.Entry<String,String> x:headerMap.entrySet()){
                    httpGet.setHeader(x.getKey(),x.getValue());
                }
            }

            HttpResponse response = httpClient.execute(httpGet);

            HttpEntity entity = response.getEntity();
            if (entity != null) {
                html = EntityUtils.toString(entity, "UTF-8");
                EntityUtils.consume(entity);//关闭内容流
            }
            //释放链接
            httpClient.close();
            return html;
        } catch (Exception ex) {
            ex.printStackTrace();
            return ex.getMessage();
        }
    }

    public static String httpGet(String url,Map<String,String> headerMap,String ip) {
        try {
            String html = null;//网页内容
            int socketTimeout = 15000;//读取数据超时
            int connectTimeout = 15000;//链接超时
            String[] ips = ip.split(":");
            HttpHost httpHost = new HttpHost(ips[0],Integer.parseInt(ips[1]));
            SocketConfig socketConfig = SocketConfig.custom()
                    .setSoKeepAlive(false)
                    .setSoLinger(1)
                    .setSoReuseAddress(true)
                    .setSoTimeout(10000)
                    .setTcpNoDelay(true).build();
            RequestConfig config = RequestConfig.custom()
                    .setConnectTimeout(connectTimeout)
                    .setConnectionRequestTimeout(connectTimeout)
                    .setProxy(httpHost)
                    .setSocketTimeout(socketTimeout).build();
            CloseableHttpClient httpClient = HttpClientBuilder.create()
                    .setDefaultSocketConfig(socketConfig)
                    .setDefaultRequestConfig(config).build();

            HttpGet httpGet = new HttpGet(url);
            //httpGet.setHeader(HttpHeaders.CONNECTION, "close");
            httpGet.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:50.0) Gecko/20100101 Firefox/50.0");
            if(null!=headerMap){
                for(Map.Entry<String,String> x:headerMap.entrySet()){
                    httpGet.setHeader(x.getKey(),x.getValue());
                }
            }
            CloseableHttpResponse response = httpClient.execute(httpGet);

            HttpEntity entity = response.getEntity();
            if (entity != null) {
                html = EntityUtils.toString(entity, "UTF-8");
                EntityUtils.consume(entity);//关闭内容流
            }
            //释放链接
            httpClient.close();
            return html;
        } catch (Exception ex) {
            return "";
        }
    }


    public static String httpGetNoClose(String url,Map<String,String> headerMap,String ip) {
        try {
            String html = null;//网页内容
            int socketTimeout = 15000;//读取数据超时
            int connectTimeout = 15000;//链接超时
            String[] ips = ip.split(":");
            HttpHost httpHost = new HttpHost(ips[0],Integer.parseInt(ips[1]));
            RequestConfig config = RequestConfig.custom()
                    .setConnectTimeout(connectTimeout)
                    .setConnectionRequestTimeout(connectTimeout)
                    .setProxy(httpHost)
                    .setSocketTimeout(socketTimeout).build();

            SocketConfig socketConfig = SocketConfig.custom()
                    .setSoKeepAlive(false)
                    .setSoLinger(1)
                    .setSoReuseAddress(true)
                    .setSoTimeout(10000)
                    .setTcpNoDelay(true).build();
            CloseableHttpClient httpClient = HttpClientBuilder.create()
//                    .setRetryHandler(new DefaultHttpRequestRetryHandler(1, true))
                    .setDefaultSocketConfig(socketConfig)
                    .setDefaultRequestConfig(config).build();

            HttpGet httpGet = new HttpGet(url);
            //httpGet.setHeader(HttpHeaders.CONNECTION, "close");
            httpGet.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:50.0) Gecko/20100101 Firefox/50.0");
            if(null!=headerMap){
                for(Map.Entry<String,String> x:headerMap.entrySet()){
                    httpGet.setHeader(x.getKey(),x.getValue());
                }
            }
            CloseableHttpResponse response = httpClient.execute(httpGet);

            HttpEntity entity = response.getEntity();
            if (entity != null) {
                html = EntityUtils.toString(entity, "UTF-8");
                EntityUtils.consume(entity);//关闭内容流
            }
            //释放链接
            httpClient.close();
            return html;
        } catch (Exception ex) {
            return "";
        }
    }

    public static String httpPost(String url,String postData,Map<String,String> headerMap){
        try {
            int socketTimeout = 15000;//读取数据超时
            int connectTimeout = 15000;//链接超时

            SocketConfig socketConfig = SocketConfig.custom()
                    .setSoKeepAlive(false)
                    .setSoLinger(1)
                    .setSoReuseAddress(true)
                    .setSoTimeout(10000)
                    .setTcpNoDelay(true).build();
            RequestConfig config = RequestConfig.custom()
                    .setConnectTimeout(connectTimeout)
                    .setConnectionRequestTimeout(connectTimeout)
                    .setSocketTimeout(socketTimeout).build();
            CloseableHttpClient httpClient = HttpClientBuilder.create()
                    .setDefaultSocketConfig(socketConfig)
                    .setDefaultRequestConfig(config).build();
            String html = null;
            HttpPost httpost = new HttpPost(url);
            httpost.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:50.0) Gecko/20100101 Firefox/50.0");
            //设置参数
            if(null!=headerMap){
                for(Map.Entry<String,String> x:headerMap.entrySet()){
                    httpost.setHeader(x.getKey(),x.getValue());
                }
            }

            // 构建消息实体
            StringEntity postEntity = new StringEntity(postData, Charset.forName("UTF-8"));
            postEntity.setContentEncoding("UTF-8");
            try{
                JSONObject.parseObject(postData);
                postEntity.setContentType("application/json; charset=UTF-8");
            }
            catch (Exception e){
                postEntity.setContentType("application/x-www-form-urlencoded; charset=UTF-8");
            }
            // 发送Json格式的数据请求
            httpost.setEntity(postEntity);

            //执行请求
            HttpResponse response = httpClient.execute(httpost);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                html = EntityUtils.toString(entity, "UTF-8");
                EntityUtils.consume(entity);//关闭内容流
            }

            //释放链接
            httpClient.close();
            return html;
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }

    public static String httpPost(String url,String postData,Map<String,String> headerMap,int count,String str) throws Exception{
        String html;
        int errorCount = -1;
        do{
            html = httpPost(url,postData,headerMap);
            errorCount++;
        }while (!html.contains(str)&&errorCount<count);
        if(errorCount==count){
            throw new Exception("尝试失败");
        }
        return html;
    }

    public static String httpPost(String url,String postData,Map<String,String> headerMap,String ip){
        try {
            int socketTimeout = 15000;//读取数据超时
            int connectTimeout = 15000;//链接超时
            String html = null;
            String[] ips = ip.split(":");
            HttpHost httpHost = new HttpHost(ips[0],Integer.parseInt(ips[1]));
            SocketConfig socketConfig = SocketConfig.custom()
                    .setSoKeepAlive(false)
                    .setSoLinger(1)
                    .setSoReuseAddress(true)
                    .setSoTimeout(10000)
                    .setTcpNoDelay(true).build();
            RequestConfig config = RequestConfig.custom().setProxy(httpHost)
                    .setConnectTimeout(connectTimeout).setSocketTimeout(socketTimeout)
                    .setConnectionRequestTimeout(connectTimeout).build();
            CloseableHttpClient httpClient = HttpClientBuilder.create()
                    .setDefaultSocketConfig(socketConfig)
                    .setDefaultRequestConfig(config).build();

            HttpPost httpost = new HttpPost(url);
            httpost.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/68.0.3440.75 Safari/537.36");
            //设置参数
            if(null!=headerMap){
                for(Map.Entry<String,String> x:headerMap.entrySet()){
                    httpost.setHeader(x.getKey(),x.getValue());
                }
            }

            // 构建消息实体
            StringEntity postEntity = new StringEntity(postData, Charset.forName("UTF-8"));
            postEntity.setContentEncoding("UTF-8");
            try{
                JSONObject jsonObject = JSONObject.parseObject(postData);
                postEntity.setContentType("application/json; charset=UTF-8");
            }
            catch (Exception e){
                postEntity.setContentType("application/x-www-form-urlencoded; charset=UTF-8");
            }
            // 发送Json格式的数据请求
            httpost.setEntity(postEntity);

            //执行请求
            HttpResponse response = httpClient.execute(httpost);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                html = EntityUtils.toString(entity, "UTF-8");
                EntityUtils.consume(entity);//关闭内容流
            }

            //释放链接
            httpClient.close();
            return html;
        } catch (Exception e) {
            return "";
        }
    }

    //获得代理IP
    public static List<String> getIp(String url){
        String html = httpGet(url);
        List<String> list= RegexUtil.stringRegexs(html,"(\\d+.\\d+.\\d+.\\d+:\\d+)");
        return list;
    }

}
