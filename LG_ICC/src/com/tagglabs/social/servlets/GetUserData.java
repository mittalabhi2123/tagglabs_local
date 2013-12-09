
package com.tagglabs.social.servlets;

import com.tagglabs.social.utils.Utility;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.json.JSONSerializer;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.util.JSONTokener;

@WebServlet(name = "GetUserData", urlPatterns = {"/getUserData"})
public class GetUserData extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String phnNo = request.getParameter("phone");
        HttpClient client = null;
        try {
            client = new DefaultHttpClient();
            ResultSet rs = Utility.getConnection().createStatement().executeQuery("SELECT a.city, a.fb_auth_token, b.fb_photo_id FROM users a, user_fb_photo b where b.phone_no LIKE '%"+ phnNo +"%' AND a.phone_no LIKE '%"+phnNo+"%'");
            while(rs.next()){
                System.out.println("Got Data");
                for(File file : new File("C:\\tmp").listFiles()){
                    System.out.println("FileEncountered::"+file.getName());
                    if(file.getName().split("[.]")[0].indexOf(phnNo) > -1) {
                        uploadFile(response, file);
                    }
                }
                    
            }
            client.getConnectionManager().shutdown();
        } catch (Exception e){
            throw new RuntimeException(e);
        }finally {        
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
    
    private void uploadFile(HttpServletResponse response, File file) throws Exception {
        int length   = 0;
        ServletOutputStream outStream = response.getOutputStream();
        ServletContext context  = getServletConfig().getServletContext();
        String mimetype = context.getMimeType(file.getName());
        
        // sets response content type
        if (mimetype == null) {
            mimetype = "application/octet-stream";
        }
        response.setContentType(mimetype);
        response.setContentLength((int)file.length());
        
        // sets HTTP header
        response.setHeader("Content-Disposition", "attachment; filename=\"" + file.getName() + "\"");
        
        byte[] byteBuffer = new byte[4096];
        DataInputStream in = new DataInputStream(new FileInputStream(file));
        
        // reads the file's bytes and writes them to the response stream
        while ((in != null) && ((length = in.read(byteBuffer)) != -1))
        {
            outStream.write(byteBuffer,0,length);
        }
        
        in.close();
        outStream.close();
    }
}
