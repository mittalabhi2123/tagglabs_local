
import facebook4j.Facebook;
import facebook4j.FacebookFactory;
import facebook4j.auth.AccessToken;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Timer;
import java.util.TimerTask;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns = {"/fbAlbum"})
public class PostPics2FbAlbum extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            Timer timer = Utility.getTimer();
        timer.schedule(new RemindTask(), 5 * 1000, 15 * 60 * 1000);   
        Facebook facebook = new FacebookFactory().getInstance();
        facebook.setOAuthAppId(Utility.FB_APP_ID, Utility.FB_APP_SECRET);
        facebook.setOAuthPermissions("email,user_birthday,read_stream,user_about_me,photo_upload,rsvp_event");
        AccessToken accessToken = facebook.getOAuthAccessToken();
    }
}

class RemindTask extends TimerTask {

        public void run() {
            File sourceFile = new File(File.separator + "data");
            File destFile = new File(File.separator + "slideshow");
            if (!destFile.exists()) {
                destFile.mkdirs();
            }
            for (File file : sourceFile.listFiles()) {
                if (file.isFile()) {
                    try {

                    } catch (Exception e) {
                        e.printStackTrace();
                        continue;
                    }
                }
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

}
