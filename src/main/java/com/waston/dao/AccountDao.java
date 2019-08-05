package com.waston.dao;

import com.waston.entity.User;
import org.apache.commons.codec.digest.DigestUtils;

import java.sql.*;

/**
 * @Description: 关于用户模块的dao层
 * @Author: Waston
 * @Date: 2019/8/3 9:58
 */
public class AccountDao extends BaseDao{
    //用户登陆
    public User userLogin (String userName,String password){
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        User user = null;

        try{
            connection = getConnection();
            String sql = "SELECT * FROM user WHERE username = ? AND "+"password = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1,userName);
            preparedStatement.setString(2,DigestUtils.md5Hex(password));
            resultSet = preparedStatement.executeQuery();

            if(resultSet.next()){
                user = getUserInfo(resultSet);
            }
        } catch (SQLException e) {
            System.err.println("查询用户信息失败");
            e.printStackTrace();
        } finally {
            closeResources(connection,preparedStatement,resultSet);
        }
        return user;
    }
    //用户注册
    public boolean userRegister(User user){
        String username = user.getUserName();
        String password = user.getPassword();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        boolean isSuccess = false;
        try{
            connection = getConnection();
            String sql = "INSERT INTO user(username,password)"+"VALUES(?,?)";
            preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1,username);
            preparedStatement.setString(2,DigestUtils.md5Hex(password));
            isSuccess = (preparedStatement.executeUpdate() == 1);
        }catch (Exception e){
            System.out.println("用户注册失败");
            e.printStackTrace();
        }finally {
            closeResources(connection,preparedStatement);
        }
        return isSuccess;
    }
    //将数据表信息封装到User类中
    public User getUserInfo(ResultSet resultSet) throws SQLException {
        User user = new User();
        user.setId(resultSet.getInt("id"));
        user.setUserName(resultSet.getString("username"));
        user.setPassword(resultSet.getString("password"));
        return user;
    }
}
