package com.tagglabs.social.servlets.hike2;

import com.tagglabs.social.utils.Utility;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONObject;

@WebServlet(name = "Invite", urlPatterns = {"/invite"})
public class Invite extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");

        String fUid = request.getParameter("friend_uid");
        String fPhone = request.getParameter("friend_phone");
        String phoneNo = request.getParameter("phoneNo");
        request.getSession().setAttribute("friendId", fUid);
        request.getSession().setAttribute("friendPhone", fPhone);
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
