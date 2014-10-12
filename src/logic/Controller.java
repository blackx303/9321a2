package logic;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns = {"/", "/register"})
public class Controller extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String urlPattern = req.getServletPath();
        
        if(urlPattern.equals("/")) {
            RequestDispatcher rd = req.getRequestDispatcher("home.jsp");
            rd.forward(req, resp);
        } else if(urlPattern.equals("/register")) {
            RequestDispatcher rd = req.getRequestDispatcher("register.jsp");
            rd.forward(req, resp);
        } else if(urlPattern.equals("/login")) {
            RequestDispatcher rd = req.getRequestDispatcher("login.jsp");
            rd.forward(req, resp);
        } else {
            //redirect to home page
            System.out.println("forwarding request for: \"" + urlPattern + "\" to home page");
            RequestDispatcher rd = req.getRequestDispatcher("home.jsp");
            rd.forward(req, resp);
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String urlPattern = req.getServletPath();
        String forwardPage = "home.jsp";
        
        if(urlPattern.equals("/login")) {
            forwardPage = "login.jsp";
            
            System.out.println("Attempted to login with username: \"" + req.getParameter("username") + "\"");
            System.out.println("\tand password: \"" + req.getParameter("password") + "\"");
        } else {
            doGet(req, resp);//shouldn't really be POST'ing unless one of the above situations
        }
        
        RequestDispatcher rd = req.getRequestDispatcher(forwardPage);
        rd.forward(req, resp);
    }

}
