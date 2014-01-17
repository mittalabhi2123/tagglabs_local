import java.sql.Connection;
import java.sql.DriverManager;

public class Utility {
    static Connection conn;
    public static Connection getConnection() throws Exception{
        if (conn == null || conn.isClosed() || !conn.isValid(10)){
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/images", "root", "root");    
        }
        return conn;
    }
}
