/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tagglabs.social.servlets;

import com.tagglabs.social.utils.Utility;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Arrays;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import twitter4j.Twitter;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;

/**
 *
 * @author abhishekmitt
 */
@WebServlet("*.twt")
public class TwitterCallbackServlet extends HttpServlet {

    Twitter twitter = null;
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		System.out.println("*** Called Twitter");
		HttpSession httpSession = request.getSession();
                int userId = Integer.parseInt(httpSession.getAttribute("userId").toString());
                for (String key : request.getParameterMap().keySet()){
                    System.out.println(key + "===>>>" + Arrays.asList(request.getParameterMap().get(key)));
                }
                String verifier = request.getParameter("oauth_verifier");
                String oauthToken = request.getParameter("oauth_token");
                if (verifier  == null || oauthToken == null){
                    response.sendRedirect(request.getContextPath() +"/social.jsf");
                    return;
                }
                try {
                        AccessToken accessToken = getTwitterAccessToken(request);
                        String email = twitter.showUser(twitter.getId()).getScreenName();
                
                        Connection conn = Utility.getConnection();
            
                        Statement updateUser = conn.createStatement();
                        String setQueryStr = "twitter_auth_token = '" + accessToken.getToken() + "', twitter_email = '" + email + "',";
                        setQueryStr = setQueryStr.concat(" twitter_auth_secret = '" + accessToken.getTokenSecret() + "'");

                        Statement checkUser = conn.createStatement();
                        ResultSet rs = checkUser.executeQuery("SELECT * FROM users WHERE twitter_auth_token = '" + accessToken.getToken() + "'");
                        while (rs.next()) {//TODO user already exists
                            System.out.println("twitter_oauthToken......"+rs.getString("twitter_auth_token"));
                            response.sendRedirect(request.getContextPath() +"/facebookError.html");
                           return; 
                        }
                        updateUser.executeUpdate("UPDATE users SET " + setQueryStr + " WHERE user_id = " + userId);
                        
                        httpSession.setAttribute("USER", twitter.showUser(twitter.getId()).getName());
                        httpSession.setAttribute("EMAIL", email);
                        httpSession.setAttribute("portal", "twitter");
                } catch (Exception e) {
                        e.printStackTrace();
                        response.sendRedirect(request.getContextPath() +"/facebookError.html");
                        return;
                }
                response.sendRedirect(request.getContextPath() +"/social.jsf");
	}

	private AccessToken getTwitterAccessToken(HttpServletRequest request) throws Exception{
            
                String verifier = request.getParameter("oauth_verifier");
                twitter = (Twitter)request.getSession().getAttribute("twitter");
                RequestToken requestToken = (RequestToken)request.getSession().getAttribute("requestToken");
                return twitter.getOAuthAccessToken(requestToken, verifier);
                
	}
}
