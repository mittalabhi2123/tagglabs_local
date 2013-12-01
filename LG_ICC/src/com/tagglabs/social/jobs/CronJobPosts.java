package com.tagglabs.social.jobs;

import com.tagglabs.social.utils.Utility;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import twitter4j.DirectMessage;
import twitter4j.HashtagEntity;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.ConfigurationBuilder;

public class CronJobPosts implements Job {

    Connection conn = null;
    HttpClient httpclient = null;
        
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        Date date = new Date();
        System.out.println("Job Started at:::" + date.toString());
        try {
            conn = Utility.getConnection();
            httpclient = new DefaultHttpClient();
        
        ResultSet users = conn.createStatement().executeQuery("SELECT user_id, fb_auth_token, twitter_auth_token, twitter_auth_secret, instagram_auth_token FROM users");
        while (users.next()) {
            int userId = users.getInt(1);
            String fbAuthToken = users.getString(2);
//            String twitterAuthToken = users.getString(3);
//            String twitterAuthSecret = users.getString(4);
//            String instagramAuthToken = users.getString(5);
            
            try{
                if(fbAuthToken!=null && fbAuthToken.length()>0 && !fbAuthToken.equalsIgnoreCase("null"))
                    runFbJob(fbAuthToken, userId);
                } catch(Exception e){
                    e.printStackTrace();
                    System.out.println("User Id::"+userId);
                }
//                try{
//                if(twitterAuthToken!=null && twitterAuthToken.length()>0)
//                    runTwitterJob(twitterAuthToken, twitterAuthSecret, userId);
//                } catch(Exception e){
//                    e.printStackTrace();
//                    System.out.println("User Id::"+userId);
//                }
        }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void runFbJob(String fbAuthToken, int userId) throws Exception {
        PreparedStatement insertPost = conn.prepareStatement("INSERT INTO posts (userId, post, postId, fb_auth_token) VALUES (?, ?, ?, ?)");
        PreparedStatement checkPostId = conn.prepareStatement("SELECT * FROM posts WHERE postId = ?");
        String newUrl = "https://graph.facebook.com/fql?q=SELECT%20message,post_id%20FROM%20stream%20WHERE%20source_id%3Dme()&access_token=" + fbAuthToken;
        System.out.println(newUrl);
        httpclient = new DefaultHttpClient();
        HttpGet httpget = new HttpGet(newUrl);
        ResponseHandler<String> responseHandler = new BasicResponseHandler();
        String responseBody = httpclient.execute(httpget, responseHandler);
        JSONObject json = (JSONObject) JSONSerializer.toJSON(responseBody);
        JSONArray array = json.getJSONArray("data");
        JSONObject postData;
        String message;
        String postId;
        outer:
        for (int i = 0; i < array.size(); i++) {
            postData = array.getJSONObject(i);
            message = postData.getString("message");
            postId = postData.getString("post_id");
            if (message == null || message.equalsIgnoreCase("")) {
                continue;
            }
            if (message.indexOf(Utility.HASH_TAG) <= -1) {
                continue;
            }
            checkPostId.setString(1, postId);
            ResultSet rs = checkPostId.executeQuery();
            while (rs.next()) {
                continue outer;
            }
            insertPost.setInt(1, userId);
            insertPost.setString(2, message);
            insertPost.setString(3, postId);
            insertPost.setString(4, fbAuthToken);
            insertPost.executeUpdate();
        }
    }

    private void runTwitterJob(String twitterAuthToken, String twitterAuthSecret, int userId) throws Exception{
        PreparedStatement insertPost = conn.prepareStatement("INSERT INTO posts (userId, post, postId, twitter_auth_token, twitter_auth_secret) VALUES (?, ?, ?, ?, ?)");
        PreparedStatement checkPostId = conn.prepareStatement("SELECT * FROM posts WHERE postId = ?");
        ConfigurationBuilder builder = new ConfigurationBuilder();
        builder.setOAuthConsumerKey(Utility.TWITTER_CONSUMER_KEY);
        builder.setOAuthConsumerSecret(Utility.TWITTER_CONSUMER_SECRET);
        Twitter twitter = new TwitterFactory(builder.build()).getInstance(new AccessToken(twitterAuthToken, twitterAuthSecret));
        ResponseList<Status> statusList = twitter.getHomeTimeline();
        String message;
        String postId;
        outer:for(Status status : statusList){
            message = status.getText();
            postId = status.getId()+"";
            if (message == null || message.equalsIgnoreCase("")) {
                continue;
            }
            HashtagEntity[] hashTag = status.getHashtagEntities();
            for(HashtagEntity hashTagEntity : hashTag){
                if(Utility.HASH_TAG.equalsIgnoreCase("#"+hashTagEntity.getText())){
                    checkPostId.setString(1, postId);
                    ResultSet rs = checkPostId.executeQuery();
                    while (rs.next()) {
                        continue outer;
                    }
                    insertPost.setInt(1, userId);
                    insertPost.setString(2, message);
                    insertPost.setString(3, postId);
                    insertPost.setString(4, twitterAuthToken);
                    insertPost.setString(5, twitterAuthSecret);
                    insertPost.executeUpdate();
                    continue outer;
                }
            }
        }
    }
}
