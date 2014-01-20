/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.File;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONArray;

/**
 *
 * @author abhishekmitt
 */
@WebServlet(urlPatterns = {"/GetImageNames"})
public class GetImageNames extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            File file = new File(File.separator+"slideshow");
            JSONArray optionJsonArr = new JSONArray();
            for (File f:file.listFiles())
                optionJsonArr.put(f.getName());
            response.setContentType("application/json");
            response.getWriter().write(optionJsonArr.toString(), 0, optionJsonArr.toString().length());
        } catch (Exception e) {
            throw new RuntimeException(e);
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
