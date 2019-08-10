package com.waston.config;

import freemarker.template.Configuration;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * @Description: 聊天监听器
 * @Author: Waston
 * @Date: 2019/8/10 16:59
 */
@WebListener
public class FreeMarkerListener implements ServletContextListener {
    public static final String TEMPLATE_KEY = "_template_";
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        Configuration configuration = new Configuration(Configuration.VERSION_2_3_0);
        //加载ftl的路径
        try{
            configuration.setDirectoryForTemplateLoading(new File("F:\\Github\\websocket_chatroom\\src\\main\\webapp"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        //设置页面编码
        configuration.setDefaultEncoding(StandardCharsets.UTF_8.displayName());
        sce.getServletContext().setAttribute(TEMPLATE_KEY,configuration);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }
}
