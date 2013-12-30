
package com.tagglabs.social.servlets;

import com.tagglabs.social.utils.Utility;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "DeletePic", urlPatterns = {"/deletePic"})
public class DeletePic extends HttpServlet {
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            List<String> phonesLst = new ArrayList<String>();
            Set<String> unusedPicLst = new HashSet<String>();
            ResultSet rs = Utility.getConnection().createStatement().executeQuery("SELECT phone_no FROM users");
            while(rs.next()){
                phonesLst.add(rs.getString(1));
            }
            String phoneNo = request.getParameter("phone");
            File file = new File("C:\\tmp");
            for (File file1 : file.listFiles()){
                if(phoneNo != null){
                    if(file1.getName().split("[.]")[0].indexOf(phoneNo) > -1 || phoneNo.indexOf(file1.getName().split("[.]")[0]) > -1) {
                        file1.delete();
                        continue;
                    }
                }
                for(String phnNo: phonesLst){
                    if (phnNo.indexOf(file1.getName().split("[.]")[0]) > -1 || file1.getName().split("[.]")[0].indexOf(phnNo) > -1)
                        continue;
                    System.out.println(file1.getName().split("[.]")[0] + " ::::: " + phnNo);
                    unusedPicLst.add(file1.getName());
                }
            }
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet DeletePic</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Deleted Successfully</h1>");
            out.println("<table>");
            int i = 0;
            for (String pic:unusedPicLst){
                out.println("<tr><td>"+ ++i +"</td><td>"+pic+"</td></tr>");
            }
            out.println("</table>");
            out.println("</body>");
            out.println("</html>");
        } catch(Exception e) {
            e.printStackTrace();
        }finally {            
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
