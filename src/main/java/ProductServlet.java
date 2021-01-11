import com.google.gson.Gson;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet(urlPatterns={"/products"},loadOnStartup = 1)

public class ProductServlet extends HttpServlet {
    // doGet will handle GET requests of our client & give appropriate response (retrieve information from the DB)
    // doPost will handle POST requests of our client & give appropriate response (add something to the DB/modify something in the DB)

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Get body of the request - this is not good practice => find another way to do this - GET should only return information specified by URI/URL
        // https://stackoverflow.com/questions/978061/http-get-with-request-body --> this gives more information on this

        String query = "SELECT * FROM products;";

        // 'products' will contain ArrayLists of Strings that each correspond to one product (each ArrayList)
        ArrayList<ArrayList> products = new ArrayList<ArrayList>();
        // ArrayList<Product> products_bis  = new ArrayList<>();

        try {
            // Connecting to the DB and returning what is identified by the URL
            Connection con = DBConnection.initialiseDB();
            Statement s = con.createStatement();
            ResultSet rset = s.executeQuery(query); // a ResultSet object is a table of data representing a database
            // '.next()' moves cursor to the next row of the DB - loop iterates through result set

            // Get number of columns for the table in the DB
            ResultSetMetaData rsmd = rset.getMetaData();
            int colcount = rsmd.getColumnCount();

            products.clear(); // this may be useless?

            // Iterate through the rows
            while(rset.next()) {
                ArrayList<String> product = new ArrayList<>();

                int i = 1;
                // Iterate through the columns
                while(i <= colcount) {
                    product.add(rset.getString(i++));
                }

                // Add one of the products (represented by an ArrayList) to the bigger collection of all products
                products.add(product);
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
        String jsonString = gson.toJson(products);

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
