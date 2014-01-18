import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Timer;

public class Utility {
    static Connection conn;
    static Timer timer;
    
    public static final String FB_REDIRECT_URL = "http://54.200.86.247:8084/Test/index.sec";
    public static final String FB_APP_ID = "1397704090459265";
    public static final String FB_APP_SECRET = "0603859bfaca6ce5aa092a47d1f291d5";
    
    public static Connection getConnection() throws Exception{
        if (conn == null || conn.isClosed() || !conn.isValid(10)){
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/autoexpo", "root", "root");    
        }
        return conn;
    }
    
    public static Timer getTimer() {
        if (timer != null)
            timer.cancel();
        return new Timer();
    }
}
