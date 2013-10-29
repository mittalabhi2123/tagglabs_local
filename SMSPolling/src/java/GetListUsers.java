
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.HashMap;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import utility.ManageConnection;


@WebServlet(urlPatterns = {"/getReport1"})
public class GetListUsers extends HttpServlet {

     public GetListUsers() {
        super();
    }
     
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            Connection conn = ManageConnection.getDBConnection();
            HashMap<String,String> userMap = new HashMap<String, String>();
            ResultSet rs0 = conn.createStatement().executeQuery("SELECT phone_no, first_name FROM pandalUsers");
            while(rs0.next())
                userMap.put(rs0.getString(1), rs0.getString(2));
            ResultSet rs = conn.createStatement().executeQuery("SELECT poll_id, question, ans FROM polls");
            ResultSet rs1 = null;
            response.setContentType("text/html");
            PrintWriter out = response.getWriter();
            out.println("<html>");
            out.println("<head>");
            out.println("<title>User's List</title>");
            out.println("</head>");
            out.println("<body>");
            while(rs.next()){
                int id = rs.getInt(1);
                String question = rs.getString(2);
                System.out.println(question);
                String ans = rs.getString(3);
                String venueId = id+":"+ans;
                out.println("<h1>"+question+"</h1>");
                out.println("<table>");
                rs1 = conn.createStatement().executeQuery("SELECT phnRfidNo FROM uservotes WHERE mode='POLL' AND venueId='" + venueId + "'");
                while(rs1.next()){
                    System.out.println("data found");
                    String phnNo = rs1.getString(1);
                    if(!userMap.containsKey(phnNo))
                        userMap.put(phnNo,"");
                    out.println("<tr><td>"+phnNo+"</td><td>"+userMap.get(phnNo)+"</td></tr>");
                }
                out.println("</table>");
            }
            out.println("</body>");
            out.println("</html>");
            out.flush();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
