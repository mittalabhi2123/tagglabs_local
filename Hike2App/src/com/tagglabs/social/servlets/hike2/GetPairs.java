
package com.tagglabs.social.servlets.hike2;

import com.tagglabs.social.utils.Utility;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

@WebServlet(name = "GetPairs", urlPatterns = {"/getPairs"})
public class GetPairs extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
        HttpClient client = null;
        try {
            client = new DefaultHttpClient();
            ResultSet rs = Utility.getConnection().createStatement().executeQuery("SELECT first_name, last_name, phone_no, fb_auth_token FROM users");
            
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet GetPairs</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<table>");
            out.println("<tr><td><b>Primary Name</b></td><td><b>Primary Phone#</b></td><td><b>Friend's Name</b></td><td><b>Friend's Phone#</b></td></tr>");
            while(rs.next()){
                System.out.println("Got Data");
                String frndId = rs.getString(1);
                String frndPhone = rs.getString(2);
                String primaryPhn = rs.getString(3);
                String primaryToken = rs.getString(4);
                String selfName = getUserId(primaryToken, client);
                String frndName = getUserId(frndId, client);
                out.println("<tr><td>"+selfName+"</td><td>"+primaryPhn+"</td><td>"+frndName+"</td><td>"+frndPhone+"</td></tr>");
            }
            out.println("</table>");
            out.println("</body>");
            out.println("</html>");
        } catch (Exception e){
            throw new RuntimeException(e);
        }finally {     
            client.getConnectionManager().shutdown();   
        }
        } finally {            
            out.close();
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
    
        private String getUserId(String accessToken, HttpClient client) throws IOException {
            if (accessToken == null || accessToken.isEmpty())
                return "";
        String newUrl = "https://graph.facebook.com/me?access_token=" + accessToken;
        System.out.println(newUrl);
        HttpGet httpget = new HttpGet(newUrl);
        ResponseHandler<String> responseHandler = new BasicResponseHandler();
        String responseBody = client.execute(httpget, responseHandler);
        JSONObject json = (JSONObject) JSONSerializer.toJSON(responseBody);
        return json.getString("name");
    }
}
