package logic.users;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
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
import jdbc.management.GenreDAO;
import jdbc.management.GenreDAODerbyImpl;
import jdbc.management.MovieDAO;
import jdbc.management.MovieDAODerbyImpl;
import jdbc.management.MovieDTO;

/**
 * Servlet implementation class ManagementController
 */
@WebServlet(urlPatterns = {"/manage", "/cinema", "/movie"})
@MultipartConfig
public class ManagementController extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private AmenityDAO amenities;
    private CinemaDAO cinemas;
    private MovieDAO movies;
    private AgeRatingDAO ageRatings;
    private GenreDAO genres;
    private final static Logger logger = 
            Logger.getLogger(ManagementController.class.getCanonicalName());
       
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
            this.genres = new GenreDAODerbyImpl();
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
    		    logger.log(Level.WARNING, "url pattern:" + urlPattern);
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
	        } else if(urlPattern.equals("/movie")) {
	            if(request.getParameter("title") != null &&
                        request.getParameter("releasedate") != null &&
                        request.getPart("poster") != null &&
                        request.getParameter("actors") != null &&
                        request.getParameter("director") != null &&
                        request.getParameter("agerating") != null &&
                        request.getParameter("synopsis") != null) {
	                attemptAddMovie(request);
                }
	            response.sendRedirect("movie");
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
                    amenitiesChecked.add(amenity);
                }
            }
            
            //filter by legit amenities
            amenitiesChecked.retainAll(amenities.findAllTypes());
            
            CinemaDTO newCinema = new CinemaDTO(location, capacity, amenitiesChecked);
            cinemas.createCinema(newCinema);
            
        } catch(NumberFormatException n) {
            //We got invalid input;
            logger.log(Level.INFO, "client specified invalid capacity of " + request.getParameter("capacity"));
        }
    }

    private void attemptAddMovie(HttpServletRequest request) {
        try {
            // TODO Auto-generated method stub
            String title = request.getParameter("title");
            logger.log(Level.INFO, "date provided for addMovie is " + request.getParameter("releasedate"));
            Date releaseDate = new SimpleDateFormat("yyyy-MM-dd").parse(request.getParameter("releasedate"));
            //TODO poster
            String actors = request.getParameter("actors");
            List<String> genresChecked = new ArrayList<String>();
            String director = request.getParameter("director");
            String ageRating = request.getParameter("agerating");
            String synopsis = request.getParameter("synopsis");
            
            Enumeration<String> paramNames = request.getParameterNames();
            while(paramNames.hasMoreElements()) {
                String name = paramNames.nextElement();
                if(name.startsWith("genre_")) {
                    String genre = name.replaceFirst("genre_", "");
                    System.out.println("got genre " + genre);
                    genresChecked.add(genre);
                }
            }
            
            genresChecked.retainAll(genres.findAll());
            
            MovieDTO add = new MovieDTO(title, releaseDate, ageRating, genresChecked, director, actors, synopsis);
            logger.log(Level.INFO, "adding movie with title " + add.getTitle());
            movies.create(add);
        } catch(ParseException p) {
            logger.log(Level.INFO, "Received bad date from client: " + request.getParameter("releasedate"));
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