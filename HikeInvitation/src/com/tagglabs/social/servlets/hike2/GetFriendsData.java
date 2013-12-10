package com.tagglabs.social.servlets.hike2;

import java.io.IOException;
import java.util.SortedMap;
import java.util.TreeMap;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

@WebServlet(name = "GetFriendsData", urlPatterns = {"/getFriends"})
public class GetFriendsData extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        JSONArray array = new JSONArray();
        try {
            SortedMap<String, String> nameMap = new TreeMap<String, String>();
            String fbAuthToken = request.getParameter("access_token");
            String newUrl = "https://graph.facebook.com/fql?q=select%20name%2C%20uid%20from%20user%20where%20uid%20IN%20(select%20uid2%20from%20friend%20where%20uid1%3Dme())&access_token=" + fbAuthToken;
            HttpClient httpclient = new DefaultHttpClient();
            HttpGet httpget = new HttpGet(newUrl);
            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            String responseBody = httpclient.execute(httpget, responseHandler);
            JSONObject json = (JSONObject) JSONSerializer.toJSON(responseBody);
            array = json.getJSONArray("data");
            JSONObject postData;
            for (int i = 0; i < array.size(); i++) {
                postData = array.getJSONObject(i);
                nameMap.put(postData.getString("name"), postData.getString("uid"));
            }
            array = new JSONArray();
            for (String frndName : nameMap.keySet()) {
                postData = new JSONObject();
                postData.put("name", frndName);
                postData.put("uid", nameMap.get(frndName));
                array.add(postData);
            }
            response.getWriter().write(array.toString(), 0, array.toString().length());
        } catch (Exception e) {
            response.getWriter().write(array.toString(), 0, array.toString().length());
        } finally {
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
}
