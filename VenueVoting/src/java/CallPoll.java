

import java.io.IOException;
import java.sql.ResultSet;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.json.JSONObject;
import utility.ManageConnection;

@WebServlet(urlPatterns = {"/CallPoll"})
public class CallPoll extends HttpServlet {


    public CallPoll() {
        super();
    }

    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            ResultSet livePollRS = ManageConnection.getDBConnection().createStatement().executeQuery("SELECT * FROM polls WHERE active = 1");
            boolean exists = livePollRS.next();
            if (!exists) {
                return;
            }
//            if(livePollRS.getInt("existsNewData") == 0)
//                return;
            JSONObject completeDataObj = new JSONObject();
            JSONArray optionJsonArr = new JSONArray();
            JSONArray optionCountJsonArr = new JSONArray();
            completeDataObj.put("question", livePollRS.getString(2));
            optionJsonArr.put(livePollRS.getString(3));
            optionJsonArr.put(livePollRS.getString(4));
            optionJsonArr.put(livePollRS.getString(5));
            optionJsonArr.put(livePollRS.getString(6));
            optionCountJsonArr.put(livePollRS.getInt(7));
            optionCountJsonArr.put(livePollRS.getInt(8));
            optionCountJsonArr.put(livePollRS.getInt(9));
            optionCountJsonArr.put(livePollRS.getInt(10));
            
            completeDataObj.put("options", optionJsonArr);
            completeDataObj.put("optionsCount", optionCountJsonArr);
            response.setContentType("application/json");
            response.getWriter().write(completeDataObj.toString(), 0, completeDataObj.toString().length());
            ManageConnection.getDBConnection().createStatement().executeUpdate("UPDATE polls SET existsNewData = 0 WHERE poll_id = " + livePollRS.getInt(1));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
