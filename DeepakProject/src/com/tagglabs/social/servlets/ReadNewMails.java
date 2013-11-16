
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
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.search.FlagTerm;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

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

        props.put("mail.pop3.user", Utility.EMAIL);

        props.put("mail.pop3.socketFactory", 995);

        props.put("mail.pop3.socketFactory.class", "javax.net.ssl.SSLSocketFactory");

        props.put("mail.pop3.port", 995);

        Session session = Session.getDefaultInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(Utility.EMAIL, Utility.PASSWORD);
            }
        });
        HttpClient client = null;
            Store store = null;
            Folder fldr = null;
        try {
            client = new DefaultHttpClient();
            store = session.getStore("imaps");
            store.connect("imap.gmail.com", Utility.EMAIL, Utility.PASSWORD);
            fldr = store.getFolder("INBOX");
            fldr.open(Folder.READ_WRITE);
            Message msg[] = fldr.search(new FlagTerm(new Flags(
                    Flags.Flag.SEEN), false));
            System.out.println(msg.length + "...." + fldr.getUnreadMessageCount() + "...." + fldr.getNewMessageCount());
            Connection conn = Utility.getConnection();
            Statement checkUser = conn.createStatement();
            for (int i = 0; i < msg.length; i++) {
                if (msg[i].getSubject() == null || msg[i].getSubject().trim().equalsIgnoreCase(""))
                    continue;
                System.out.println(i+". Subject:::"+msg[i].getSubject());
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
                    System.out.println("File created....");
                    for(String phoneNo : phoneNos) {
                        ResultSet rs = checkUser.executeQuery("SELECT fb_auth_token FROM users WHERE phone_no LIKE '%" + phoneNo + "%'");
                        while (rs.next()) {//TODO user already exists
                            HttpPost post = new HttpPost("https://graph.facebook.com/me/photos?access_token="+rs.getString(1)); 
                            MultipartEntity reqEntity = new MultipartEntity();
                            reqEntity.addPart("source", new FileBody(f));
                            post.setEntity(reqEntity);
                            HttpResponse response1 = client.execute(post);
                            HttpEntity resEntity = response1.getEntity();
                            System.out.println(response1.getStatusLine());
                            if (resEntity != null) {
                              System.out.println(EntityUtils.toString(resEntity));
                            }
                            if (resEntity != null) {
                              resEntity.consumeContent();
                            }
                        }
                        System.out.println("File posted....");
                            client.getConnectionManager().shutdown();
                    }
                }
                fldr.setFlags(msg, new Flags(Flags.Flag.SEEN), true);
                System.out.println("SentDate : " + msg[i].getSentDate() + "\n" + "From : " + msg[i].getFrom()[0] + "\n" + "Subject : " + msg[i].getSubject() + "\n" + "Message : " + "\n" + msg[i].getContent().toString());
            }
            fldr.close(true);
            store.close();
        } catch (Exception e) {
            try{
            fldr.close(true);
            store.close();
            } catch(Exception e1){
                throw new RuntimeException(e1);
            }
            client.getConnectionManager().shutdown();
            throw new RuntimeException(e);
        }
    }
}
