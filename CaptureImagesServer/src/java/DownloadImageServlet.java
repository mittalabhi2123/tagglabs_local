import java.io.*;
import java.sql.ResultSet;

import java.util.*;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet(urlPatterns = {"/fetchImages"})
public class DownloadImageServlet extends HttpServlet {

    public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }

    public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RandomAccessFile raf1=null;
        try {
            String uid = request.getHeader("id");
            String zone = request.getHeader("zone");
            ResultSet rs = Utility.getConnection().createStatement().executeQuery("SELECT * FROM files WHERE zone = '"+zone+"'");
            
            int filesPendingNum = 0;
            File file = null;
            while(rs.next()){
                filesPendingNum++;
                if (file != null)
                    continue;
                file = new File(rs.getString(2));
            }
            raf1 = new RandomAccessFile(file, "r"); 
            raf1.seek(0);
            byte b1[] = new byte[(int)raf1.length()];
            raf1.readFully(b1);
                --filesPendingNum;
            response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition", "attachment;filename=" + file.getName());
            response.addHeader("fileName", file.getName());
            response.addIntHeader("filesLeft",(filesPendingNum > 0) ? 1 : 0);
            response.addIntHeader("fileSize", b1.length);
            response.getOutputStream().write(b1);
            response.getOutputStream().flush();
            response.getOutputStream().close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally{
            if(raf1 != null )
                raf1.close();
            
        }

    }

    public DownloadImageServlet() {
    }
}