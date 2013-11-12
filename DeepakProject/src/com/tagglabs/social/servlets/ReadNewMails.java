
package com.tagglabs.social.servlets;

import com.tagglabs.social.utils.Utility;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;
import javax.faces.context.FacesContext;
import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.MimeMultipart;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;

/**
 *
 * @author abhishekmitt
 */
@WebServlet(name = "ReadNewMails", urlPatterns = {"/readNewMails"})
public class ReadNewMails extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    

        Properties props = new Properties();

        props.put("mail.pop3.host", "pop.gmail.com");

        props.put("mail.pop3.user", "userName");

        props.put("mail.pop3.socketFactory", 995);

        props.put("mail.pop3.socketFactory.class", "javax.net.ssl.SSLSocketFactory");

        props.put("mail.pop3.port", 995);

        Session session = Session.getDefaultInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("userName", "password");
            }
        });
        try {
            Store store = session.getStore("pop3");
            store.connect("pop.gmail.com", "userName", "password");
            Folder fldr = store.getFolder("INBOX");
            fldr.open(Folder.READ_ONLY);
            Message[] msg = fldr.getMessages(1,fldr.getUnreadMessageCount());
            Address[] address;
            Connection conn = Utility.getConnection();
            Statement checkUser = conn.createStatement();
            for (int i = 0; i < msg.length; i++) {
                String[] phoneNos = msg[i].getSubject().split(",");
                Multipart multipart = (Multipart) msg[i].getContent();
                
                for (int j = 0; j < multipart.getCount(); j++) {
                    BodyPart bodyPart = multipart.getBodyPart(j);
                    if(!Part.ATTACHMENT.equalsIgnoreCase(bodyPart.getDisposition()) &&
                               !StringUtils.isNotBlank(bodyPart.getFileName())) {
                      continue; // dealing with attachments only
                    } 
                    InputStream is = bodyPart.getInputStream();
                    File f = new File("/tmp/" + bodyPart.getFileName());
                    FileOutputStream fos = new FileOutputStream(f);
                    byte[] buf = new byte[4096];
                    int bytesRead;
                    while((bytesRead = is.read(buf))!=-1) {
                        fos.write(buf, 0, bytesRead);
                    }
                    fos.close();
                    for(String phoneNo : phoneNos) {
                        ResultSet rs = checkUser.executeQuery("SELECT fb_auth_token FROM users WHERE phone_no = '" + phoneNo + "'");
                        while (rs.next()) {//TODO user already exists
//                            PostMethod filePost = new PostMethod('https://graph.facebook.com/me/photos');
//    filePost.getParams().setBooleanParameter(HttpMethodParams.USE_EXPECT_CONTINUE, false);
//    try {
//      println("Uploading " + file.getName() + " to 'https://graph.facebook.com/me/photos'");
//      Part[] parts = [new FilePart('source', f.getName(), f), new StringPart('access_token', rs.getString(1)), new StringPart('message', '')]
//      filePost.setRequestEntity(new MultipartRequestEntity(parts, filePost.getParams()));
//      HttpClient client = new DefaultHttpClient();
//      client.getHttpConnectionManager().getParams().setConnectionTimeout(5000);
//      int status = client.executeMethod(filePost);
//      if (status == HttpStatus.SC_OK) {
//        println("Upload complete, response=" + filePost.getResponseBodyAsString());
//      } else {
//        println("Upload failed, response=" + HttpStatus.getStatusText(status));
//        // Create response
//        StringBuilder notificationsSendResponse = new StringBuilder();
//        byte[] byteArrayNotifications = new byte[4096];
//        for (int n; (n = filePost.getResponseBodyAsStream().read(byteArrayNotifications)) != -1;) {
//          notificationsSendResponse.append(new String(byteArrayNotifications, 0, n));
//        }
//        String notificationInfo = notificationsSendResponse.toString();
//      }
//    } catch (Exception ex) {
//      println("ERROR: " + ex.getClass().getName() + " " + ex.getMessage());
//      ex.printStackTrace();
//    } finally {
//      filePost.releaseConnection();
//    }
                        }
                    }
                }
                
                System.out.println("SentDate : " + msg[i].getSentDate() + "\n" + "From : " + msg[i].getFrom()[0] + "\n" + "Subject : " + msg[i].getSubject() + "\n" + "Message : " + "\n" + msg[i].getContent().toString());
            }
            fldr.close(true);
            store.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
