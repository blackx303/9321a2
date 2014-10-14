package logic.users;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jdbc.management.AmenityDAO;
import jdbc.management.AmenityDAODerbyImpl;
import jdbc.management.CinemaDAO;
import jdbc.management.CinemaDAODerbyImpl;

/**
 * Servlet implementation class ManagementController
 */
@WebServlet(urlPatterns = {"/manage", "/cinema"})
public class ManagementController extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private AmenityDAO amenities;
    private CinemaDAO cinemas;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ManagementController() {
        super();
        try {
            this.amenities = new AmenityDAODerbyImpl();
            this.cinemas = new CinemaDAODerbyImpl();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    String login = (String) request.getSession().getAttribute("login");
	    
	    if("admin".equals(login)) {
    		String urlPattern = request.getServletPath();
    		
    		if(urlPattern.equals("/manage")) {
    		    request.getRequestDispatcher("WEB-INF/manage/manage.jsp").forward(request, response);
    		} else if(urlPattern.equals("/cinema")) {
    		    loadCinemaPage(request, response);
    		} else {
    		    System.out.println("url pattern:" + urlPattern);
    		}
	    } else {
	        response.sendRedirect("home");
	    }
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    String login = (String) request.getSession().getAttribute("login");
	    
	    if("admin".equals(login)) {
	        String urlPattern = request.getServletPath();
	        
	        if(urlPattern.equals("/cinema")) {
	            
	            loadCinemaPage(request, response);
	        } else {
	            request.getRequestDispatcher(urlPattern).forward(request, response);
	        }
	    } else {
	        response.sendRedirect("home");
	    }
	}

    private void loadCinemaPage(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("amenitytypes", amenities.findAllTypes());
        request.setAttribute("cinemas", cinemas.findAll());
        request.getRequestDispatcher("WEB-INF/manage/cinema.jsp").forward(request, response);
    }

}
