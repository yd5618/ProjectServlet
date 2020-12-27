import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.stream.Collectors;

// Line below specifies how the servlet can be accessed basically ('/DBaccess') - can be accessed through more URL patterns if needed
@WebServlet(urlPatterns={"/DBaccess"},loadOnStartup = 1)

public class MainServlet extends HttpServlet {
    // doGet will handle GET requests of our client & give appropriate response (retrieve information from the DB) - change this
    // doPost will handle POST requests of our client & give appropriate response (add something to the DB/modify something in the DB)

    /*
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Get body of the request - this is not good practice => find another way to do this - GET should only return information specified by URI/URL
        // https://stackoverflow.com/questions/978061/http-get-with-request-body --> this gives more information on this
        String reqBody=req.getReader().lines().collect(Collectors.joining(System.lineSeparator()));

        try {
            // Connecting to the DB and querying what the POST request gave
            Connection con = DBConnection.initialiseDB();
            Statement s = con.createStatement();
            ResultSet rset = s.executeQuery(reqBody); // a ResultSet object is a table of data representing a database
            while(rset.next()) {
                System.out.println(rset.getString("brand"));
            }
            s.close();
            con.close();
        }
        catch (Exception e) {
            System.out.println("There was a problem");
        }

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
    } */

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Get the request body (SQL query to be executed)
        String reqBody=req.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
        // int nb;

        try {
            // Connecting to the DB and querying what the POST request gave
            Connection con = DBConnection.initialiseDB();
            Statement s = con.createStatement();
            s.executeUpdate(reqBody); // will it actually return an int with the nb of rows affected?
            s.close();
            con.close();
        }
        catch (Exception e) {
            System.out.println("There was a problem");
        }

        resp.setContentType("text/html");
        resp.getWriter().write("You have successfully modified the DB - this was your request: "+reqBody);
    }
}
