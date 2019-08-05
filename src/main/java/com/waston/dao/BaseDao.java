package com.waston.dao;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidDataSourceFactory;
import com.waston.utils.CommUtils;

import javax.sql.DataSource;
import java.sql.*;
import java.util.Properties;

/**
 * @Description: 封装基础操作，数据源、获取连接、关闭资源
 * @Author: Waston
 * @Date: 2019/8/3 9:52
 */
public class BaseDao {
    private static DataSource dataSource;

    //加载数据源
    static {
        Properties properties = CommUtils.loadProperties("datasource.properties");
        try{
            dataSource = DruidDataSourceFactory.createDataSource(properties);
        } catch (Exception e) {
            System.err.println("数据源加载失败");
            e.printStackTrace();
        }
    }

    //获取数据库连接
    protected Connection getConnection(){
        try{
            return dataSource.getConnection();
        } catch (SQLException e) {
            System.err.println("获取连接失败");
            e.printStackTrace();
        }
        return null;
    }

    //关闭数据库连接的资源
    protected void closeResources(Connection connection,Statement statement){
        if(connection != null){
            try{
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        if(statement != null){
            try{
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    protected void closeResources(Connection connection,
                                  Statement statement,
                                  ResultSet resultSet) {
        closeResources(connection,statement);
        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
