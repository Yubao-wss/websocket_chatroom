package com.waston.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @Description: 封装基础的工具方法，如加载配置文件、json序列化等
 * @Author: Waston
 * @Date: 2019/7/30 19:04
 */
public class CommUtils {
    private static final Gson gson = new GsonBuilder().create();
    private CommUtils(){}

    /**
     * 根据指定的文件名加载配置文件
     * @param fileName
     * @return
     */
    public static Properties loadProperties(String fileName){
        Properties properties = new Properties();
        try {

            //获取当前配置文件夹下的输入流
            InputStream in = CommUtils.class.getClassLoader().getResourceAsStream(fileName);
            //加载配置文件里的所有内容
            properties.load(in);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return properties;
    }
    public static String object2Json(Object obj){
        return gson.toJson(obj);
    }
    public static Object json2Object(String jsonStr,Class objClass){
        return gson.fromJson(jsonStr,objClass);
    }
    public static boolean strIsNull(String str){
        return str == null || str.equals("");
    }

}
