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
import utility.ManageConnection;

/**
 *
 * @author abhishekmitt
 */
@WebServlet(urlPatterns = {"/UpdatePoll3"})
public class UpdatePoll3 extends HttpServlet {

    public UpdatePoll3() {
        super();
    }
    
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            ResultSet livePollRS = ManageConnection.getDBConnection().createStatement().executeQuery("SELECT poll_id FROM polls WHERE active = 1");
            boolean exists = livePollRS.next();
            if (!exists) {
                return;
            }
            int pollId = livePollRS.getInt(1);
            ManageConnection.getDBConnection().createStatement().executeUpdate("UPDATE polls SET option3_count = (option3_count + 1), existsNewData = 1 WHERE poll_id = " + pollId);
            
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
