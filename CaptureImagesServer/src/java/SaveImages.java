

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
            byte[] b = new byte[4096];
            int current = 0;
            while ((current = bis.read(b)) != -1) {
                    fos.write(b, 0, current);
            }
            bis.close();
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
            final File file2Copy = file;
            new Thread(){
                public void run(){
                    try{
                        copyFile(file2Copy);
                    } catch(Exception e){
                        e.printStackTrace();
                    }
                }
            };
            
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
    
    
    private static void copyFile(File sourceFile)
		throws IOException {
	if (!sourceFile.exists()) {
		return;
	}
        File destFile = new File(File.separator+"slideshow", sourceFile.getName());
	if (!destFile.exists()) {
                destFile.mkdirs();
		destFile.createNewFile();
	}
	FileChannel source = null;
	FileChannel destination = null;
	source = new FileInputStream(sourceFile).getChannel();
	destination = new FileOutputStream(destFile).getChannel();
	if (destination != null && source != null) {
		destination.transferFrom(source, 0, source.size());
	}
	if (source != null) {
		source.close();
	}
	if (destination != null) {
		destination.close();
	}

}
}
