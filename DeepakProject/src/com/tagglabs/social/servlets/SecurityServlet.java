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

import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

import org.apache.commons.lang.StringUtils;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

@WebServlet("*.sec")
public class SecurityServlet extends HttpServlet {

    private static final long serialVersionUID = 8071426090770097330L;

    public SecurityServlet() {
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        System.out.println("*** Called SecurityServlet");
        HttpSession httpSession = request.getSession();
        String accessToken = "";
        accessToken = getFacebookAccessToken(request);
        getEmailForFbUser(accessToken, httpSession);
        try {
            Connection conn = Utility.getConnection();
            System.out.println(accessToken);

            Statement checkUser = conn.createStatement();
            ResultSet rs = checkUser.executeQuery("SELECT * FROM users WHERE fb_auth_token = '" + accessToken + "'");
            while (rs.next()) {//TODO user already exists
                System.out.println("fb_auth_token......" + rs.getString("fb_auth_token"));
                response.sendRedirect(request.getContextPath() + "/facebookError.html");
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/facebookError.html");
            return;
        }
        response.sendRedirect(request.getContextPath() + "/login.jsf");
    }

    private String getFacebookAccessToken(HttpServletRequest request) {
        String faceCode = request.getParameter("code");
        String token = null;
        if (faceCode != null && !"".equals(faceCode)) {
            String newUrl = "https://graph.facebook.com/oauth/access_token?client_id="
                    + Utility.FB_APP_ID + "&redirect_uri=" + Utility.FB_REDIRECT_URL + "&client_secret="
                    + Utility.FB_APP_SECRET + "&code=" + faceCode;
            HttpClient httpclient = new DefaultHttpClient();
            try {
                HttpGet httpget = new HttpGet(newUrl);
                ResponseHandler<String> responseHandler = new BasicResponseHandler();
                String responseBody = httpclient.execute(httpget, responseHandler);
                token = StringUtils.removeStart(responseBody, "access_token=");
                System.out.println(token);
                token = token.substring(0, token.indexOf("&expires="));
                System.out.println(token);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                httpclient.getConnectionManager().shutdown();
            }
        }
        return token;
    }

    private String getEmailForFbUser(String accessToken, HttpSession httpSession) {
        String email = "";
        HttpClient httpclient = new DefaultHttpClient();
        try {
            if (accessToken != null && !"".equals(accessToken)) {
                String newUrl = "https://graph.facebook.com/me?access_token=" + accessToken;
                httpclient = new DefaultHttpClient();
                HttpGet httpget = new HttpGet(newUrl);
                System.out.println("Get info from face --> executing request: " + httpget.getURI());
                ResponseHandler<String> responseHandler = new BasicResponseHandler();
                String responseBody = httpclient.execute(httpget, responseHandler);
                JSONObject json = (JSONObject) JSONSerializer.toJSON(responseBody);
                String facebookId = json.getString("id");
                String firstName = json.getString("first_name");
                String lastName = json.getString("last_name");
                if (json.containsKey("email"))
                    email = json.getString("email");
                httpSession.setAttribute("USER", firstName + " " + lastName);
                httpSession.setAttribute("EMAIL", email);
                httpSession.setAttribute("TOKEN", accessToken);
                httpSession.setAttribute("portal", "facebook");

            } else {
                System.err.println("Token za facebook je null");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            httpclient.getConnectionManager().shutdown();
        }
        return email;
    }
}
