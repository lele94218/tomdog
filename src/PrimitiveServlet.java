import javax.servlet.*;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author taoranxue on 6/27/17 7:48 PM.
 */
public class PrimitiveServlet implements Servlet {
    @Override
    public void init(ServletConfig servletConfig) throws ServletException {
        System.out.println("init");
    }

    @Override
    public ServletConfig getServletConfig() {
        return null;
    }

    @Override
    public void service(ServletRequest request, ServletResponse response) throws ServletException, IOException {
        System.out.println("from service");
        PrintWriter out = response.getWriter();
        response.setContentType("text/html");
        String errorMessage = "<h1>This is Primitive Servlet</h1>";
        out.print(errorMessage);
    }

    @Override
    public String getServletInfo() {
        return null;
    }

    @Override
    public void destroy() {
        System.out.println("destroy");
    }
}
