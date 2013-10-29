/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.IOException;
import java.sql.ResultSet;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import utility.ManageConnection;

@WebServlet(urlPatterns = {"/StartAll"})
public class StartAll extends HttpServlet {

    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
          ManageConnection.getDBConnection().createStatement().executeUpdate("UPDATE polls SET start = 1");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
