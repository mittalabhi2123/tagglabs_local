
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import utility.ManageConnection;

@WebServlet(urlPatterns = {"/saveData"})
public class SaveData extends HttpServlet {

    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            ResultSet maxId = ManageConnection.getDBConnection().createStatement().executeQuery("SELECT MAX(poll_id) FROM polls");
            int pollId = 1;
            boolean exists = maxId.next();
            if (!exists) {
                return;
            }
            pollId = maxId.getInt(1) + 1;
            PreparedStatement prep = ManageConnection.getDBConnection().prepareStatement("INSERT INTO polls VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?, ?, ?)");
            prep.setInt(1, pollId);
            prep.setString(2, request.getParameter("question"));
            prep.setString(3, request.getParameter("op1"));
            prep.setString(4, request.getParameter("op2"));
            prep.setString(5, request.getParameter("op3"));
            prep.setString(6, request.getParameter("op4"));
            prep.setInt(7, 0);
            prep.setInt(8, 0);
            prep.setInt(9, 0);
            prep.setInt(10, 0);
            prep.setInt(11, 0);
            prep.setInt(12, 0);
            prep.setInt(13, 1);
            prep.setInt(14, 0);
            prep.setString(15, request.getParameter("answer"));
            prep.setString(16, request.getParameter("phnNo"));
            prep.setInt(17, 0);
            prep.executeUpdate();
            response.sendRedirect(request.getContextPath() + "/enterQuestion.html");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
