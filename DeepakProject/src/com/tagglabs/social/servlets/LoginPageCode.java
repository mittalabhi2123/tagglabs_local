package com.tagglabs.social.servlets;

import com.tagglabs.social.utils.Utility;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.auth.RequestToken;

@ManagedBean(name = "loginPageCode")
@SessionScoped
public class LoginPageCode implements Serializable {

    private boolean dataExists = false;
    private static final LinkedHashMap<String, String[]> cityMap = new LinkedHashMap<String, String[]>();

    static {
        if (cityMap.isEmpty()) {
            cityMap.put("City", new String[]{"College"});
            cityMap.put("AHD+Baroda", new String[]{"College", "CU SHAH SCIENCE", "CU SHAH COMMERCE", "CU SHAH ARTS", "KS", "SILVER OAK", "KALOL", "NGC", "GIT", "MC SHAH", "CC SETH", "IITE", "MB PATEL", "NS PATEL", "BIT BARODA", "MSU", "IIM", "MICA", "ASIA PACIFIC", "LJ INSTITUTE OF MGT", "NIRMA INSTITUE OF TECH", "ID COLLEGE", "AHMD. INSTITUTE OF TECH", "BHAVANS", "HL COLLEGE OF COMMERCE", "HA", "CAB1", "CAB2", "CAB3", "CAB4", "CAB5", "CAB6", "CAB7", "CAB8", "CAB9", "CAB10", "CAB11", "CAB12", "CAB13", "CAB14", "CAB15", "CAB16", "CAB17", "CAB18", "CAB19"});
            cityMap.put("Bangaluru", new String[]{"College", "GARDEN CITY ", "BALDWIN", "DAYANAND SAGAR", "MS RAMIAH", "MOUNT CARMEL", "BISHOP COTTON", "EAST POINT", "UNIV VISVESWARAYA", "SINDHI", "RC", "ST. JOSEPHS", "S NIJALINGAPPA", "NATIONAL DEGREE", "APS", "SLN", "VIJAYA", "IIM", "PRESIDENCY", "DON BOSCO", "BANGALORE CITY COLLEGE", "GAUTHAM COLLEGE OF SCIENCE", "OXFORD COLLEGE OF BUSINESS MGT", "REVA", "NIFT", "INTERNATIONAL INST OF INFORMATION TECHNOLOGY", "SYMBIOSIS INSTITUTE OF BUSINESS", "RAJA REDDY INST OF TECHNOLOGY", "ST.JOHNS MEDICAL COLLEGE", "INDIAN STATISTICAL INSTITUTE", "CB1", "CB2", "CB3", "CB4", "CB5", "CB6", "CB7", "CB8", "CB9", "CB10", "CB11", "CB12", "CB13", "CB14", "CB15"});
            cityMap.put("Chandigarh", new String[]{"College", "DAV", "GURU GOBIND SINGH", "PUNJAB UNIVERSITY", "CHANDIGARH COLLEGE OF ARCHITECTURE", "PUNJAB ENGG COLLEGE", "IPMIR", "GGDSD", "GOVT. MEDICAL COLLEGE", "COLLEGE OF ART", "PGIMER", "INDO GLOBAL COLLEGE OF ARCHITECTURE", "CCHD1", "CCHD2", "CCHD3", "CCHD4", "CCHD5", "CCHD6", "CCHD7", "CCHD8", "CCHD9", "CCHD10", "CCHD11", "CCHD12", "CCHD13", "CCHD14", "CCHD15", "CCHD16", "CCHD17", "CCHD18", "CCHD19", "CCHD20", "CCHD21", "CCHD22", "CCHD23", "CCHD24", "CCHD25", "CCHD26", "CCHD27", "CCHD28", "CCHD29", "CCHD30", "CCHD31", "CCHD32", "CCHD33"});
            cityMap.put("Chennai", new String[]{"College", "PRESIDENCY", "LOYOLA", "QUEENS MARY", "IIT", "MADRAS CHRISTIAN", "VIVEKANANDA", "GURU NANAK", "SATYABHAMA", "STELLA", "CHELLAMAL", "SRM", "HINDUSTAN", "NIFT", "ETHIRAJ", "DG VAISHNAVA", "NATIONAL INST OF BUSINESS MGMT", "ALAGAPPA COLLEGE OF TECH", "MADRAS INSTITUTE OF TECH", "SSN COLLEGE OF ENGG", "JAYA ENGG COLLEGE", "ST JOSEPHS COLLEGE OF ENGG", "CC1", "CC2", "CC3", "CC4", "CC5", "CC6", "CC7", "CC8", "CC9", "CC10", "CC11", "CC12", "CC13", "CC14", "CC15", "CC16", "CC17", "CC18", "CC19", "CC20", "CC21", "CC22", "CC23"});
            cityMap.put("Delhi NCR", new String[]{"College", "SRCC", "HINDU", "MIRANDA", "ST.STEPHENS", "KMC", "RAMJAS", "DR", "KHALSA", "DCE", "GURU GOBIND SINGH UNIVERSITY", "DEEN DAYAL", "VENKY", "ARSD", "JESUS&MARY", "KAMALA NEHRU", "VIVEKANAND", "IP", "LSR", "AUROBINDO", "SHAHEED BHAGAT SINGH", "AND", "RAJDHANI", "SPAC", "IIT DELHI", "AMITY", "SHARDA UNIVERSITY", "FMS", "AIIMS", "LADY IRWIN", "NIFT", "IIFT", "FORE", "IMI", "IIPM", "HANSRAJ COLLEGE", "MAHARAJ AGRASEN", "CD1", "CD2", "CD3", "CD4", "CD5", "CD6", "CD7", "CD8"});
            cityMap.put("Hyderabad", new String[]{"College", "Aurora", "Badruka", "methodist", "St.Peters", "RG Kedia", "St.Francis", "St. Anns", "Bhavans", "St. Mary", "Stanley", "Gitam", "CMR Vishwabharati", "Loyola", "Nizam", "CBIT", "NIFT", "IMT", "CH1", "CH2", "CH3", "CH4", "CH5", "CH6", "CH7", "CH8", "CH9", "CH10", "CH11", "CH12", "CH13", "CH14", "CH15", "CH16", "CH17", "CH18", "CH19", "CH20", "CH21", "CH22", "CH23", "CH24", "CH25", "CH26", "CH27"});
            cityMap.put("Kolkata", new String[]{"College", "GOKHALE", "LADY BRABOURNE", "LORETO", "PRESIDENCY", "ST. XAVIERS", "ASHUTOSH", "BETHUNE", "BISHOP", "GOENKA", "SCOTTISH", "SOUTH CALCUTTA", "ST. PAULS", "DINABANDHU", "VIVEKANANDA", "SOUTH CITY", "JADAVPUR", "IIM", "IISWBM", "BHOWANIPORE", "NORTH CITY", "ANDREWS", "BEHALA COLLEGE", "NEW ALIPORE COLLEGE", "CK1", "CK2", "CK3", "CK4", "CK5", "CK6", "CK7", "CK8", "CK9", "CK10", "CK11", "CK12", "CK13", "CK14", "CK15", "CK16", "CK17", "CK18", "CK19", "CK20", "CK21"});
            cityMap.put("Mumbai", new String[]{"College", "ELPHINSTONE", "HR CHURCHGATE", "JAI HIND", "KC COLLEGE, CHURCHGATE", "KHALSA COLLEGE , MATUNGA", "LALA LAJPATRAI", "LS RAHEJA", "MITHIBAI, VILLE PARLE", "MMK BANDRA", "NM VILLE PARLE", "RIZVI", "SK SOMAIYA VIDYA VIHAR", "SYDENHAM", "WILSON", "THAKUR COLLEGE, KANDIVILLI", "NATIONAL", "CHETNA", "BHAVANS", "IIT POWAI", "RA PODDAR , MATUNGA", "RUIA", "THADOMAL", "HINDUJA", "SOPHIYA", "BURHANI", "SP JAIN", "WELINGKAR", "JAMNALAL BAJAJ", "SIES SION", "ICIT", "SARDAR PATEL COLLEGE OF ENGG", "TOLANI", "KBP HINDUJA, CHARNI", "GHAMSHYAM DAS SARAF, MALAD", "KJ SOMMAIYA, VIDYA VIHAR", "JHUNJHUNWALA COLLEGE, GHATKOPAR", "VIVA COLLEGE, VIRAR", "VAZE KELKAR, MULUND", "PD DALMIA LIONS, MALAD", "NAGINDAS KHANDWALA , MALAD", "CM1", "CM2", "CM3", "CM4"});
            cityMap.put("Navi Mumbai", new String[]{"College", "ACPE", "FR C RODRIGUES", "ITM KHARGAR", "INDIRA INSTITUTE OF MGT", "RAJEEV GANDHI COLLEGE", "STERLING INSTITUTE NERUL", "PILLAI ISTITUTE", "SIES", "DY PATIL", "BHARTI VIDYAPEETH-ARCHITECTURE", "YERALA DENTAL COLLEGE", "DATTA MEGHE COLLEGE OF ENGG", "LOKMANYA TILAK COLLEG OF ENGG", "TERNA ENGG COLLEGE", "RAMAROA ADIK COLLEGE OF ENGG", "PARSVNATH COLLEGE OF ENGG", "IIFT KHARGAR", "BN BANDOKAR, THANE", "JOSHI BEDEKAR, THANE", "CHM COLLEGE , ULHASNAGAR", "CNM1", "CNM2", "CNM3", "CNM4", "CNM5", "CNM6", "CNM7", "CNM8", "CNM9", "CNM10", "CNM11", "CNM12", "CNM13", "CNM14", "CNM15", "CNM16", "CNM17", "CNM18", "CNM19", "CNM20", "CNM21", "CNM22", "CNM23", "CNM24"});
            cityMap.put("Pune", new String[]{"College", "SYMBIOSIS", "FERGUSON", "SNDT", "NESS WADIA", "NEVILLE WADIA", "DY PATIL", "DECCAN", "BHARTI VIDYAPEETH", "GOKHALE", "IISER", "IMED", "BMCC", "CURSOW WADIA", "FMS", "FTII", "ISMR", "IBMR", "ILS LAW", "SINGHNAD ENGG", "MIT", "IIBR", "MIT INSTITUTE OF DESIGN", "DSK SUPINFOCOM", "POONA COLLEGE OF ARTS & COMMERCE", "CP1", "CP2", "CP3", "CP4", "CP5", "CP6", "CP7", "CP8", "CP9", "CP10", "CP11", "CP12", "CP13", "CP14", "CP15", "CP16", "CP17", "CP18", "CP19", "CP20"});
            
        }
    }
    private static final long serialVersionUID = -1611162265998907599L;
    private String firstName;
    private String lastName;
    private String phoneNo;
    private String college;
    private String city;
    private ArrayList<SelectItem> cities;
    private ArrayList<SelectItem> colleges;
    
