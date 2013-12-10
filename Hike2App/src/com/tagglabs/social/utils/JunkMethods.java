/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tagglabs.social.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import twitter4j.StatusUpdate;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.ConfigurationBuilder;

/**
 *
 * @author abhishekmitt
 */
public class JunkMethods {
//        private void checkInUser(String accessToken, String city, String twtToken, String twtSecret) {
//        try{
//        ResultSet rs = Utility.getConnection().createStatement().executeQuery("SELECT placeId, eventId, placeCaption FROM day_college WHERE city = '" + city + "' LIMIT 1");
//        while (rs.next()) {
//            if (accessToken != null && !accessToken.isEmpty()){
//                String placePageId = rs.getString(1);
//                if (placePageId  == null || placePageId.isEmpty())
//                    return;
//                String url = "https://graph.facebook.com/me/feed";
//                HttpClient client = new DefaultHttpClient();
//                HttpPost post = new HttpPost(url);
//                List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
//                urlParameters.add(new BasicNameValuePair("message", rs.getString(3)));
//                urlParameters.add(new BasicNameValuePair("place", placePageId));
//                urlParameters.add(new BasicNameValuePair("access_token", accessToken));
//                post.setEntity(new UrlEncodedFormEntity(urlParameters));
//                HttpResponse responseFb = client.execute(post);
//                BufferedReader rd = new BufferedReader(
//                        new InputStreamReader(responseFb.getEntity().getContent()));
//                StringBuffer result = new StringBuffer();
//                String line = "";
//                while ((line = rd.readLine()) != null) {
//                    result.append(line);
//                }
//            } else {
//                ConfigurationBuilder builder = new ConfigurationBuilder();
//                builder.setOAuthConsumerKey(Utility.TWITTER_CONSUMER_KEY);
//                builder.setOAuthConsumerSecret(Utility.TWITTER_CONSUMER_SECRET);
//                Twitter twitter = new TwitterFactory(builder.build()).getInstance(new AccessToken(twtToken, twtSecret));
//                twitter.updateStatus(new StatusUpdate(rs.getString(3)));
//            }
//        }
//        } catch(Exception e){
//            e.printStackTrace();
//            return;
//        }
//    }
//        
//        
//    
//    public String getTwitterUrlAuth() {;
//        Twitter twitter = new TwitterFactory().getInstance();
//        twitter.setOAuthConsumer(Utility.TWITTER_CONSUMER_KEY, Utility.TWITTER_CONSUMER_SECRET);
//        try {
//            RequestToken requestToken = twitter.getOAuthRequestToken(Utility.TWITTER_REDIRECT_URL);
//            HttpSession session =
//                    (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
//            session.setAttribute("twitter", twitter);
//            session.setAttribute("requestToken", requestToken);
//            return requestToken.getAuthorizationURL();
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }
//    
//    
//
//    public void loadData(AjaxBehaviorEvent event) {
//        System.out.println("loadData*************************");
//        phoneNo = getPhoneNo();
//        try {
//            if (phoneNo == null || phoneNo.equals("")) {
//                dataExists = false;
//                ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
//                ec.redirect(((HttpServletRequest) ec.getRequest()).getRequestURI());
//                return;
//            }
//            Connection conn = Utility.getConnection();
//            Statement checkUser = conn.createStatement();
//            ResultSet rs = checkUser.executeQuery("SELECT * FROM users WHERE phone_no = '" + phoneNo + "'");
//            int userId = 0;
//            while (rs.next()) {//TODO user already exists
//                dataExists = true;
//                userId = rs.getInt(1);
//                ((HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false)).setAttribute("userId", userId);
//            }
//            if (userId == 0) {
//                dataExists = false;
//            }
//            ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
//            ec.redirect(((HttpServletRequest) ec.getRequest()).getRequestURI());
//            return;
//        } catch (Exception e) {
//            e.printStackTrace();
//            showError(e.getLocalizedMessage());
//        }
//    }
}
