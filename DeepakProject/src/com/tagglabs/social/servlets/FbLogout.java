
package com.tagglabs.social.servlets;

import com.tagglabs.social.utils.Utility;
import java.io.IOException;
import java.sql.ResultSet;
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
            String accessToken = httpSession.getAttribute("TOKEN").toString();
            System.out.println("fb_auth_token......" + accessToken);
            response.sendRedirect("https://www.facebook.com/logout.php?confirm=1&next=" + Utility.FB_REDIRECT_URL + "&access_token=" + accessToken);
        } catch(Exception e){
            e.printStackTrace();
        }
        
    }
}
