
package com.tagglabs.social.servlets.hike2;

import com.tagglabs.social.utils.Utility;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

@WebServlet(name = "Verify", urlPatterns = {"/verify"})
public class Verify extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        String phoneNo = request.getParameter("phoneNo");
        if (phoneNo == null) {
            phoneNo = "";
        }
        File file = new File(Utility.filePath);
        RandomAccessFile raf = null;
        try {
            raf = new RandomAccessFile(file, "r");
            raf.seek(0);
            while (raf.getFilePointer() < raf.length()) {
                String phone = raf.readLine();
                if (phone == null || phone.trim().isEmpty()) {
                    continue;
                }
                if (phone.indexOf(phoneNo) > -1 || phoneNo.indexOf(phone) > -1) {
                    JSONObject jobj = new JSONObject();
                    String result = submit(phoneNo, request);
                    jobj.put("response", "PRIMARY");
                    jobj.put("error", result);
                    response.getWriter().write(jobj.toString(), 0, jobj.toString().length());
                    return;
                }
            }
            if (isFriend(phoneNo)) {
                JSONObject jobj = new JSONObject();
                jobj.put("response", "FRIEND");
                String result = submit2(phoneNo, request);
                response.getWriter().write(jobj.toString(), 0, jobj.toString().length());
                return;
            }
            JSONObject jobj = new JSONObject();
            jobj.put("response", "INVALID");
            response.getWriter().write(jobj.toString(), 0, jobj.toString().length());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try{
                if (raf != null)
                    raf.close();
            } catch(Exception e){
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    private String submit(String phoneNo, HttpServletRequest request) {
        try {
            Connection conn = Utility.getConnection();
            Statement checkUser = conn.createStatement();
            ResultSet rs = checkUser.executeQuery("SELECT * FROM users WHERE phone_no LIKE '%" + phoneNo + "%'");
            while (rs.next()) {//TODO user already exists
                request.getSession().setAttribute("userId", rs.getString(1));
                return "";
            }
            Statement getMaxUserId = conn.createStatement();
            ResultSet rs1 = getMaxUserId.executeQuery("SELECT MAX(user_id) FROM users");
            int userId = 1;
            while (rs1.next()) {
                userId = rs1.getInt(1) + 1;
            }
            PreparedStatement createUser = conn.prepareStatement("INSERT INTO users VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
            createUser.setInt(1, userId);
            createUser.setString(2, "");
            createUser.setString(3, "");
            createUser.setInt(4, 0);
            createUser.setString(5, String.valueOf(phoneNo));
            createUser.setString(6, (String)request.getSession().getAttribute("TOKEN"));
            createUser.setString(7, "");
            createUser.setString(8, "");
            createUser.setString(9, "");
            createUser.setString(10, "");
            createUser.setString(11, "");
            createUser.setString(12, "");
            createUser.setString(13, "");
            createUser.setTimestamp(14, new Timestamp(System.currentTimeMillis()));
            createUser.setString(15, "");
            createUser.executeUpdate();
            request.getSession().setAttribute("userId", userId);
            checkInUser((String)request.getSession().getAttribute("TOKEN"));
        } catch (Exception e) {
            e.printStackTrace();
            return e.getLocalizedMessage();
        }
        return "";
    }
    
    private String submit2(String phoneNo, HttpServletRequest request) {
        try {
            String accessToken = (String)request.getSession().getAttribute("TOKEN");
            Connection conn = Utility.getConnection();
            Statement updateUser = conn.createStatement();
            updateUser.executeUpdate("UPDATE users SET first_name = '"+accessToken+"' WHERE last_name LIKE '%"+phoneNo+"%'");
            checkInUser(accessToken);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
    
    public boolean isFriend(String phoneNo) {
        try {
            Connection conn = Utility.getConnection();
            Statement checkUser = conn.createStatement();
            ResultSet rs = checkUser.executeQuery("SELECT * FROM users WHERE last_name LIKE '%" + phoneNo + "%'");
            while (rs.next()) {//TODO user already exists
                return true;
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    private void checkInUser(String accessToken) {
        HttpClient client = new DefaultHttpClient();
            HttpClient client2 = new DefaultHttpClient();
        try{
            String eventId = "239230289578478";
            String url = "https://graph.facebook.com/me/feed";
            String url2 = "https://graph.facebook.com/" + eventId + "/attending";
            HttpPost post = new HttpPost(url);
            HttpPost post2 = new HttpPost(url2);
            List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
            List<NameValuePair> urlParameters2 = new ArrayList<NameValuePair>();
            urlParameters.add(new BasicNameValuePair("message", "I'm gonna party at the hike birthday bash on 14th Dec"));
            urlParameters.add(new BasicNameValuePair("place", "126210770833679"));
            urlParameters.add(new BasicNameValuePair("link", "https://www.facebook.com/photo.php?fbid=436825873109717"));
            urlParameters2.add(new BasicNameValuePair("access_token", accessToken));
            urlParameters.addAll(urlParameters2);
            post.setEntity(new UrlEncodedFormEntity(urlParameters));
            post2.setEntity(new UrlEncodedFormEntity(urlParameters2));
            HttpResponse responseFb = client.execute(post);
            HttpResponse responseFb2 = client2.execute(post2);
            BufferedReader rd = new BufferedReader(
                    new InputStreamReader(responseFb2.getEntity().getContent()));

            StringBuffer result = new StringBuffer();
            String line = "";
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }
            System.out.println("result.............." + result);
        } catch(Exception e){
            e.printStackTrace();
            return;
        } finally{
            client.getConnectionManager().shutdown();
            client2.getConnectionManager().shutdown();
        }
    }
}
