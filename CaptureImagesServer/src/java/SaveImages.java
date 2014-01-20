

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.channels.FileChannel;
import java.sql.PreparedStatement;
import java.util.Random;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author abhishekmitt
 */
@WebServlet(urlPatterns = {"/saveImages"})
public class SaveImages extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        System.out.println("Request Received");
        try {
            String uid = request.getHeader("id");
            String zone = request.getHeader("zone");
            System.out.println(uid+"...."+zone);
            Random rand = new Random();
            
            File file1  = new File(File.separator+"data"+File.separator+zone+File.separator+uid);
            file1.mkdirs();
            File file = new File(file1, (uid+"_"+rand.nextInt(99999))+".jpg");
            if (file.exists())
                file  = new File(file1, (uid+"_"+rand.nextInt(99999))+".jpg");
            BufferedInputStream bis = new BufferedInputStream(request.getInputStream());
            FileOutputStream fos = new FileOutputStream(file, true);
            FileOutputStream fos2 = new FileOutputStream(new File(File.separator+"slideshow",file.getName()), true);
            byte[] b = new byte[4096];
            int current = 0;
            while ((current = bis.read(b)) != -1) {
                    fos.write(b, 0, current);
                    fos2.write(b,0,current);
            }
            bis.close();
            fos2.close();
            fos.close();
            response.setStatus(200);
            PreparedStatement prep = Utility.getConnection().prepareStatement("INSERT INTO files VALUES (?,?,?,?,?,?,?)");
            prep.setString(1, file.getName());
            prep.setString(2, file.getAbsolutePath());
            prep.setString(3, uid);
            prep.setString(4, zone);
            prep.setInt(5, 0);
            prep.setLong(6, file.length());
            prep.setInt(7,0);
            prep.executeUpdate();
            
        } catch(Exception e){
            e.printStackTrace();
        }
        finally {
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }
}
