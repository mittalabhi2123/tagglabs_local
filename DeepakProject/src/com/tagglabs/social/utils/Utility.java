package com.tagglabs.social.utils;

import java.sql.Connection;
import java.sql.DriverManager;

public class Utility {
    
    public static final String FB_REDIRECT_URL = "http://54.200.86.247:8084/Test/index.sec";
    public static final String TWITTER_REDIRECT_URL = "http://54.200.86.247:8084/Test/index.twt";
    public static final String FB_APP_ID = "1397704090459265";
    public static final String FB_APP_SECRET = "0603859bfaca6ce5aa092a47d1f291d5";
            
    public static final String TWITTER_CONSUMER_KEY = "AJEoHE2loR8GY9m5X17A";
    public static final String TWITTER_CONSUMER_SECRET = "jQqTKWDNW2XLLxZ6yowEfax3QjScJ848Bg89NQUBt5E";
    
    public static final String HASH_TAG = "#ihike";
        
    public static final String EMAIL = "hikecollejing@gmail.com";
    public static final String PASSWORD = "12345six";
    
    static Connection conn;
    public static Connection getConnection() throws Exception{
        if (conn == null || conn.isClosed() || !conn.isValid(10)){
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/tagglabs", "root", "root");    
        }
        return conn;
    }
}
