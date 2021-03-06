//
//package com.tagglabs.social.servlets;
//
//import com.tagglabs.social.utils.Utility;
//import java.io.File;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.sql.Connection;
//import java.sql.ResultSet;
//import java.sql.Statement;
//import java.util.Properties;
//import javax.mail.Authenticator;
//import javax.mail.BodyPart;
//import javax.mail.Flags;
//import javax.mail.Folder;
//import javax.mail.Message;
//import javax.mail.Multipart;
//import javax.mail.Part;
//import javax.mail.PasswordAuthentication;
//import javax.mail.Session;
//import javax.mail.Store;
//import javax.mail.search.FlagTerm;
//import javax.servlet.ServletException;
//import javax.servlet.annotation.WebServlet;
//import javax.servlet.http.HttpServlet;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import org.apache.commons.lang.StringUtils;
//import org.apache.http.HttpEntity;
//import org.apache.http.HttpResponse;
//import org.apache.http.client.HttpClient;
//import org.apache.http.client.methods.HttpPost;
//import org.apache.http.entity.mime.MultipartEntity;
//import org.apache.http.entity.mime.content.FileBody;
//import org.apache.http.entity.mime.content.StringBody;
//import org.apache.http.impl.client.DefaultHttpClient;
//import org.apache.http.util.EntityUtils;
//import twitter4j.StatusUpdate;
//import twitter4j.Twitter;
//import twitter4j.TwitterFactory;
//import twitter4j.auth.AccessToken;
//import twitter4j.conf.ConfigurationBuilder;
//
///**
// *
// * @author abhishekmitt
// */
//@WebServlet(name = "ReadNewMails", urlPatterns = {"/readNewMails"})
//public class ReadNewMails extends HttpServlet {
//
//    protected void doGet(HttpServletRequest request, HttpServletResponse response)
//            throws ServletException, IOException {
//        doPost(request, response);
//    }
//
//    protected void doPost(HttpServletRequest request, HttpServletResponse response)
//            throws ServletException, IOException {
//    
//
//        Properties props = new Properties();
//
//        props.put("mail.pop3.host", "pop.gmail.com");
//
//        props.put("mail.pop3.user", Utility.EMAIL);
//
//        props.put("mail.pop3.socketFactory", 995);
//
//        props.put("mail.pop3.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
//
//        props.put("mail.pop3.port", 995);
//
//        Session session = Session.getDefaultInstance(props, new Authenticator() {
//            @Override
//            protected PasswordAuthentication getPasswordAuthentication() {
//                return new PasswordAuthentication(Utility.EMAIL, Utility.PASSWORD);
//            }
//        });
//        HttpClient client = null;
//            Store store = null;
//            Folder fldr = null;
//        try {
//            client = new DefaultHttpClient();
//            store = session.getStore("imaps");
//            store.connect("imap.gmail.com", Utility.EMAIL, Utility.PASSWORD);
//            fldr = store.getFolder("INBOX");
//            fldr.open(Folder.READ_WRITE);
//            Message msg[] = fldr.search(new FlagTerm(new Flags(
//                    Flags.Flag.SEEN), false));
//            System.out.println(msg.length + "...." + fldr.getUnreadMessageCount() + "...." + fldr.getNewMessageCount());
//            Connection conn = Utility.getConnection();
//            Statement checkUser = conn.createStatement();
//            for (int i = 0; i < msg.length; i++) {
//                if (msg[i].getSubject() == null || msg[i].getSubject().trim().equalsIgnoreCase(""))
//                    continue;
//                System.out.println(i+". Subject:::"+msg[i].getSubject());
//                String[] phoneNos = msg[i].getSubject().split(",");
//                Multipart multipart = null;
//                try{
//                    multipart = (Multipart) msg[i].getContent();
//                } catch(ClassCastException ec){
//                    continue;
//                }
//                for (int j = 0; j < multipart.getCount(); j++) {
//                    BodyPart bodyPart = multipart.getBodyPart(j);
//                    if(!Part.ATTACHMENT.equalsIgnoreCase(bodyPart.getDisposition()) &&
//                               !StringUtils.isNotBlank(bodyPart.getFileName())) {
//                      continue; // dealing with attachments only
//                    } 
//                    InputStream is = bodyPart.getInputStream();
//                    File f = new File("C:\\tmp\\" + phoneNos[0] + "." + bodyPart.getFileName().split("[.]")[1]);
//                    FileOutputStream fos = new FileOutputStream(f);
//                    byte[] buf = new byte[4096];
//                    int bytesRead;
//                    while((bytesRead = is.read(buf))!=-1) {
//                        fos.write(buf, 0, bytesRead);
//                    }
//                    fos.close();
//                    System.out.println("File created....");
//                    phones:for(String phoneNo : phoneNos) {
//                        phoneNo = phoneNo.trim();
//                        ResultSet rs = checkUser.executeQuery("SELECT fb_auth_token,city, twitter_auth_token, twitter_auth_secret, phone_no FROM users WHERE phone_no LIKE '%" + phoneNo + "%'");
//                        while (rs.next()) {//TODO user already exists
//                            System.out.println(phoneNo);
//                            ResultSet rs2 = Utility.getConnection().createStatement().executeQuery("SELECT eventId, picCaption FROM day_college WHERE city = '" + rs.getString(2) + "' LIMIT 1");
//                            if(!rs2.next())
//                                break;
//                            String message = rs2.getString(2);
//                            String fbAccessToken = rs.getString(1);
//                            if(fbAccessToken == null || fbAccessToken.isEmpty() || fbAccessToken.trim().length() < 8){
//                                System.out.println("hello....."+fbAccessToken);
//                                postTwitterImage(rs.getString(3), rs.getString(4), f, message);
//                                fldr.setFlags(new Message[]{msg[i]}, new Flags(Flags.Flag.SEEN), true);
//                                continue phones;
//                            }
//                            HttpPost post = new HttpPost("https://graph.facebook.com/me/photos?access_token="+rs.getString(1)); 
//                            MultipartEntity reqEntity = new MultipartEntity();
//                            reqEntity.addPart("source", new FileBody(f));
//                            reqEntity.addPart("message", new StringBody(message));
//                            post.setEntity(reqEntity);
//                            post.setHeader("message", message);
//                            HttpResponse response1 = client.execute(post);
//                            HttpEntity resEntity = response1.getEntity();
//                            if (resEntity != null) {
//                                String responseData = EntityUtils.toString(resEntity);
//                              //{"id":"10152026299253058","post_id":"669853057_514100968696757"}
//                              String intermediateData = responseData.split(",")[0].split(":")[1];
//                              String photoId = intermediateData.substring(1, intermediateData.length() - 1);
//                              Utility.getConnection().createStatement().executeUpdate("INSERT INTO user_fb_photo VALUES ('"+rs.getString(5)+"','"+photoId+"')");
//                            }
//                            if (resEntity != null) {
//                              resEntity.consumeContent();
//                            }
//                            fldr.setFlags(new Message[]{msg[i]}, new Flags(Flags.Flag.SEEN), true);
//                        }
//                        System.out.println("File posted....");
//                            
//                    }
////                    f.delete();
//                }
//                
//                
//            }
//            fldr.close(true);
//            store.close();
//            client.getConnectionManager().shutdown();
//        } catch (Exception e) {
//            try{
//                client.getConnectionManager().shutdown();
//                            
//            fldr.close(true);
//            store.close();
//            } catch(Exception e1){
//                throw new RuntimeException(e1);
//            }
//            client.getConnectionManager().shutdown();
//            throw new RuntimeException(e);
//        }
//    }
//    
//    private void postTwitterImage(String twtToken, String twtSecret, File image, String message) throws Exception{
//        ConfigurationBuilder builder = new ConfigurationBuilder();
//        builder.setOAuthConsumerKey(Utility.TWITTER_CONSUMER_KEY);
//        builder.setOAuthConsumerSecret(Utility.TWITTER_CONSUMER_SECRET);
//        Twitter twitter = new TwitterFactory(builder.build()).getInstance(new AccessToken(twtToken, twtSecret));
//        StatusUpdate status = new StatusUpdate("#LGmycricketmyvoice - That's me with the original trophy of LG People's Choice Award.");
//        status.setMedia(image);
//        twitter.updateStatus(status);
//    }
//}
