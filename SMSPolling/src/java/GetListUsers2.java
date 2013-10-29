
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import utility.ManageConnection;


@WebServlet(urlPatterns = {"/getReport2"})
public class GetListUsers2 extends HttpServlet {

     public GetListUsers2() {
        super();
    }
     
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            Connection conn = ManageConnection.getDBConnection();
            HashMap<String,String> userMap = new HashMap<String, String>();
            ResultSet rs0 = conn.createStatement().executeQuery("SELECT phone_no, first_name FROM pandalUsers");
            while(rs0.next())
                userMap.put(rs0.getString(1), rs0.getString(2));
            HashMap<String,String> ansMap = new HashMap<String, String>();
            ResultSet rs00 = conn.createStatement().executeQuery("SELECT poll_id, ans FROM polls");
            while(rs0.next())
                ansMap.put(rs00.getString(1), rs00.getString(2));
            System.out.println("UserMap & PollMap Size::" + userMap.size() + "...." + ansMap.size());
            ResultSet rs = conn.createStatement().executeQuery("SELECT phnRfidNo, venueId FROM userVotes WHERE venueId IN (SELECT CONCAT(poll_id,':',ans) FROM polls) ORDER BY venueId");
            HashMap<String,Integer> userAnsMap = new HashMap<String, Integer>();
            HashMap<String,Double> userWeightMap = new HashMap<String, Double>();
            String venueId = "";
            int count=1;
            while(rs.next()){
                String userId = rs.getString(1);
                String ans = rs.getString(2);
                if(!ans.equalsIgnoreCase(venueId)){
                    count=1;
                    venueId=ans;
                }else{
                    count++;
                }
                if(userAnsMap.containsKey(userId))
                    userAnsMap.put(userId, userAnsMap.get(userId)+1);
                else 
                    userAnsMap.put(userId, 1);
                
                if(userWeightMap.containsKey(userId))
                    userWeightMap.put(userId, userWeightMap.get(userId)+(1/count));
                else 
                    userWeightMap.put(userId, (double)(1/count));
            }
            SortedMap<Double,List<String>> finalMap = new TreeMap<Double,List<String>>(Collections.reverseOrder());
            for(String userId:userAnsMap.keySet()){
                double finalScore = userAnsMap.get(userId)+userWeightMap.get(userId);
                if(!finalMap.containsKey(finalScore))
                    finalMap.put(finalScore, new ArrayList<String>());
                finalMap.get(finalScore).add(userId);
            }
            response.setContentType("text/html");
            PrintWriter out = response.getWriter();
            out.println("<html>");
            out.println("<head>");
            out.println("<title>User's List</title>");
            out.println("</head>");
            out.println("<body>");
                out.println("<table>");
            for(Double score:finalMap.keySet()){
                List<String> userIds = finalMap.get(score);
                for(String userId:userIds){
                    if(!userMap.containsKey(userId))
                        userMap.put(userId,"");
                    out.println("<tr><td>"+userId+"</td><td>"+userMap.get(userId)+"</td></tr>");
                }
            }
            out.println("</table>");
            out.println("</body>");
            out.println("</html>");
            out.flush();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
