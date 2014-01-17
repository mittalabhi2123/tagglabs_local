/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@WebServlet(urlPatterns = {"/ackImage"})
public class AcknowledgeFileReceived extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String uid = request.getHeader("id");
            String zone = request.getHeader("zone");
            String fileName = request.getHeader("fileName");
            Utility.getConnection().createStatement().executeUpdate("UPDATE files SET fetched = 1 WHERE zone = '"+zone+"' AND id = '"+uid+"' AND filename = '"+fileName+"'");
        } catch (Exception e) {
            e.printStackTrace();
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
