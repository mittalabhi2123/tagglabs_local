
package com.tagglabs.social.servlets.hike2;

import com.tagglabs.social.utils.Utility;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONObject;

@WebServlet(name = "Verify", urlPatterns = {"/verify"})
public class Verify extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        String phoneNo = request.getParameter("phoneNo");
        if (phoneNo == null) {
            phoneNo = "";
        }
        File file = new File(Utility.filePath);
        RandomAccessFile raf = null;
        try {
            raf = new RandomAccessFile(file, "rw");
            raf.seek(0);
            while (raf.getFilePointer() < raf.length()) {
                String phone = raf.readLine();
                if (phone == null || phone.trim().isEmpty()) {
                    continue;
                }
                if (phone.indexOf(phoneNo) > -1 || phoneNo.indexOf(phone) > -1) {
                    JSONObject jobj = new JSONObject();
                    String result = submit(phoneNo, request);
                    jobj.put("response", "PRIMARY");
                    jobj.put("error", result);
                    response.getWriter().write(jobj.toString(), 0, jobj.toString().length());
                    return;
                }
            }
            if (isFriend(phoneNo)) {
                JSONObject jobj = new JSONObject();
                jobj.put("response", "FRIEND");
                response.getWriter().write(jobj.toString(), 0, jobj.toString().length());
                return;
            }
            JSONObject jobj = new JSONObject();
            jobj.put("response", "INVALID");
            response.getWriter().write(jobj.toString(), 0, jobj.toString().length());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try{
                if (raf != null)
                    raf.close();
            } catch(Exception e){
                e.printStackTrace();
            }
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

    public String submit(String phoneNo, HttpServletRequest request) {
        try {
            Connection conn = Utility.getConnection();
            Statement checkUser = conn.createStatement();
            ResultSet rs = checkUser.executeQuery("SELECT * FROM users WHERE phone_no LIKE '%" + phoneNo + "%'");
            while (rs.next()) {//TODO user already exists
                request.getSession().setAttribute("userId", rs.getString(1));
                return "";
            }
            Statement getMaxUserId = conn.createStatement();
            ResultSet rs1 = getMaxUserId.executeQuery("SELECT MAX(user_id) FROM users");
            int userId = 1;
            while (rs1.next()) {
                userId = rs1.getInt(1) + 1;
            }
            PreparedStatement createUser = conn.prepareStatement("INSERT INTO users VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
            createUser.setInt(1, userId);
            createUser.setString(2, "");
            createUser.setString(3, "");
            createUser.setInt(4, 0);
            createUser.setString(5, String.valueOf(phoneNo));
            createUser.setString(6, (String)request.getSession().getAttribute("TOKEN"));
            createUser.setString(7, "");
            createUser.setString(8, "");
            createUser.setString(9, "");
            createUser.setString(10, "");
            createUser.setString(11, "");
            createUser.setString(12, "");
            createUser.setString(13, "");
            createUser.setTimestamp(14, new Timestamp(System.currentTimeMillis()));
            createUser.setString(15, "");
            createUser.executeUpdate();
            request.getSession().setAttribute("userId", userId);
        } catch (Exception e) {
            e.printStackTrace();
            return e.getLocalizedMessage();
        }
        return "";
    }
    
    public boolean isFriend(String phoneNo) {
        try {
            Connection conn = Utility.getConnection();
            Statement checkUser = conn.createStatement();
            ResultSet rs = checkUser.executeQuery("SELECT * FROM users WHERE last_name LIKE '%" + phoneNo + "%'");
            while (rs.next()) {//TODO user already exists
                return true;
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
