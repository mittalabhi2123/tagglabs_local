package com.tagglabs.social.utils;

import java.sql.Connection;
import java.sql.DriverManager;

public class Utility {
    
    public static final String FB_REDIRECT_URL = "http://54.200.86.247:8084/vhs/index.sec";
    public static final String TWITTER_REDIRECT_URL = "http://54.200.86.247:8084/vhs/index.twt";
    public static final String FB_APP_ID = "240917439402248";
    public static final String FB_APP_SECRET = "7700a762dcc7a1e7fb6c74e9c3b35782";
            
    public static final String TWITTER_CONSUMER_KEY = "AJEoHE2loR8GY9m5X17A";
    public static final String TWITTER_CONSUMER_SECRET = "jQqTKWDNW2XLLxZ6yowEfax3QjScJ848Bg89NQUBt5E";
    
    public static final String HASH_TAG = "#ihike";
        
    public static final String EMAIL = "kfumirror@gmail.com";
    public static final String PASSWORD = "12345@six";
    static Connection conn;
    public static Connection getConnection() throws Exception{
        if (conn == null || conn.isClosed() || !conn.isValid(10)){
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/vhs", "root", "root");    
        }
        return conn;
    }
}
