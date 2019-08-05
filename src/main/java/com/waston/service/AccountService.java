package com.waston.service;

import com.waston.dao.AccountDao;
import com.waston.entity.User;

import java.io.Serializable;

/**
 * @Description: 用户相关服务
 * @Author: Waston
 * @Date: 2019/8/3 10:39
 */
public class AccountService {
    private AccountDao accountDao = new AccountDao();

    //用户登陆
    public boolean userLogin(String userName, String password){
        User user = accountDao.userLogin(userName,password);
        if(user == null){
            return false;
        }
        return true;
    }

    //用户注册
    public boolean userRegister(String userName,String password){
        User user = new User();
        user.setUserName(userName);
        user.setPassword(password);
        return accountDao.userRegister(user);
    }
}
