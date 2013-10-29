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

@WebServlet(urlPatterns = {"/setStart"})
public class SetStart extends HttpServlet {

    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String phnNo=request.getParameter("phnNo");
            System.out.println("SELECT poll_id, start FROM polls WHERE active = 1 AND phnNo LIKE '%"+phnNo+"%'");
            ResultSet livePollRS = ManageConnection.getDBConnection().createStatement().executeQuery("SELECT poll_id, start FROM polls WHERE active = 1 AND phnNo LIKE '%"+phnNo+"%'");
            int start = 0;
            int pollId = 0;
            while (livePollRS.next()) {
                start=livePollRS.getInt(2);
                pollId = livePollRS.getInt(1);
            }
            System.out.println(start+"...."+pollId);
            if(start == 1){
                ManageConnection.getDBConnection().createStatement().executeUpdate("UPDATE polls SET start = 0 WHERE poll_id = "+pollId);
            } else{
                ManageConnection.getDBConnection().createStatement().executeUpdate("UPDATE polls SET start = 1 WHERE poll_id = "+pollId);
            }
            response.sendRedirect(request.getContextPath() + "/index.html?phnNo="+phnNo);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
