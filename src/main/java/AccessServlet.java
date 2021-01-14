import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.stream.Collectors;

@WebServlet(urlPatterns={"/access"},loadOnStartup = 1)

public class AccessServlet extends HttpServlet {
    // doGet will handle GET requests of our client & give appropriate response (retrieve information from the DB)
    // doPost will handle POST requests of our client & give appropriate response (add something to the DB/modify something in the DB)

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String param = req.getParameter("item");
        // String nb = req.getParameter("nb");
        // String query="";
        // String query = "SELECT * FROM products;";

        String query = "SELECT * FROM "+param+";";
        /*
        if (item_param == "clients") {
            query = "SELECT * FROM "+item_param+";";
        }
        else if(nb == "1") {
            query = "SELECT * FROM products;";
        }
        else if(nb == "2") {
            query = "SELECT * FROM products2;";
        }
        else if(nb == "2") {
            query = "SELECT * FROM products3;";
        }
        */

        // 'clients' will contain ArrayLists of Strings that each correspond to one client (each ArrayList)
        ArrayList<ArrayList> items = new ArrayList<ArrayList>();

        try {
            // Connecting to the DB and returning what is identified by the URL
            Connection con = DBConnection.initialiseDB();
            Statement s = con.createStatement();
            ResultSet rset = s.executeQuery(query); // a ResultSet object is a table of data representing a database
            // '.next()' moves cursor to the next row of the DB - loop iterates through result set

            // Get number of columns for the table in the DB
            ResultSetMetaData rsmd = rset.getMetaData();
            int colcount = rsmd.getColumnCount();

            // this may be useless?

            // Iterate through the rows
            while(rset.next()) {
                ArrayList<String> item = new ArrayList<>();

                int i = 1;
                // Iterate through the columns
                while(i <= colcount) {
                    item.add(rset.getString(i++));
                }

                // Add one of the clients (represented by an ArrayList) to the bigger collection of all clients
                items.add(item);
            }

            // Close everything manually
            rset.close();
            s.close();
            con.close();
        }
        catch (Exception e) {
            System.out.println("There was a problem");
        }

        // 'Converting' to JSON
        Gson gson = new Gson();
        String jsonString = gson.toJson(items);

        resp.setContentType("application/json");
        resp.getWriter().write(jsonString); // this is where you return the information --> here in JSON format
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Get the request body (SQL query to be executed)
        String reqBody=req.getReader().lines().collect(Collectors.joining(System.lineSeparator()));

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