    private boolean fbOk = false;
    private boolean twitterOk = false;
    private boolean instagramOk = false;
    
    private boolean anyOneConnected = false;

    public boolean isAnyOneConnected() {
        anyOneConnected = isFbOk() || isTwitterOk() || isInstagramOk();
        return anyOneConnected;
    }

    public boolean isFbOk() {
        HttpSession session =
                (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        String portal = (String) session.getAttribute("portal");
        if (portal != null && portal.equalsIgnoreCase("facebook"))
            fbOk = true;
        return fbOk;
    }

    public boolean isTwitterOk() {
        HttpSession session =
                (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        String portal = (String) session.getAttribute("portal");
        if (portal != null && portal.equalsIgnoreCase("twitter"))
            twitterOk = true;
        return twitterOk;
    }

    public boolean isInstagramOk() {
        HttpSession session =
                (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        String portal = (String) session.getAttribute("portal");
        if (portal != null && portal.equalsIgnoreCase("instagram"))
            instagramOk = true;
        return instagramOk;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getCollege() {
        return college;
    }

    public void setCollege(String college) {
        this.college = college;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public ArrayList<SelectItem> getCities() {
        if (cities == null) {
            cities = new ArrayList<SelectItem>();
            for (String str : cityMap.keySet()) {
                cities.add(new SelectItem(str));
            }
        }
        return cities;
    }

    public void setCities(ArrayList<SelectItem> cities) {
        this.cities = cities;
    }

    public ArrayList<SelectItem> getColleges() {
         if (colleges == null) {
            colleges = new ArrayList<SelectItem>();
        } else {
            colleges.clear();
        }
        if (cities == null) {
            getCities();
        }
        String cityTemp = (getCity() == null || getCity().length() == 0) ? (String) cities.get(0).getValue() : getCity();
        for (String str : cityMap.get(cityTemp)) {
            colleges.add(new SelectItem(str));
        }
        return colleges;
    }

    public void setColleges(ArrayList<SelectItem> colleges) {
        this.colleges = colleges;
    }

    public void cityChangeLstnr(ValueChangeEvent event) {
        if (colleges == null) {
            colleges = new ArrayList<SelectItem>();
        } else {
            colleges.clear();
        }
        System.out.println(event.getNewValue());
        for (String str : cityMap.get(event.getNewValue())) {
            colleges.add(new SelectItem(str));
        }
    }

    public String getFacebookUrlAuth() {;
        HttpSession session =
                (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        String sessionId = session.getId();
        String returnValue = "https://www.facebook.com/dialog/oauth?client_id="
                + Utility.FB_APP_ID + "&redirect_uri=" + Utility.FB_REDIRECT_URL
                + "&scope=email,user_birthday,read_stream,user_about_me&state=" + sessionId;
        session.setAttribute("firstName", Utility.FB_APP_ID);
        System.out.println(returnValue);
        return returnValue;
    }

    public String getUserFromSession() {
        HttpSession session =
                (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        String userName = (String) session.getAttribute("USER");
        String email = (String) session.getAttribute("EMAIL");
        String portal = (String) session.getAttribute("portal");
        if (portal != null){
            if (portal.equalsIgnoreCase("facebook"))
                fbOk = true;
            else if (portal.equalsIgnoreCase("twitter"))
                twitterOk = true;
            else if (portal.equalsIgnoreCase("instagram"))
                instagramOk = true;
        }
        if (userName != null) {
            return "Welcome " + userName + "</br>Email : " + email;
        } else {
            return "";
        }
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

    public String getTwitterUrlAuth() {;
        Twitter twitter = new TwitterFactory().getInstance();
        twitter.setOAuthConsumer(Utility.TWITTER_CONSUMER_KEY, Utility.TWITTER_CONSUMER_SECRET);
        try {
            RequestToken requestToken = twitter.getOAuthRequestToken(Utility.TWITTER_REDIRECT_URL, Utility.TWITTER_REDIRECT_URL);
            HttpSession session =
                    (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
            session.setAttribute("twitter", twitter);
            session.setAttribute("requestToken", requestToken);
            return requestToken.getAuthorizationURL();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void loadData(AjaxBehaviorEvent event) {
        System.out.println("loadData*************************");
        firstName = "";
        lastName = "";
        city = "";
        college = "";
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
            while (rs.next()) {//TODO user already exists
                dataExists = true;
                int userId = rs.getInt(1);
                ((HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false)).setAttribute("userId", userId);
                firstName = rs.getString(2);
                lastName = rs.getString(3);
                city = rs.getString(9);
                college = rs.getString(10);
                String fbToken = rs.getString(6);
                String twitterToken = rs.getString(7);
                String instaToken = rs.getString(8);
                if (fbToken != null && fbToken.length() > 0)
                    fbOk = true;
                else fbOk = false;
                if (twitterToken != null && twitterToken.length() > 0)
                    twitterOk = true;
                else twitterOk = false;
                if (instaToken != null && instaToken.length() > 0)
                    instagramOk = true;
                else instagramOk = false;
            }
            if (firstName == null || firstName.length() <= 0) {
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
                createUser.setString(2, firstName);
                createUser.setString(3, lastName);
                createUser.setInt(4, 0);
                createUser.setString(5, String.valueOf(phoneNo));
                createUser.setString(6, "");
                createUser.setString(7, "");
                createUser.setString(8, "");
                createUser.setString(9, city);
                createUser.setString(10, college);
                createUser.setString(11, "");
                createUser.setString(12, "");
                createUser.setString(13, "");
                createUser.setTimestamp(14, new Timestamp(System.currentTimeMillis()));
                createUser.setString(15, "");
                createUser.executeUpdate();
                ((HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false)).setAttribute("userId", userId);
            } else {
                int userId = Integer.parseInt(((HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false)).getAttribute("userId").toString());
                PreparedStatement createUser = conn.prepareStatement("UPDATE users SET first_name=?, last_name=?, phone_no=?, city=?, college=? WHERE user_id = ?");
                createUser.setString(1, firstName);
                createUser.setString(2, lastName);
                createUser.setString(3, String.valueOf(phoneNo));
                createUser.setString(4, city);
                createUser.setString(5, college);
                createUser.setInt(6, userId);
                createUser.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
            showError(e.getLocalizedMessage());
            return null;
        }
        return "social";
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
}