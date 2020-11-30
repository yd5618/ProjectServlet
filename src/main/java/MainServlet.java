import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet(urlPatterns={"/data"},loadOnStartup = 1) // check what this means exactly

public class MainServlet extends HttpServlet {
    // doGet will handle GET requests of our client & give appropriate response
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //
    }

    // doPost will handle POST requests of our client & give appropriate response 
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //
    }

}
