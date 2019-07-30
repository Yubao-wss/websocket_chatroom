package com.waston.utils;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidDataSourceFactory;
import com.alibaba.druid.pool.DruidPooledConnection;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

/**
 * @Description: 基于DruidDataSource
 * @Author: Waston
 * @Date: 2019/7/30 20:59
 */
public class DataSourceUtils {
    private static DruidDataSource druidDataSource;

    static {
        Properties properties = CommUtils.loadProperties("datasource.properties");
        try {
            druidDataSource = (DruidDataSource) DruidDataSourceFactory.createDataSource(properties);
        } catch (Exception e) {
            System.out.println("获取数据源失败");
        }
    }

    public static DruidPooledConnection getConnection(){
        try {
            return druidDataSource.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void closeResources(Connection connection,
                                      Statement statement){
         if(connection != null){
             try {
                 connection.close();
             } catch (SQLException e) {
                 e.printStackTrace();
             }
         }

         if(statement != null){
             try {
                 statement.close();
             } catch (SQLException e) {
                 e.printStackTrace();
             }
         }
    }

}
