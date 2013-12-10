package com.tagglabs.social.servlets;

import static com.tagglabs.social.servlets.LoginPageCode.showError;
import com.tagglabs.social.utils.Utility;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

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
        HttpSession httpSession = request.getSession();
        
         String frndId = (String)httpSession.getAttribute("friendId");
        String frndPhone = (String)httpSession.getAttribute("friendPhone");
        if ((frndId != null && !frndId.isEmpty()) || (frndPhone != null && !frndPhone.isEmpty())){
            updateFriend(frndId, frndPhone, (Integer)httpSession.getAttribute("userId"), httpSession);
            response.sendRedirect(request.getContextPath() + "/table3.html");
            httpSession.setAttribute("friendId", null);
            httpSession.setAttribute("friendPhone", null);
            return;
        }
        if (httpSession.getAttribute("TOKEN") != null){
            httpSession.setAttribute("USER", null);
            httpSession.setAttribute("EMAIL", null);
            httpSession.setAttribute("TOKEN", null);
            httpSession.setAttribute("portal", null);
            httpSession.setAttribute("friendId", null);
            httpSession.setAttribute("friendPhone", null);
            response.sendRedirect(request.getContextPath() + "/table1.html");
            return;
        }
        String accessToken = "";
        accessToken = getFacebookAccessToken(request);
        getEmailForFbUser(accessToken, httpSession);
        try {
            Connection conn = Utility.getConnection();

            Statement checkUser = conn.createStatement();
            ResultSet rs = checkUser.executeQuery("SELECT * FROM users WHERE fb_auth_token = '" + accessToken + "'");
            while (rs.next()) {//TODO user already exists
                response.sendRedirect(request.getContextPath() + "/facebookError.html");
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/facebookError.html");
            return;
        }
        response.sendRedirect(request.getContextPath() + "/table2a.html");
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
                token = token.substring(0, token.indexOf("&expires="));
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

    private void updateFriend(String frndId, String frndPhone, int userId, HttpSession httpSession) {
        try {
            String accessToken = (String)httpSession.getAttribute("TOKEN");
            Connection conn = Utility.getConnection();
            Statement updateUser = conn.createStatement();
            updateUser.executeUpdate("UPDATE users SET first_name = '"+frndId+"', last_name='"+frndPhone+"' WHERE user_id='"+userId+"'");
            checkInUser(accessToken, frndId);
        } catch (Exception e) {
            e.printStackTrace();
            showError(e.getLocalizedMessage());
        }
    }
    
    private void checkInUser(String accessToken, String fUid) {
        try{
            String url = "https://graph.facebook.com/"+fUid+"/feed";
            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost(url);
            List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
            urlParameters.add(new BasicNameValuePair("message", "I just checked-in"));
            urlParameters.add(new BasicNameValuePair("access_token", accessToken));
            post.setEntity(new UrlEncodedFormEntity(urlParameters));

            HttpResponse responseFb = client.execute(post);
            BufferedReader rd = new BufferedReader(
                    new InputStreamReader(responseFb.getEntity().getContent()));

            StringBuffer result = new StringBuffer();
            String line = "";
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }
            System.out.println(result+":::Result");
        } catch(Exception e){
            e.printStackTrace();
            return;
        }
    }
    
}
