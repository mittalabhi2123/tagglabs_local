
package com.tagglabs.social.servlets;

import com.tagglabs.social.utils.Utility;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(name = "FbLogout", urlPatterns = {"/logout"})
public class FbLogout extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try{
            HttpSession httpSession = request.getSession();
            int userId = Integer.parseInt(httpSession.getAttribute("userId").toString());
            Connection conn = Utility.getConnection();

            Statement checkUser = conn.createStatement();
            ResultSet rs = checkUser.executeQuery("SELECT fb_auth_token FROM users WHERE user_id = '" + userId + "'");
            while (rs.next()) {//TODO user already exists
                String accessToken = rs.getString(1);
                if(accessToken == null || accessToken.isEmpty()){
                    response.sendRedirect(request.getContextPath() + "/social.jsf");
                    return;
                }
                System.out.println("fb_auth_token......" + accessToken);
                response.sendRedirect("https://www.facebook.com/logout.php?confirm=1&next=" + Utility.FB_REDIRECT_URL + "&access_token=" + accessToken);
                break;
            }
            
        } catch(Exception e){
            e.printStackTrace();
        }
        
    }
}
