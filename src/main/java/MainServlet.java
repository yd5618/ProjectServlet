import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.stream.Collectors;

// Line below specifies how the servlet can be accessed basically ('/DBaccess') - can be accessed through more URL patterns if needed
@WebServlet(urlPatterns={"/DBaccess"},loadOnStartup = 1)

public class MainServlet extends HttpServlet {
    // doGet will handle GET requests of our client & give appropriate response (retrieve information from the DB)
    // doPost will handle POST requests of our client & give appropriate response (add something to the DB/modify something in the DB)

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Get body of the request - this is not good practice => find another way to do this - GET should only return information specified by URI/URL
        // https://stackoverflow.com/questions/978061/http-get-with-request-body --> this gives more information on this

        String query = "SELECT * FROM products;";

        String brand = "";
        String amount = "";
        String sprice = "";
        String pprice = "";
        String fullstock = "";
        String limitation = "";
        String description = "";
        String category = "";
        String currentstock = "";

        ArrayList<String> product = new ArrayList<String>();
        ArrayList<ArrayList> products = new ArrayList<ArrayList>();

        try {
            // Connecting to the DB and returning what is identified by the URL
            Connection con = DBConnection.initialiseDB();
            Statement s = con.createStatement();
            ResultSet rset = s.executeQuery(query); // a ResultSet object is a table of data representing a database
            // '.next()' moves cursor to the next row of the DB - loop iterates through result set

            /*
            while(rset.next()) {
                product.clear();

                brand = rset.getString(1);
                product.add(brand);
                amount = rset.getString(2);
                product.add(amount);
                sprice = rset.getString(3);
                product.add(sprice);
                pprice = rset.getString(4);
                product.add(pprice);
                fullstock = rset.getString(5);
                product.add(fullstock);
                limitation = rset.getString(6);
                product.add(limitation);
                description = rset.getString(7);
                product.add(description);
                category = rset.getString(8);
                product.add(category);
                currentstock = rset.getString(10);
                product.add(currentstock);

                products.add(product);
            } */

            rset.next();
            product.add(rset.getString(2));
            products.add(product);
            product.clear();

            rset.absolute(3);
            product.add(rset.getString(2));
            products.add(product);

            rset.close();
            s.close();
            con.close();
        }
        catch (Exception e) {
            System.out.println("There was a problem");
        }

        Gson gson = new Gson();
        String jsonString = gson.toJson(products);

        // what do we do with req?
        resp.setContentType("application/json");
        resp.getWriter().write(jsonString); // this is where you return the information
        // req.getServletPath(); returns the ServletPath of the URL
    }

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
