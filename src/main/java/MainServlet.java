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

        // 'products' will contain ArrayLists of Strings that each correspond to one product (each ArrayList)
        ArrayList<ArrayList> products = new ArrayList<ArrayList>();
        ArrayList<Product> products_bis  = new ArrayList<>();

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

                String brand = rset.getString(1);
                String amount = rset.getString(2);
                double sprice = rset.getDouble(3);
                double pprice = rset.getDouble(4);
                int fullstock = rset.getInt(5);
                boolean limitation = rset.getBoolean(6);
                String description = rset.getString(7);
                String category = rset.getString(8);
                int id = rset.getInt(9);
                int currentstock = rset.getInt(10);

                Product product_bis = new Product(brand, amount, sprice, pprice, fullstock, limitation, description, category, id, currentstock);

                int i = 1;
                // Iterate through the columns
                while(i <= colcount) {
                    product.add(rset.getString(i++));
                }

                // Add one of the products (represented by an ArrayList) to the bigger collection of all products
                products_bis.add(product_bis);
                products.add(product);
            }

            // here need to attach information as attribute to request - only if certain parameter

            req.setAttribute("products_list",products_bis);
            RequestDispatcher dispatcher = req.getRequestDispatcher("index.jsp"); // not sure about the "index.jsp"
            dispatcher.forward(req,resp);

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
