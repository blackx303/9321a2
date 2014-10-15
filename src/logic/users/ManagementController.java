package logic.users;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jdbc.management.AgeRatingDAO;
import jdbc.management.AgeRatingDAODerbyImpl;
import jdbc.management.AmenityDAO;
import jdbc.management.AmenityDAODerbyImpl;
import jdbc.management.CinemaDAO;
import jdbc.management.CinemaDAODerbyImpl;
import jdbc.management.CinemaDTO;
import jdbc.management.MovieDAO;
import jdbc.management.MovieDAODerbyImpl;

/**
 * Servlet implementation class ManagementController
 */
@WebServlet(urlPatterns = {"/manage", "/cinema", "/movie"})
public class ManagementController extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private AmenityDAO amenities;
    private CinemaDAO cinemas;
    private MovieDAO movies;
    private AgeRatingDAO ageRatings;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ManagementController() {
        super();
        try {
            this.amenities = new AmenityDAODerbyImpl();
            this.cinemas = new CinemaDAODerbyImpl();
            this.movies = new MovieDAODerbyImpl();
            this.ageRatings = new AgeRatingDAODerbyImpl();
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
    		} else if(urlPattern.equals("/movie")) {
    		    loadMoviePage(request, response);
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
	            if(request.getParameter("location") != null &&
	                    request.getParameter("capacity") != null) {
	                attemptAddCinema(request);
	            }
	            response.sendRedirect("cinema");
	        } else {
	            request.getRequestDispatcher(urlPattern).forward(request, response);
	        }
	    } else {
	        response.sendRedirect("home");
	    }
	}

    private void attemptAddCinema(HttpServletRequest request) {
        String location = request.getParameter("location");
        try {
            int capacity = Integer.parseInt(request.getParameter("capacity"));
            capacity = Math.max(1, capacity);
            Set<String> amenitiesChecked = new HashSet<String>();
            
            //get list of amenities
            Enumeration<String> paramNames = request.getParameterNames();
            while(paramNames.hasMoreElements()) {
                String name = paramNames.nextElement();
                if(name.startsWith("amenity_")) {
                    String amenity = name.replaceFirst("amenity_", "");
                    System.out.println("got amenity " + amenity);
                    amenitiesChecked.add(amenity);
                }
            }
            
            //filter by legit amenities
            amenitiesChecked.retainAll(amenities.findAllTypes());
            
            CinemaDTO newCinema = new CinemaDTO(location, capacity, amenitiesChecked);
            cinemas.createCinema(newCinema);
            
        } catch(NumberFormatException n) {
            //We got invalid input;
            System.out.println("invalid capacity specified " + request.getParameter("capacity"));
        }
    }

    private void loadCinemaPage(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("amenitytypes", amenities.findAllTypes());
        request.setAttribute("cinemas", cinemas.findAll());
        request.getRequestDispatcher("WEB-INF/manage/cinema.jsp").forward(request, response);
    }

    private void loadMoviePage(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("movies", movies.findAll());
        request.setAttribute("ageratings", ageRatings.findAll());
        request.getRequestDispatcher("WEB-INF/manage/movie.jsp").forward(request, response);
    }
}