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
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.ConfigurationBuilder;

public class CronJobPostsLikes implements Job {

    Connection conn = null;
    PreparedStatement insertPost = null;
            HttpClient httpclient = null;
    public void execute(JobExecutionContext context) throws JobExecutionException {
        Date date = new Date();
        System.out.println("Job2 Started at:::" + date.toString());
        httpclient = new DefaultHttpClient();
        try {
            conn = Utility.getConnection();
            insertPost = conn.prepareStatement("UPDATE posts SET likes = ?, comments = ? WHERE postId = ?");
            ResultSet users = conn.createStatement().executeQuery("SELECT user_id, fb_auth_token, twitter_auth_token, twitter_auth_secret, instagram_auth_token FROM users");
            while (users.next()) {
                int userId = users.getInt(1);
                String fbAuthToken = users.getString(2);
                String twitterAuthToken = users.getString(3);
                String twitterAuthSecret = users.getString(4);
                String instagramAuthToken = users.getString(5);
                try{
                        if(fbAuthToken!=null && fbAuthToken.length()>0)
                            runFbJob(fbAuthToken);
                } catch(Exception e){
                    e.printStackTrace();
                    System.out.println("User Id::"+userId);
                }
                try{
                if(twitterAuthToken!=null && twitterAuthToken.length()>0)
                    runTwitterJob(twitterAuthToken, twitterAuthSecret, userId);
                } catch(Exception e){
                    e.printStackTrace();
                    System.out.println("User Id::"+userId);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void runFbJob(String fbAuthToken) throws Exception {
        String newUrl = "https://graph.facebook.com/fql?q=SELECT%20message,post_id,likes,comment_info%20FROM%20stream%20WHERE%20source_id%3Dme()&access_token=" + fbAuthToken;
        System.out.println(newUrl);
        httpclient = new DefaultHttpClient();
        HttpGet httpget = new HttpGet(newUrl);
        ResponseHandler<String> responseHandler = new BasicResponseHandler();
        String responseBody = httpclient.execute(httpget, responseHandler);
        JSONObject json = (JSONObject) JSONSerializer.toJSON(responseBody);
        JSONArray array = json.getJSONArray("data");
        JSONObject postData;
        JSONObject postLike;
        JSONObject commentLike;
        String postId;
        for (int i = 0; i < array.size(); i++) {
            postData = array.getJSONObject(i);
            String msg = postData.getString("message");
            postId = postData.getString("post_id");
            postLike = postData.getJSONObject("likes");
            commentLike = postData.getJSONObject("comment_info");
            if (postLike.containsKey("count"))
                insertPost.setInt(1, postLike.getInt("count"));
            else
                insertPost.setInt(1, 0);
            insertPost.setInt(2, commentLike.getInt("comment_count"));
            insertPost.setString(3, postId);
            insertPost.executeUpdate();
        }
    }
    
    private void runTwitterJob(String twitterAuthToken, String twitterAuthSecret, int userId) throws Exception{
        ResultSet rs = conn.createStatement().executeQuery("SELECT postId FROM posts WHERE twitter_auth_token IS NOT NULL AND twitter_auth_token != ''");
        ConfigurationBuilder builder = new ConfigurationBuilder();
        builder.setOAuthConsumerKey(Utility.TWITTER_CONSUMER_KEY);
        builder.setOAuthConsumerSecret(Utility.TWITTER_CONSUMER_SECRET);
            Twitter twitter = new TwitterFactory(builder.build()).getInstance(new AccessToken(twitterAuthToken, twitterAuthSecret));
        while(rs.next()){
            String postId = rs.getString(1);
            long favCount = twitter.showStatus(Long.parseLong(postId)).getFavoriteCount();
            long retweetCount = twitter.showStatus(Long.parseLong(postId)).getRetweetCount();
            insertPost.setLong(1, favCount);
            insertPost.setLong(2, retweetCount);
            insertPost.setString(3, postId);
            insertPost.executeUpdate();
        }
    }
}
