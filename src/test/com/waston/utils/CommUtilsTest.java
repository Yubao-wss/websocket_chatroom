package com.waston.utils;

import org.junit.Assert;
import org.junit.Test;

import java.util.Properties;

import static org.junit.Assert.*;

/**
 * @Description:
 * @Author: Waston
 * @Date: 2019/7/30 19:12
 */
public class CommUtilsTest {

    @Test
    public void loadProperties() {
        String fileName = "db.properties";
        Properties properties = CommUtils.loadProperties(fileName);
        String url = properties.getProperty("url");
        Assert.assertNotNull(url);
    }
}