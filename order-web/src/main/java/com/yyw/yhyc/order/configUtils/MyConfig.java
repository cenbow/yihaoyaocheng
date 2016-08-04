package com.yyw.yhyc.order.configUtils;

import java.io.IOException;
import java.util.Properties;

/**
 * Created by zhangqiang on 2016/8/4.
 */
public class MyConfig {
    public static String STATIC_URL;
    static{
        Properties props = new Properties();
        try {
            props.load(MyConfig.class.getClassLoader().getResourceAsStream("myconfig.properties"));
            STATIC_URL = (String)props.get("static_url");
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
