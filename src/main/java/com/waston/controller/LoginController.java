package com.waston.controller;

import com.waston.config.FreeMarkerListener;
import com.waston.service.AccountService;
import com.waston.utils.CommUtils;
import freemarker.core.ParseException;
import freemarker.template.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.temporal.Temporal;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description:
 * @Author: Waston
 * @Date: 2019/8/6 9:49
 */
@WebServlet(urlPatterns = "/login")
public class LoginController extends HttpServlet {
    private AccountService accountService = new AccountService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String userName = req.getParameter("username");
        String password = req.getParameter("password");
        resp.setContentType("text/html;charaset=utf8");
        PrintWriter out = resp.getWriter();
        if(CommUtils.strIsNull(userName) || CommUtils.strIsNull(password)){
            //登陆失败，留在登陆页面
            out.println("    <script>\n" +
                    "        alert(\"用户名或密码为空!\");\n" +
                    "        window.location.href = \"/index.html\";\n" +
                    "    </script>");
        }
        if(accountService.userLogin(userName,password)){
            //登陆成功
            //加载聊天室ftl页面
            Template temlate = getTemplate(req,"/chat.ftl");
            Map<String,String> userMap = new HashMap<>();
            userMap.put("username",userName);
            try {
                temlate.process(userMap,out);
            } catch (TemplateException e) {
                e.printStackTrace();
            }
        }else {
            //登陆失败
            out.println("    <script>\n" +
                    "        alert(\"用户名不存在或密码不正确!\");\n" +
                    "        window.location.href = \"/index.html\";\n" +
                    "    </script>");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req,resp);
    }

    /**
     * 创建聊天室环境的方法
     * @param req
     * @param fileName
     * @return
     */
    private Template getTemplate(HttpServletRequest req,String fileName){
        Configuration cfg = (Configuration) req.getServletContext().getAttribute(FreeMarkerListener.TEMPLATE_KEY);
            try{
                return cfg.getTemplate(fileName);
            }  catch (IOException e) {
                e.printStackTrace();
            }
            return null;
    }

}
