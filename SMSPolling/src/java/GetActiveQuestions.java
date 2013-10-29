
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.json.JSONObject;
import utility.ManageConnection;

@WebServlet(urlPatterns = {"/GetActiveQuestions"})
public class GetActiveQuestions extends HttpServlet {

     protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String phnNo = request.getParameter("phnNo");
            ResultSet livePollRS = ManageConnection.getDBConnection().createStatement().executeQuery("SELECT poll_id,question FROM polls WHERE active = 0 AND done = 0 AND phnNo='"+phnNo+"'");
            JSONObject completeDataObj = null;
            JSONArray dataArr = new JSONArray();
            while(livePollRS.next()){
               completeDataObj = new JSONObject();
               completeDataObj.put("id", livePollRS.getInt(1));
                completeDataObj.put("question", livePollRS.getString(2));
                dataArr.put(completeDataObj);
            }
            response.setContentType("application/json");
            response.getWriter().write(dataArr.toString(), 0, dataArr.toString().length());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
