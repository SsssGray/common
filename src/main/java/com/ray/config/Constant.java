package com.ray.config;

import org.smslib.Service;

import java.util.HashMap;
import java.util.Map;

public class Constant {
    public static Service SRV = Service.getInstance();

    public static Map<String,String> COM_PHONE_MAP = new HashMap<String,String>();

    public static Map<String,String> PHONE_COM_MAP = new HashMap<String,String>();
}
