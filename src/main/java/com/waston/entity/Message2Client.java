package com.waston.entity;

import java.util.Map;

/**
 * @Description: 后端向前端发送的信息实体类
 * @Author: Waston
 * @Date: 2019/8/10 17:22
 */
public class Message2Client {
    //信息内容
    private String content;
    //服务端登陆的用户列表
    private Map<String,String> names;


    public void setContent(String msg){
        this.content = msg;
    }
    //构造方法需加入调用client的客户端的用户名
    public void setContent(String userName,String msg){
        this.content = userName + "说：" + msg;
    }

    public String getContent() {
        return content;
    }

    public void setNames(Map<String, String> names) {
        this.names = names;
    }
}
