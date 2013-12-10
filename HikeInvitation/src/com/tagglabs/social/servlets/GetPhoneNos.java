
package com.tagglabs.social.servlets;

import com.tagglabs.social.utils.Utility;
import java.io.IOException;
import java.sql.ResultSet;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONArray;

@WebServlet(name = "GetPhoneNos", urlPatterns = {"/getPhoneNo"})
public class GetPhoneNos extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        JSONArray dataArr = new JSONArray();
        try {
            ResultSet rs = Utility.getConnection().createStatement().executeQuery("SELECT phone_no FROM users");
            while(rs.next())
                dataArr.put(rs.getString(1));
            response.getWriter().write(dataArr.toString(), 0, dataArr.toString().length());
        } catch(Exception e){
            response.getWriter().write(dataArr.toString(), 0, dataArr.toString().length());
        }finally {       
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

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}
