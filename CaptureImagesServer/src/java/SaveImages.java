

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
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
        try {
//            String uid = request.getHeader("id");
//            String zone = request.getHeader("zone");
            
            
            String uid = request.getPart("id").getName();
            String zone = request.getPart("zone").getName();
            
            Random rand = new Random();
            
            File file  = new File("data"+File.separator+uid+File.separator+(uid+"_"+zone+"_"+rand.nextInt(999))+".jpg");
            if (file.exists())
                file  = new File("data"+File.separator+uid+File.separator+(uid+"_"+zone+"_"+rand.nextInt(999))+".jpg");
            file.mkdirs();
            BufferedInputStream bis = new BufferedInputStream(request.getPart("file").getInputStream());
//            BufferedInputStream bis = new BufferedInputStream(is);
            FileOutputStream fos = new FileOutputStream(file, true);
            byte[] b = new byte[4096];
            int current = 0;
            while ((current = bis.read(b)) != -1) {
                    fos.write(b, 0, current);
            }
            bis.close();
            fos.close();
            response.setStatus(200);
        } finally {
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
