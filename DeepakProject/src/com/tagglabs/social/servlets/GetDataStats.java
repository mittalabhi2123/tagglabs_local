/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tagglabs.social.servlets;

import com.tagglabs.social.utils.Utility;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author abhishekmitt
 */
@WebServlet(name = "GetDataStats", urlPatterns = {"/getData"})
public class GetDataStats extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            Connection conn = Utility.getConnection();
            ResultSet rs = conn.createStatement().executeQuery("SELECT phone_no, city, created_on FROM users ORDER BY created_on");
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MMM/yyyy");
            LinkedHashMap<String, List<String>> dataMap = new LinkedHashMap<String, List<String>>();
            List<String> phnNoLst = null;
            int total = 0;
            while(rs.next()){
                total++;
                String date = sdf.format(new Date(rs.getTimestamp(3).getTime()));
                if (!dataMap.containsKey(date))
                    dataMap.put(date, new ArrayList<String>());
                dataMap.get(date).add(rs.getString(2) + ":" + rs.getString(1));
            }
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Get data for users</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Total Records: " + total + "</h1>");
            for (String date : dataMap.keySet()){
                phnNoLst = dataMap.get(date);
                out.println("<h2>For Date: " + date + "(" + phnNoLst.size() + ")</h2>");
                out.println("<table>");
                out.println("<tr><td style=\"width:30%\"><b><u>CITY</u></b></td><td style=\"width:30%\"><b><u>PHONE #</u></b></td></tr>");
                for (String phnData:phnNoLst){
                    out.println("<tr><td>"+phnData.split(":")[0]+"</td><td>"+phnData.split(":")[1]+"</td></tr>");
                }
                out.println("</table>");
            }
            /* TODO output your page here. You may use following sample code. */
            
            
            out.println("</body>");
            out.println("</html>");
        } catch(Exception e){
            throw new RuntimeException(e);
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
}
