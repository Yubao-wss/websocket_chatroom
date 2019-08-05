package com.waston.service;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @Description: WebSocket的实现
 * @Author: Waston
 * @Date: 2019/8/5 22:21
 */
@ServerEndpoint("/websocket")
public class WebSocket {
    //储存所有连接到后端的WebSocket
    //使用CopyOnWriteArrayList保证线程安全
    private static CopyOnWriteArrayList<WebSocket> clients = new CopyOnWriteArrayList<>();

    //绑定当前WebSocket会话
    private Session session;

    @OnOpen
    public void onOpen(Session session){
        this.session = session;
        clients.add(this);
        System.out.println("有新的连接，当前Session的ID为:"+session.getId() + ",当前聊天室共有" + clients.size() + "人");
    }

    @OnError
    public void onError(Throwable e){
        System.err.println("WebSocket连接失败！");
        e.printStackTrace();
    }

    @OnMessage
    public void onMessage(String msg){
        System.out.println("浏览器发来的信息为:"+msg);

        //群聊
        for(WebSocket webSocket : clients){
            webSocket.sendMsg(msg);
        }
    }

    @OnClose
    public void onClose(){
        System.out.println("有用户退出聊天室");
        clients.remove(this);
        System.out.println("当前聊天室还剩下:"+clients.size()+"人");
    }

    public void sendMsg(String msg){
        try{
            this.session.getBasicRemote().sendText(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
