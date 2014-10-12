package logic;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.DatatypeConverter;

import jdbc.UserDAO;
import jdbc.UserDTO;

@WebServlet(urlPatterns = {"/", "/register", "/login", "/logout"})
public class Controller extends HttpServlet {
    private UserDAO users;
    
    public Controller() {
        super();
        try {
            users = new UserDAODerbyImpl();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
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
        } else if(urlPattern.equals("/logout")) {
            logout(req);
            RequestDispatcher rd = req.getRequestDispatcher("/");
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
            
            if(req.getParameter("username") != null &&
                    req.getParameter("password") != null) {
                if(attemptLogin(req, resp)) {
                    forwardPage = "/";
                } else {
                    req.setAttribute("invalid", req.getParameter("username"));
                }
            }
        } else {
            doGet(req, resp);//shouldn't really be POST'ing unless one of the above situations
            return;
        }
        
        RequestDispatcher rd = req.getRequestDispatcher(forwardPage);
        rd.forward(req, resp);
    }

    private boolean attemptLogin(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("Attempted to login with username: \"" + req.getParameter("username") + "\"");
        System.out.println("\tand password: \"" + req.getParameter("password") + "\"");
        
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        boolean authenticated = false;
        
        UserDTO user = users.findUser(username);
        
        if(user != null) {
            byte[] saltBytes = user.getSalt();
            byte[] storedHash = user.getPasswordAndSaltHash();
            
            authenticated = authenticate(password, saltBytes, storedHash);
        }
        
        if(authenticated) {
            System.out.println(username + " successfully logged in");
            req.getSession().setAttribute("login", username);
        } else {
            System.out.println("Login failed.");
        }
        
        return authenticated;
    }

    private boolean authenticate(String password, byte[] saltBytes, byte[] storedHash) {
        boolean goodLogin = true;
        
        byte[] loginHash = hashPassword(password, saltBytes);
        
        for(int i = 0; i < storedHash.length; ++i) {
            if(storedHash[i] != loginHash[i]) {
                goodLogin = false;
            }
        }
        
        
        return goodLogin;
    }

    private byte[] hashPassword(String password, byte[] saltBytes) {
        byte[] loginHash = null;
        
        try{
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            digest.reset();
            digest.update(saltBytes);
        
            loginHash = digest.digest(password.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            System.err.println("App depends on presence of SHA256 algorithm");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            System.err.println("App requires UTF8 support");
        }
        
        return loginHash;
    }
    
    private void logout(HttpServletRequest req) {
        String username = (String) req.getSession().getAttribute("login");
        
        System.out.print("Logging out");
        if(username != null) {
            System.out.print(" " + username);
            req.getSession().setAttribute("login", null);
        }
        
        System.out.println(".");
    }

}
