package com.waston.utils;

import java.sql.*;
import java.util.Properties;

/**
 * @Description: JDBC操作的公共方法
 * @Author: Waston
 * @Date: 2019/7/30 19:35
 */
public class JDBCUtils {
    private static String driverName;
    private static String url;
    private static String username;
    private static String password;

    static {
        Properties properties = CommUtils.loadProperties("db.properties");
        driverName = properties.getProperty("driverName");
        url = properties.getProperty("url");
        username = properties.getProperty("userName");
        password = properties.getProperty("password");
        //加载驱动
        try {
            Class.forName(driverName);
        } catch (ClassNotFoundException e) {
            System.out.println("加载驱动时出错");
        }
    }

    // 获取数据库连接
    public static Connection getConnection(){
        try {
            return DriverManager.getConnection(url,username,password);
        } catch (SQLException e) {
            System.out.println("获取数据库连接出错");
        }
        return null;
    }

    /**
     * 关闭
     * @param connection
     * @param statement
     * @param resultSet
     */
    public static void closeResources(Connection connection,
                                      Statement statement,
                                      ResultSet resultSet){
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        if (statement != null) {
            try {
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


}
