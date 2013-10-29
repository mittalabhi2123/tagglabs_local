package utility;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ManageConnection {

	private static Connection conn = null;
	static {
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/tagglabs","root","root");
	    } catch (Exception f) {
	       throw new RuntimeException(f);
	    }  
	}
	
	public static Connection getDBConnection() throws SQLException{
		if(conn.isClosed()){
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/tagglabs","root","root");
		}
		return conn;     
	}
	
	@Override
	public void finalize (){//need to check how to do this at class unloading...
	    try{
	      if(conn!=null && !conn.isClosed())
	       conn.close();
	    }catch(Exception e){
	    	throw new RuntimeException(e);
	    }
	  }
	
}
