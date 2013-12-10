package com.tagglabs.social.utils;

import java.sql.Connection;
import java.sql.DriverManager;

public class Utility {
    
    public static final String FB_REDIRECT_URL = "http://localhost:8080/invitation/index.sec";
    public static final String TWITTER_REDIRECT_URL = "http://localhost:8080/invitation/index.twt";
    public static final String FB_APP_ID = "1402637849975673";
    public static final String FB_APP_SECRET = "d8d14186bb9dd3842bfe50c7e855eb17";
            
    public static final String TWITTER_CONSUMER_KEY = "AJEoHE2loR8GY9m5X17A";
    public static final String TWITTER_CONSUMER_SECRET = "jQqTKWDNW2XLLxZ6yowEfax3QjScJ848Bg89NQUBt5E";
    
    public static final String HASH_TAG = "#ihike";
        
    public static final String EMAIL = "hikecollejing@gmail.com";
    public static final String PASSWORD = "12345@six";
    
    public static final String filePath="\\usr\\local\\google\\phone.txt";
    static Connection conn;
    public static Connection getConnection() throws Exception{
        if (conn == null || conn.isClosed() || !conn.isValid(10)){
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/invitation", "root", "root");    
        }
        return conn;
    }
}
