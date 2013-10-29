/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import utility.ManageConnection;

@WebServlet(urlPatterns = {"/setActive"})
public class SetActive extends HttpServlet {

    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            if (request.getParameter("type").equalsIgnoreCase("active")){
                ManageConnection.getDBConnection().createStatement().executeUpdate("UPDATE polls SET active = 0, done = 1 WHERE active = 1");
                ManageConnection.getDBConnection().createStatement().executeUpdate("UPDATE polls SET active = 1 WHERE poll_id = " + Integer.parseInt(request.getParameter("pollId")));
            } else{
                ManageConnection.getDBConnection().createStatement().executeUpdate("UPDATE polls SET active = 0, done = 1 WHERE poll_id = " + Integer.parseInt(request.getParameter("pollId")));
            }
            response.sendRedirect(request.getContextPath() + "/enterQuestion.html");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
