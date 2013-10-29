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
@WebServlet(urlPatterns = {"/smsvote"})
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
            System.out.println(To + "..." + From + "..." + body);
            response.setContentType("text/plain");
            response.setContentLength(130);
            Enumeration<String> headerName=request.getHeaderNames();
            while(headerName.hasMoreElements()){
                String header = headerName.nextElement();
                response.setHeader(header, request.getHeader(header));
            }
            if(body == null)
                return;
            if (!body.equalsIgnoreCase("A") && !body.equalsIgnoreCase("B")&& !body.equalsIgnoreCase("C") && !body.equalsIgnoreCase("D")){
                response.getOutputStream().write("Invalid input".getBytes());
                System.out.println(body+"...."+body.length());
                return;
            }
            String query="SELECT poll_id, start FROM polls WHERE active = 1 AND phnNo LIKE '%"+To.substring(1)+"%'";
            System.out.println(query);
            ResultSet livePollRS = conn.createStatement().executeQuery(query);
            boolean exists = livePollRS.next();
            if (!exists) {
                return;
            }
            System.out.println(exists +"...."+livePollRS.getInt(2) +"..."+livePollRS.getInt(1));
            if(livePollRS.getInt(2) != 1){
                response.getOutputStream().write("Voting for this question is not open.".getBytes());
                return;
            }
            int pollId = livePollRS.getInt(1);
            ResultSet rs1 = conn.createStatement().executeQuery("SELECT * FROM userVotes WHERE phnRfidNo='"+From+"' AND venueId LIKE '"+pollId+":%'");
            while (rs1.next()){
                System.out.println("Vote already casted");
                response.getOutputStream().write("Vote already casted".getBytes());
                return;
            }
            conn.createStatement().executeUpdate("INSERT INTO userVotes VALUES ('"+From+"','"+(pollId+":"+body)+"','POLL')");
            if (body.equalsIgnoreCase("A"))
                conn.createStatement().executeUpdate("UPDATE polls SET option1_count = option1_count + 1 WHERE poll_id = " + pollId);
            else if (body.equalsIgnoreCase("B"))
                conn.createStatement().executeUpdate("UPDATE polls SET option2_count = option2_count + 1 WHERE poll_id = " + pollId);
            else if (body.equalsIgnoreCase("C"))
                conn.createStatement().executeUpdate("UPDATE polls SET option3_count = option3_count + 1 WHERE poll_id = " + pollId);
            else if (body.equalsIgnoreCase("D"))
                conn.createStatement().executeUpdate("UPDATE polls SET option4_count = option4_count + 1 WHERE poll_id = " + pollId);
            response.getOutputStream().write("Hi! Thanks for your response".getBytes());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
