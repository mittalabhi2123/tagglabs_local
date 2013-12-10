
package com.tagglabs.social.servlets;

import com.tagglabs.social.utils.Utility;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.json.JSONSerializer;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.util.JSONTokener;

@WebServlet(name = "GetUserTextData", urlPatterns = {"/getUserTextData"})
public class GetUserTextData extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String phnNo = request.getParameter("phone");
        HttpClient client = null;
        try {
            client = new DefaultHttpClient();
            ResultSet rs = Utility.getConnection().createStatement().executeQuery("SELECT a.city, a.fb_auth_token, b.fb_photo_id FROM users a, user_fb_photo b where b.phone_no LIKE '%"+ phnNo +"%' AND a.phone_no LIKE '%"+phnNo+"%'");
            while(rs.next()){
                System.out.println("Got Data");
                String city = rs.getString(1);
                String authToken = rs.getString(2);
                String photoId = rs.getString(3);
                JSONObject obj = new JSONObject();
                obj.put("city", city);
                getUserId(authToken, client, obj);
                obj.put("like", runFbJob(authToken, client, photoId));
            response.getWriter().write(obj.toString(), 0, obj.toString().length());
            break;
            }
        } catch (Exception e){
            throw new RuntimeException(e);
        }finally {     
            client.getConnectionManager().shutdown();   
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
    
    private String getUserId(String accessToken, HttpClient client, JSONObject obj) throws IOException {
        String newUrl = "https://graph.facebook.com/me?access_token=" + accessToken;
        HttpGet httpget = new HttpGet(newUrl);
        ResponseHandler<String> responseHandler = new BasicResponseHandler();
        String responseBody = client.execute(httpget, responseHandler);
        JSONObject json = (JSONObject) JSONSerializer.toJSON(responseBody);
//        System.out.println(json.toString());
//        System.out.println(json.getString("first_name") + ":::" + json.getString("last_name"));
        obj.put("firstName", json.getString("first_name"));
        obj.put("lastName", json.getString("last_name"));
        return json.getString("id");
    }

    private String runFbJob(String fbAuthToken, HttpClient httpclient, String photoId) throws Exception {
        String newUrl = "https://graph.facebook.com/fql?q=SELECT%20like_info%20FROM%20photo%20WHERE%20object_id%3D"+ photoId +"&access_token=" + fbAuthToken;
        System.out.println(newUrl);
        HttpGet httpget = new HttpGet(newUrl);
        ResponseHandler<String> responseHandler = new BasicResponseHandler();
        String responseBody = httpclient.execute(httpget, responseHandler);
        JSONObject json = (JSONObject) JSONSerializer.toJSON(responseBody);
        JSONArray array = json.getJSONArray("data");
        try{
        if (array.size() > 0){
//            System.out.println(array.getJSONObject(0).toString());
//            System.out.println("Likes::"+array.getJSONObject(0).getString("like_count"));
            String responseData = array.getJSONObject(0).toString();
            return responseData.split("like_count")[1].substring(2,3);
        }
        }catch(Exception e){
            e.printStackTrace();
            return "0";
        }
        return "0";
    }
}
