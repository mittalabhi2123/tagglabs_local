
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
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
@WebServlet(urlPatterns = {"/userReg"})
public class SmsUserRegister extends HttpServlet {

     public SmsUserRegister() {
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
            System.out.println(To + "..." + From + "..." + body);
            Enumeration<String> headerName=request.getHeaderNames();
            while(headerName.hasMoreElements()){
                String header = headerName.nextElement();
                response.setHeader(header, request.getHeader(header));
            }
            if(body == null || !body.toUpperCase().startsWith("REG ")){
                response.getOutputStream().write("Invalid keyword used.!".getBytes());
                return;
            }
            Statement checkUser = conn.createStatement();
                ResultSet rs = checkUser.executeQuery("SELECT * FROM users WHERE phone_no = '" + From + "'");
                while (rs.next()) {//TODO user already exists
                    response.getOutputStream().write("User already exists!".getBytes());
                    return;
                }
                Statement getMaxUserId = conn.createStatement();
                ResultSet rs1 = getMaxUserId.executeQuery("SELECT MAX(user_id) FROM users");
                int userId = 1;
                while (rs1.next()) {
                    userId = rs1.getInt(1) + 1;
                }
                PreparedStatement createUser = conn.prepareStatement("INSERT INTO users VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
                createUser.setInt(1, userId);
                createUser.setString(2, body.substring(4));
                createUser.setString(3, "SMS_REGISTER");
                createUser.setInt(4, 0);
                createUser.setString(5, String.valueOf(From));
                createUser.setString(6, "");
                createUser.setString(7, "");
                createUser.setString(8, "");
                createUser.setString(9, "");
                createUser.setString(10, "");
                createUser.setString(11, "");
                createUser.executeUpdate();
            response.getOutputStream().write("Hello. Thanks for registering with us!".getBytes());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
