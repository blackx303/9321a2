package logic.users;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.DatatypeConverter;

import jdbc.users.PendingUserDTO;
import jdbc.users.UserDAO;
import jdbc.users.UserDTO;
import jdbc.users.ViewerDTO;

@WebServlet(urlPatterns = {"/", "/home", "/register", "/login", "/logout", "/confirm"})
public class UserController extends HttpServlet {
    private UserDAO users;
    
    public UserController() {
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
        
        if(urlPattern.equals("/") || urlPattern.equals("/home")) {
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
            resp.sendRedirect("");
        } else if(urlPattern.equals("/confirm")) {
            if(req.getParameter("u") != null &&
                    req.getParameter("k") != null) {
                if(attemptRegistrationConfirmation(req, req.getParameter("u"),
                        req.getParameter("k"))) {
                    //if successful confirmation
                    RequestDispatcher rd = req.getRequestDispatcher("/profile");
                    rd.forward(req, resp);
                } else {
                    RequestDispatcher rd = req.getRequestDispatcher("/badconfirm.jsp");
                    rd.forward(req, resp);
                }
            } else {
                req.getRequestDispatcher("/").forward(req, resp);
            }
            
        } else if(urlPattern.equals("/profile")) {
            if(req.getSession().getAttribute("login") != null) {
                ViewerDTO user = users.findNormalUser((String)req.getSession().getAttribute("login"));
                
                if(user != null) {
                    //ie if it is a viewer_account
                    req.setAttribute("nickname", user.getNickname());
                    req.setAttribute("email", user.getEmail());
                    req.setAttribute("firstname", user.getFirstName());
                    req.setAttribute("lastname", user.getLastName());
                    
                    req.getRequestDispatcher("profile.jsp").forward(req, resp);
                } else {
                    req.getRequestDispatcher("home.jsp").forward(req, resp);
                }
            } else {
                req.getRequestDispatcher("home.jsp").forward(req, resp);
            }
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
        
        if(urlPattern.equals("/login")) {
            
            if(req.getParameter("username") != null &&
                    req.getParameter("password") != null) {
                if(attemptLogin(req, resp)) {
                    RequestDispatcher rd = req.getRequestDispatcher("/");
                    rd.forward(req, resp);
                } else {
                    req.setAttribute("invalid", req.getParameter("username"));
                    RequestDispatcher rd = req.getRequestDispatcher("login.jsp");
                    rd.forward(req, resp);
                }
            } else {
                RequestDispatcher rd = req.getRequestDispatcher("login.jsp");
                rd.forward(req, resp);
            }
        } else if(urlPattern.equals("/register")) {            
            if(req.getParameter("username") != null &&
                    req.getParameter("password") != null &&
                    req.getParameter("email") != null) {
                if(attemptRegistration(req, resp)) {
                    resp.sendRedirect("");
                } else {
                    RequestDispatcher rd = req.getRequestDispatcher("register.jsp");
                    rd.forward(req, resp);
                }
            } else {
                RequestDispatcher rd = req.getRequestDispatcher("register.jsp");
                rd.forward(req, resp);
            }
        } else if(urlPattern.equals("/profile")) {
            if(req.getSession().getAttribute("login") != null &&
                    req.getParameter("nickname") != null &&
                    req.getParameter("email") != null &&
                    req.getParameter("firstname") != null &&
                    req.getParameter("lastname") != null) {
                ViewerDTO user = users.findNormalUser((String)req.getSession().getAttribute("login"));
                
                user.setNickname((String)req.getParameter("nickname"));
                user.setEmail((String)req.getParameter("email"));
                user.setFirstName((String)req.getParameter("firstname"));
                user.setLastName((String)req.getParameter("lastname"));
                
                users.storeViewer(user);
                
                req.setAttribute("nickname", req.getParameter("nickname"));
                req.setAttribute("email", req.getParameter("email"));
                req.setAttribute("firstname", req.getParameter("firstname"));
                req.setAttribute("lastname", req.getParameter("lastname"));
                
                resp.sendRedirect("profile");
            } else {
                req.getRequestDispatcher("/").forward(req, resp);
            }
        } else {
            doGet(req, resp);//shouldn't really be POST'ing unless one of the above situations
            return;
        }
        
    }

    private boolean attemptRegistration(HttpServletRequest req, HttpServletResponse resp) {
        boolean successful = true;
        String username = (String) req.getParameter("username");
        String password = (String) req.getParameter("password");
        String email = (String) req.getParameter("email");
        System.out.println("Attempting to register user \"" + username + "\" with pass \"" + password + "\" with email \"" + email + "\"");
        
        if(users.findUser(username) != null) {
            successful = false;
            req.setAttribute("usertaken", username);
        }
        
        if(! username.toLowerCase().matches("[a-z][a-z0-9_]{4,15}")) {
            successful = false;
            req.setAttribute("badusername", username);
        }
        
        if(!(email.contains("@"))) {//TODO this is just a basic check
            successful = false;
            req.setAttribute("bademail", email);
        }
        
        if(successful) {
            username = username.toLowerCase();
            
            //createUser(username, password, email);
            //verification link should be /confirm?k=baacdfe12fea31a&u=user
            byte[] salt = generateSalt();
            String saltString = DatatypeConverter.printHexBinary(salt);
            byte[] hash = hashPassword(password, salt);
            String hashString = DatatypeConverter.printHexBinary(hash);
            
            String confirmationKey = users.createPending(username, saltString, hashString, email);
            //TODO send a verification link
            System.out.println("\"" + username + "\" can activate their account by visiting /confirm?k=" + confirmationKey + "&u=" + username);
            
            System.out.println("Successfully registered \"" + username + "\" (pending confirmation).");
        } else {
            System.out.println("Unsuccessful in registering \"" + username + "\".");
        }
        return successful;
    }

    private boolean attemptRegistrationConfirmation(HttpServletRequest req, String username, String confirmationKey) {
        boolean success = false;
        PendingUserDTO user = users.findPendingUser(username);
        
        if(user != null) {
            if(user.getConfirmationKey().equals(confirmationKey)) {
                users.confirmPendingUser(username);
                
                req.getSession().setAttribute("login", username);
                success = true;
            }
        }
        
        return success;
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
    
    private byte[] generateSalt() {
        byte[] salt = new byte[32];
        
        SecureRandom r = new SecureRandom();
        r.nextBytes(salt);
        
        return salt;
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
