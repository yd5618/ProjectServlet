import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import static org.mockito.Mockito.when;
import static org.junit.Assert.assertEquals;

public class TestAccessServlet {
    // Creating two mock objects (request, response)
    @Mock
    HttpServletRequest request;
    @Mock
    HttpServletResponse response;
    @Before
    public void setUp() throws Exception {
        // Initialising all mock objects
        MockitoAnnotations.initMocks(this);
    }
    @Test
    public void testDoGet() throws IOException, ServletException {
        // Things to test include .getParameter(), the whole thing with rset maybe, the getWriter() thing that sends the jsonString
        when(request.getParameter("item")).thenReturn("clients"); // now need to check that indeed returns good stuff

        StringWriter stringWriter = new StringWriter(); 
        PrintWriter printWriter = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(printWriter);

        // Calling the doGet() method that we want to test
        AccessServlet myServlet = new AccessServlet();
        myServlet.doGet(request, response);

        String result = stringWriter.getBuffer().toString().trim();
        assertEquals(result, new String("")); // here you should compare the output with the desired one which is not that
        // you should compare with what you should get from DB???

        assertEquals("application/json", response.getContentType());
    }

    @Test
    public void testDoPost() {
        // test req.getReader() and the resp.getWriter as well and the whole thing with our statement s maybe?
    }
}