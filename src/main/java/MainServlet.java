import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.awt.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Statement;
import java.util.stream.Collectors;

@WebServlet(urlPatterns={"/DBaccess"},loadOnStartup = 1) // check what this means exactly
// specifies which servlet should be invoked for a url given by client?
// specifies how servlet can be accessed basically - can be accessed through more URL patterns if needed

public class MainServlet extends HttpServlet {
    // doGet will handle GET requests of our client & give appropriate response
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // what do we do with req?
        resp.setContentType("text/html");
        resp.getWriter().write("You have accessed the servlet");
        // req.getServletPath(); returns the ServletPath of the URL
        try {
            processGETRequest(req, resp);
        }
        catch (Exception e) {
            System.out.println("There was a problem");
        }
    }

    // doPost will handle POST requests of our client & give appropriate response
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String reqBody=req.getReader().lines().collect(Collectors.joining(System.lineSeparator()));

        // Connecting to the DB and querying what the POST request gave
        Connection con = DBConnection.initialiseDB();
        Statement s = con.createStatement();
        // String sqlStr = "create table test (id SERIAL PRIMARY KEY,familyname varchar(128),givenname varchar(128),phonenumber varchar(32));";
        s.executeUpdate(reqBody);
        s.close();
        con.close();

        resp.setContentType("text/html");
        resp.getWriter().write("You have successfully modified the DB");
    }

    // Change the following to a post request
    protected void processGETRequest(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            Connection con = DBConnection.initialiseDB();
            Statement s = con.createStatement();
            String sqlStr = "create table test (id SERIAL PRIMARY KEY,familyname varchar(128),givenname varchar(128),phonenumber varchar(32));";
            s.executeUpdate(sqlStr);
            s.close();
            con.close();
        }
        catch (Exception e) {
            System.out.println("There was a problem");
        }
    }
}
