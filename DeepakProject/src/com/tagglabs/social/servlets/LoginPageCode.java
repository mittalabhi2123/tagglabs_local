package com.tagglabs.social.servlets;

import com.tagglabs.social.utils.Utility;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

@ManagedBean(name = "loginPageCode")
@SessionScoped
public class LoginPageCode implements Serializable {

    private boolean dataExists = false;

    private String phoneNo = "+91";
    private String city="";

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getFacebookUrlAuth() {;
        HttpSession session =
                (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        String sessionId = session.getId();
        String returnValue = "https://www.facebook.com/dialog/oauth?client_id="
                + Utility.FB_APP_ID + "&redirect_uri=" + Utility.FB_REDIRECT_URL
                + "&scope=email,user_birthday,read_stream,user_about_me,photo_upload,rsvp_event&state=" + sessionId;
        session.setAttribute("firstName", Utility.FB_APP_ID);
        System.out.println(returnValue);
        return returnValue;
    }

    public String getMessage() {
        HttpSession session =
                (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        return (session.getAttribute("message") == null ? "" : (String) session.getAttribute("message"));
    }

    public String getErrorMessage() {
        HttpSession session =
                (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        return (session.getAttribute("error") == null ? "" : (String) session.getAttribute("error"));
    }

    public void loadData(AjaxBehaviorEvent event) {
        System.out.println("loadData*************************");
        phoneNo = getPhoneNo();
        try {
            if (phoneNo == null || phoneNo.equals("")) {
                dataExists = false;
                ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
                ec.redirect(((HttpServletRequest) ec.getRequest()).getRequestURI());
                return;
            }
            Connection conn = Utility.getConnection();
            Statement checkUser = conn.createStatement();
            ResultSet rs = checkUser.executeQuery("SELECT * FROM users WHERE phone_no = '" + phoneNo + "'");
            int userId = 0;
            while (rs.next()) {//TODO user already exists
                dataExists = true;
                userId = rs.getInt(1);
                ((HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false)).setAttribute("userId", userId);
            }
            if (userId == 0) {
                dataExists = false;
            }
            ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
            ec.redirect(((HttpServletRequest) ec.getRequest()).getRequestURI());
            return;
        } catch (Exception e) {
            e.printStackTrace();
            showError(e.getLocalizedMessage());
        }
    }

    public String submit() {
        try {
            String accessToken = (String)((HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false)).getAttribute("TOKEN");
            Connection conn = Utility.getConnection();
            Statement checkUser = conn.createStatement();
            if (!dataExists) {
                ResultSet rs = checkUser.executeQuery("SELECT * FROM users WHERE phone_no = '" + phoneNo + "'");
                while (rs.next()) {//TODO user already exists
                    showError("User already exists!");
                    return null;
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
                createUser.setString(6, accessToken);
                createUser.setString(7, "");
                createUser.setString(8, "");
                createUser.setString(9, city);
                createUser.setString(10, "");
                createUser.setString(11, "");
                createUser.setString(12, "");
                createUser.setString(13, "");
                createUser.setTimestamp(14, new Timestamp(System.currentTimeMillis()));
                createUser.setString(15, "");
                createUser.executeUpdate();
                ((HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false)).setAttribute("userId", userId);
                ((HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false)).setAttribute("city", city);
                checkInUser(accessToken, city);
            } else {
                int userId = Integer.parseInt(((HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false)).getAttribute("userId").toString());
                PreparedStatement createUser = conn.prepareStatement("UPDATE users SET fb_auth_token=?, phone_no=?, city=? WHERE user_id = ?");
                createUser.setString(1, accessToken);
                createUser.setString(2, String.valueOf(phoneNo));
                createUser.setString(3, city);
                createUser.setInt(4, userId);
                createUser.executeUpdate();
                ((HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false)).setAttribute("city", city);
            }
        } catch (Exception e) {
            e.printStackTrace();
            showError(e.getLocalizedMessage());
            return null;
        }
        return "congrats";
    }

    public String done() {
        return "congrats";
    }
    
    public String congratsBack() {
        return "back";
    }
    
    protected static void showError(String pClientID, String pSummary, String pDetail) {
        FacesMessage facesMessage = new FacesMessage(pSummary, (pDetail == null) ? pSummary : pDetail);
        FacesContext.getCurrentInstance().addMessage(pClientID, facesMessage);
    }

    protected static void showError(String pSummary) {
        showError("loginForm", pSummary, null);

    }
    
    
    private void checkInUser(String accessToken, String city) {
        try{
        ResultSet rs = Utility.getConnection().createStatement().executeQuery("SELECT placeId, eventId, placeCaption FROM day_college WHERE city = '" + city + "' LIMIT 1");
        while (rs.next()) {
            String placePageId = rs.getString(1);
            String eventId = rs.getString(2);
            String url = "https://graph.facebook.com/me/feed";
            String url2 = "https://graph.facebook.com/" + eventId + "/attending";
            HttpClient client = new DefaultHttpClient();
            HttpClient client2 = new DefaultHttpClient();
            HttpPost post = new HttpPost(url);
            HttpPost post2 = new HttpPost(url2);
            List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
            List<NameValuePair> urlParameters2 = new ArrayList<NameValuePair>();
            urlParameters.add(new BasicNameValuePair("message", rs.getString(3)));
            urlParameters.add(new BasicNameValuePair("place", placePageId));
            urlParameters2.add(new BasicNameValuePair("access_token", accessToken));
            urlParameters.addAll(urlParameters2);
            post.setEntity(new UrlEncodedFormEntity(urlParameters));
            post2.setEntity(new UrlEncodedFormEntity(urlParameters2));
            HttpResponse responseFb = client.execute(post);
            HttpResponse responseFb2 = client2.execute(post2);
            BufferedReader rd = new BufferedReader(
                    new InputStreamReader(responseFb.getEntity().getContent()));

            StringBuffer result = new StringBuffer();
            String line = "";
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }
            System.out.println("result.............." + result);
        }
        } catch(Exception e){
            e.printStackTrace();
            return;
        }
    }
}