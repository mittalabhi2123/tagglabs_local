
import facebook4j.Facebook;
import facebook4j.FacebookFactory;
import facebook4j.auth.AccessToken;
import java.io.IOException;
import java.io.PrintWriter;
import javax.faces.context.FacesContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


@WebServlet(urlPatterns = {"/RegisterPage"})
public class RegisterPage extends HttpServlet {


    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Facebook facebook = new FacebookFactory().getInstance();
        facebook.setOAuthAppId(Utility.FB_APP_ID, Utility.FB_APP_SECRET);
        facebook.setOAuthPermissions("email,user_birthday,read_stream,user_about_me,photo_upload,rsvp_event");
        try {
            AccessToken requestToken = facebook.getOAuthAccessToken(Utility.FB_REDIRECT_URL);
                        
            HttpSession session =
                    (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
            session.setAttribute("facebook", facebook);
            session.setAttribute("accessToken", requestToken);
//            response.sendRedirect(requestToken.getAuthorizationURL());
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
