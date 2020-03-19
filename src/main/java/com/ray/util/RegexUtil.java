package com.ray.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexUtil {

    /**
     * 正则获取组1的内容
     * @param str
     * @param regex
     * @return
     */
    public static List<String> stringRegexs(String str,String regex){
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(str);
        List<String> list = new ArrayList<String>();
        while (m.find()) {
            list.add(m.group(1));
        }
        return list;
    }

    /**
     * 正则获取组1的内容
     * @param str
     * @param regex
     * @return
     */
    public static String stringRegex(String str,String regex){
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(str);
        if(m.find()){
            return m.group(1);
        }
        return "";
    }

    public static void main(String[] args) {
        System.out.println("hello world");
    }
}
