package com.waston.service;

import com.waston.entity.Message2Client;
import com.waston.entity.MessageFromClient;
import com.waston.utils.CommUtils;

import javax.servlet.annotation.WebServlet;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
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

    //储存所有的用户列表
    private static Map<String,String> names = new ConcurrentHashMap<>();
    //储存当前客户端的名称
    private String userName;

    @OnOpen
    public void onOpen(Session session){
        this.session = session;
        String userName = session.getQueryString().split("=")[1];
        this.userName = userName;
        //将客户端实体保存到clients
        clients.add(this);
        //将当前用户以及ID保存到用户列表
        names.put(session.getId(),userName);
        System.out.println("有新的连接，当前Session的ID为:"+session.getId() + ",当前聊天室共有" + clients.size() + "人");
        //发送给所有用户一个上线通知
        Message2Client message2Client = new Message2Client();
        message2Client.setContent(userName+"上线了！");
        message2Client.setNames(names);
        //发送消息
        String jsonStr = CommUtils.object2Json(message2Client);
        for(WebSocket webSocket : clients){
            webSocket.sendMsg(jsonStr);
        }
    }

    @OnError
    public void onError(Throwable e){
        System.err.println("WebSocket连接失败！");
        e.printStackTrace();
    }

    @OnMessage
    public void onMessage(String msg){
        //将msg封装为MessageFromClient对象
        MessageFromClient messageFromClient = (MessageFromClient) CommUtils.json2Object(msg,MessageFromClient.class);
        //判断聊天类别，并开始发送消息
        if(messageFromClient.getType().equals("1")){
            //群聊发送
            String content = messageFromClient.getMsg();
            Message2Client message2Client = new Message2Client();
            message2Client.setContent(content);
            message2Client.setNames(names);

            for(WebSocket webSocket : clients){
                webSocket.sendMsg(CommUtils.object2Json(message2Client));
            }
        }else if(messageFromClient.getType().equals("2")){
            //私聊发送
            String content = messageFromClient.getMsg();
            int toLength = messageFromClient.getTo().length();
            String tos[] = messageFromClient.getTo().substring(0,toLength-1).split("-");
            List<String> lists = Arrays.asList(tos);
            //找到指定的ID
            for(WebSocket webSocket : clients){
                if(lists.contains(webSocket.session.getId()) && this.session.getId() != webSocket.session.getId()){
                    Message2Client message2Client = new Message2Client();
                    message2Client.setContent(userName,content);
                    message2Client.setNames(names);
                    webSocket.sendMsg(CommUtils.object2Json(message2Client));
                }
            }
        }
    }

    @OnClose
    public void onClose(){
        //先将客户端聊天实体移除
        clients.remove(this);
        //从用户列表中移除该用户ID
        names.remove(session.getId());
        System.out.println(userName+"断开连接！");
        //给当前所有在线的用户发送一个通知
        Message2Client message2Client = new Message2Client();
        message2Client.setContent(userName+"下线了！");
        message2Client.setNames(names);
        String jsonStr = CommUtils.object2Json(message2Client);

        for(WebSocket webSocket : clients){
            webSocket.sendMsg(jsonStr);
        }
    }

    public void sendMsg(String msg){
        try{
            this.session.getBasicRemote().sendText(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
