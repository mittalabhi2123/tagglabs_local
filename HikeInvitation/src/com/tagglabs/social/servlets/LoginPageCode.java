package com.tagglabs.social.servlets;

import com.tagglabs.social.utils.Utility;
import java.io.File;
import java.io.RandomAccessFile;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.primefaces.event.SelectEvent;

@ManagedBean(name = "loginPageCode")
@SessionScoped
public class LoginPageCode implements Serializable {

    private boolean dataExists = false;
    private boolean isFriend = false;
    private String phoneNo = "+91";
    private String phoneNo2 = "+91";
    private String friend = "";

    public String getFriend() {
        return friend;
    }

    public void setFriend(String friend) {
        this.friend = friend;
    }
    private String city="";
    private List<String> phoneNoLst = new ArrayList<String>();
        SortedMap<String, String> nameMap = new TreeMap<String, String>();
    static {
        try{
            //TODO load list for validation
        } catch(Exception e){
            
        }
    }
    
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

    public String getPhoneNo2() {
        return phoneNo2;
    }

    public void setPhoneNo2(String phoneNo2) {
        this.phoneNo2 = phoneNo2;
    }

    public String getFacebookUrlAuth() {;
        HttpSession session =
                (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        String sessionId = session.getId();
        String returnValue = "https://www.facebook.com/dialog/oauth?client_id="
                + Utility.FB_APP_ID + "&redirect_uri=" + Utility.FB_REDIRECT_URL
                + "&scope=email,user_birthday,read_stream,user_about_me,photo_upload,rsvp_event&state=" + sessionId;
        session.setAttribute("firstName", Utility.FB_APP_ID);
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
    
    public String submit() {
        try {
            String accessToken = (String)((HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false)).getAttribute("TOKEN");
            String twtToken = (String)((HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false)).getAttribute("TWT_TOKEN");
            String twtSecret = (String)((HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false)).getAttribute("TWT_SECRET");
            Connection conn = Utility.getConnection();
            Statement checkUser = conn.createStatement();
            if (!dataExists) {
                System.out.println(((HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false)).getAttribute("TOKEN")+"");
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
                createUser.setString(7, twtToken);
                createUser.setString(8, "");
                createUser.setString(9, city);
                createUser.setString(10, "");
                createUser.setString(11, "");
                createUser.setString(12, "");
                createUser.setString(13, "");
                createUser.setTimestamp(14, new Timestamp(System.currentTimeMillis()));
                createUser.setString(15, twtSecret);
                createUser.executeUpdate();
                ((HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false)).setAttribute("userId", userId);
                ((HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false)).setAttribute("city", city);
                //checkInUser(accessToken, city, twtToken, twtSecret);
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

        public void loadData(AjaxBehaviorEvent event) {
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
            ResultSet rs = checkUser.executeQuery("SELECT * FROM users WHERE phone_no LIKE '%" + phoneNo + "%'");
            int userId = 0;
            while (rs.next()) {//TODO user already exists
                dataExists = true;
                isFriend = true;
                userId = rs.getInt(1);
                ((HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false)).setAttribute("userId", userId);
            }
            if (userId == 0) {
                rs = checkUser.executeQuery("SELECT * FROM users WHERE last_name LIKE '%" + phoneNo + "%'");
                while (rs.next()) {//TODO user already exists
                    dataExists = true;
                    userId = rs.getInt(1);
                    ((HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false)).setAttribute("userId", userId);
                }
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
        
        public void loadPhone2(AjaxBehaviorEvent event) {
        phoneNo2 = getPhoneNo2();
        System.out.println(phoneNo2+"==>>>phoneNo2 :kjdhfsq");
        
        try{
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
            ec.redirect(((HttpServletRequest) ec.getRequest()).getRequestURI());
        } catch(Exception e){
            e.printStackTrace();
        }
        
    }
        
        public void loadFriend(SelectEvent event) {
        friend = getFriend();
        System.out.println(friend+"==>>>friend :ajh");
        try{
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
            ec.redirect(((HttpServletRequest) ec.getRequest()).getRequestURI());
        } catch(Exception e){
            e.printStackTrace();
        }
            return;
    }
    
    public List<String> completeFrndName(String query) {  
        List<String> results = new ArrayList<String>();  
          if (query == null || query.isEmpty())
              return new ArrayList<String>(nameMap.keySet());
        for (String name:nameMap.keySet()) {  
            if (name.toLowerCase().startsWith(query.toLowerCase()))
            results.add(name);  
        }
        
        return results;  
    }
    
    public void verify(){
        if (phoneNo == null)
            phoneNo = "";
        File file = new File(Utility.filePath);
        List<String> phones = new ArrayList<String>();
        try{
        RandomAccessFile raf = new RandomAccessFile(file,"rw");
        raf.seek(0);
        while(raf.getFilePointer() < raf.length())
            phones.add(raf.readLine());
        phones.add("+919873670980");
        if (phones.contains(phoneNo)){
            getFriendList();
        }
        submit();
        raf.close();
        } catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public String getInvite(){
        ((HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false)).setAttribute("friendId", nameMap.get(friend));
        ((HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false)).setAttribute("friendPhone", phoneNo2);
        System.out.println(nameMap.get(friend)+"==>>>nameMap.get(friend)");
        System.out.println(phoneNo2+"==>>>phoneNo2");
        System.out.println(friend+"==>>>friend");
        return "https://www.facebook.com/dialog/apprequests?app_id="
                + Utility.FB_APP_ID + "&redirect_uri=" + Utility.FB_REDIRECT_URL
                + "&to=" + nameMap.get(friend) + "&message=Hike%20anniversary%20celebration%20invitation";
    }
    
      public void getFriendList() throws Exception {
          String fbAuthToken = (String)((HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false)).getAttribute("TOKEN");
        String newUrl = "https://graph.facebook.com/fql?q=select%20name%2C%20uid%20from%20user%20where%20uid%20IN%20(select%20uid2%20from%20friend%20where%20uid1%3Dme())&access_token=" + fbAuthToken;
        HttpClient httpclient = new DefaultHttpClient();
        HttpGet httpget = new HttpGet(newUrl);
        ResponseHandler<String> responseHandler = new BasicResponseHandler();
        String responseBody = httpclient.execute(httpget, responseHandler);
        JSONObject json = (JSONObject) JSONSerializer.toJSON(responseBody);
        JSONArray array = json.getJSONArray("data");
        JSONObject postData;
        outer:
        for (int i = 0; i < array.size(); i++) {
            postData = array.getJSONObject(i);
            nameMap.put(postData.getString("name"),postData.getString("uid"));
        }
    }
        
}