
package com.tagglabs.social.servlets.hike2;

import com.tagglabs.social.utils.Utility;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.Statement;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.json.JSONObject;

/**
 *
 * @author Administrator
 */
@WebServlet(name = "SaveFrndPhone", urlPatterns = {"/saveFrndPhone"})
public class SaveFrndPhone extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");

        String fUid = request.getParameter("friend_uid");
        fUid = "";
        String fPhone = request.getParameter("friend_phone");
        String phoneNo = request.getParameter("phoneNo");
        request.getSession().setAttribute("friendId", fUid);
        request.getSession().setAttribute("friendPhone", fPhone);
        updateFriend(fUid, fPhone, (Integer)request.getSession().getAttribute("userId"), request.getSession());
            //response.sendRedirect(request.getContextPath() + "/table3.html");
            String url = "https://www.facebook.com/dialog/apprequests?app_id="
                + Utility.FB_APP_ID + "&redirect_uri=" + Utility.FB_REDIRECT_URL
                + "&to=" + fUid + "&message=Hike%20anniversary%20celebration%20invitation";
        try {
            JSONObject jobj = new JSONObject();

            jobj.put("url", url);
            response.getWriter().write(jobj.toString(), 0, jobj.toString().length());
        } catch (Exception e) {
            e.printStackTrace();
        }
            return;
        }

private void updateFriend(String frndId, String frndPhone, int userId, HttpSession httpSession) {
        try {
            String accessToken = (String)httpSession.getAttribute("TOKEN");
            Connection conn = Utility.getConnection();
            Statement updateUser = conn.createStatement();
            updateUser.executeUpdate("UPDATE users SET first_name = '"+frndId+"', last_name='"+frndPhone+"' WHERE user_id='"+userId+"'");
//            checkInUser(accessToken, frndId);
        } catch (Exception e) {
            e.printStackTrace();
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
