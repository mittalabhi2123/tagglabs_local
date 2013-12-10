
package com.tagglabs.social.servlets.hike2;

import com.tagglabs.social.utils.Utility;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.json.JSONObject;

@WebServlet(name = "FacebookLogin", urlPatterns = {"/facebookLogin"})
public class FacebookLogin extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        HttpSession session = request.getSession();
        String sessionId = session.getId();
        String url = "https://www.facebook.com/dialog/oauth?client_id="
                + Utility.FB_APP_ID + "&redirect_uri=" + Utility.FB_REDIRECT_URL
                + "&scope=email,user_birthday,read_stream,user_about_me,photo_upload&state=" + sessionId;
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
