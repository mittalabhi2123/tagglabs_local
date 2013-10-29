/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.Enumeration;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONObject;
import utility.ManageConnection;

/**
 *
 * @author abhishekmitt
 */
@WebServlet(urlPatterns = {"/UpdatePoll1"})
public class UpdatePoll1 extends HttpServlet {

     public UpdatePoll1() {
        super();
    }
     
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            Connection conn = ManageConnection.getDBConnection();
            String SmsSid = request.getParameter("SmsSid");
            String From = request.getParameter("From");
            String To = request.getParameter("To");
            String body = request.getParameter("Body");
            response.setContentType("text/plain");
            response.setContentLength(130);
            Enumeration<String> headerName=request.getHeaderNames();
            while(headerName.hasMoreElements()){
                String header = headerName.nextElement();
                response.setHeader(header, request.getHeader(header));
            }
            ResultSet livePollRS = conn.createStatement().executeQuery("SELECT poll_id FROM polls WHERE active = 1");
            boolean exists = livePollRS.next();
            if (!exists) {
                return;
            }
            int pollId = livePollRS.getInt(1);
            String phnNoSmall = From.substring(2);
                System.out.println(To + "..." + From + "..." + body);
                ResultSet rs1 = conn.createStatement().executeQuery("SELECT * FROM userVotes WHERE phnRfidNo='"+From+"' AND venueId LIKE '"+pollId+":%'");
                while (rs1.next()){
                    JSONObject data = new JSONObject();
                    data.put("result", "error");
                    data.put("reason", "Vote already casted");
                    response.getWriter().write(data.toString(), 0, data.toString().length());
                    System.out.println("Vote already casted");
                    response.getOutputStream().write("Vote already casted".getBytes());
                    return;
                }
                conn.createStatement().executeUpdate("INSERT INTO userVotes VALUES ('"+From+"','"+(pollId+":"+body)+"','POLL')");
                conn.createStatement().executeUpdate("UPDATE polls SET option2_count = option2_count + 1 WHERE poll_id = " + pollId);
                response.getOutputStream().write("Hello. Thanks for your reply".getBytes());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
