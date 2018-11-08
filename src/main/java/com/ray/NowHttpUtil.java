package com.ray;

import com.alibaba.fastjson.JSONObject;
import com.ray.entity.PhoneSms;

/**
 * @author gyy
 * @date 2018/11/6 15:19
 */
public class NowHttpUtil {
    public static boolean sendMsg(PhoneSms phoneSms) {
        String url = "http://localhost:8112/api/phoneSms/create";
        String postData = JSONObject.toJSONString(phoneSms);
        String html = HttpClientUtil.httpPost(url, postData, null);
        if(html.contains("操作成功")){
            return true;
        }else {
            return false;
        }
    }
}
