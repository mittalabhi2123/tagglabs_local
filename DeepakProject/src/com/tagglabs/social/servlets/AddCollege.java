package com.tagglabs.social.servlets;

import com.tagglabs.social.utils.Utility;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Arrays;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

import org.apache.commons.lang.StringUtils;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

@WebServlet("/addCollege")
public class AddCollege extends HttpServlet {

    private static final long serialVersionUID = 8071426090770097330L;

    public AddCollege() {
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        System.out.println("*** Called Add College");
        String city = request.getParameter("city");
        String college = request.getParameter("college");
        try {
            Connection conn = Utility.getConnection();
            PreparedStatement insert = conn.prepareStatement("INSERT INTO day_college VALUES (?,?,?)");
            insert.setString(1,city);
            insert.setString(2,college);
            insert.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
            insert.executeQuery();
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
    }
}
