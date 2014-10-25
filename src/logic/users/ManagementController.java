package logic.users;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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
import javax.servlet.http.Part;

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
import jdbc.management.MoviePosterDTO;
import jdbc.management.ScreeningDAO;
import jdbc.management.ScreeningDAODerbyImpl;
import jdbc.management.ScreeningDTO;

/**
 * Servlet implementation class ManagementController
 */
@WebServlet(urlPatterns = {"/manage", "/cinema", "/movie", "/screening", "/poster"})
@MultipartConfig
public class ManagementController extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private AmenityDAO amenities;
    private CinemaDAO cinemas;
    private MovieDAO movies;
    private ScreeningDAO screenings;
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
            this.screenings = new ScreeningDAODerbyImpl();
            this.ageRatings = new AgeRatingDAODerbyImpl();
            this.genres = GenreDAODerbyImpl.get();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    String login = (String) request.getSession().getAttribute("login");

        String urlPattern = request.getServletPath();
	    if("admin".equals(login) || urlPattern.equals("/poster")) {
    		
    		if(urlPattern.equals("/manage")) {
    		    request.getRequestDispatcher("WEB-INF/manage/manage.jsp").forward(request, response);
    		} else if(urlPattern.equals("/cinema")) {
    		    loadCinemaPage(request, response);
    		} else if(urlPattern.equals("/movie")) {
    		    loadMoviePage(request, response);
    		} else if(urlPattern.equals("/screening")) {
    		    loadScreeningPage(request, response);
    		} else if(urlPattern.equals("/poster")) {
    		    logger.log(Level.INFO, "got request for img for movie(title:" + request.getParameter("t") + ";release:" + request.getParameter("r") + ";)");
    		    if(request.getParameter("t") != null && request.getParameter("r") != null) {
    		        servePoster(request, response);
    		    }
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
	        } else if(urlPattern.equals("/screening")) {
	            if(request.getParameter("movie") != null &&
	                    request.getParameter("cinema") != null &&
	                    request.getParameter("screeningdate") != null &&
	                    request.getParameter("screeningtime") != null) {
	                attemptAddScreening(request);
	            }
	            response.sendRedirect("screening");
	        } else {
	            request.getRequestDispatcher(urlPattern).forward(request, response);
	        }
	    } else {
	        response.sendRedirect("home");
	    }
	}

    private void attemptAddScreening(HttpServletRequest request) {
        String movieStr = request.getParameter("movie");
        String cinema = request.getParameter("cinema");
        String dateStr = request.getParameter("screeningdate");
        String timeStr = request.getParameter("screeningtime");
        System.out.println("Attempting to add screening for " 
                + movieStr + " at " + cinema + " starting at " + dateStr + " " + timeStr);
        try {
            Date screeningTime = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse((dateStr + " " + timeStr));
            String[] movieId = movieStr.split(";");
            String title = movieId[1];
            Date releaseDate = new SimpleDateFormat("yyyy-MM-dd").parse(movieId[0]);
            
            ScreeningDTO screening = new ScreeningDTO(title, releaseDate, cinema, screeningTime);
            
            screenings.create(screening);
        } catch (ParseException e) {
            e.printStackTrace();
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
            String title = request.getParameter("title");
            logger.log(Level.INFO, "date provided for addMovie is " + request.getParameter("releasedate"));
            Date releaseDate = new SimpleDateFormat("yyyy-MM-dd").parse(request.getParameter("releasedate"));
            Part posterPart = request.getPart("poster");
            String fname = posterPart.getHeader("content-disposition").split("filename=\"")[1].split("\"")[0];
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
            
            String mimetype = "image/jpg";
            if(fname.endsWith(".png")) {
                mimetype = "image/png";
            }
            //get poster data
            ByteArrayOutputStream posterByteStream = new ByteArrayOutputStream();
            InputStream posterInStream = posterPart.getInputStream();
            int read;
            byte[] bytes = new byte[1024];
            while((read = posterInStream.read(bytes)) != -1) {
                posterByteStream.write(bytes, 0, read);
            }
            byte[] posterData = posterByteStream.toByteArray();
            
            MovieDTO add = new MovieDTO(title, releaseDate, ageRating, genresChecked, director, actors, synopsis);
            MoviePosterDTO poster = new MoviePosterDTO(title, releaseDate, mimetype, posterData);//TODO fix mimetype
            logger.log(Level.INFO, "adding movie with title " + add.getTitle());
            movies.create(add, poster);
        } catch(ParseException p) {
            logger.log(Level.INFO, "Received bad date from client: " + request.getParameter("releasedate"));
        } catch (IOException e) {
            e.printStackTrace();
            logger.log(Level.WARNING, "problem parsing image");
        } catch (IllegalStateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ServletException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
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
        request.setAttribute("genres", genres.findAll());
        request.getRequestDispatcher("WEB-INF/manage/movie.jsp").forward(request, response);
    }
    
    private void loadScreeningPage(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("screenings", screenings.findAll());
        request.setAttribute("movies", movies.findAllNowShowing());
        request.setAttribute("cinemas", cinemas.findAll());
        request.getRequestDispatcher("WEB-INF/manage/screening.jsp").forward(request, response);
    }

    private void servePoster(HttpServletRequest request,
            HttpServletResponse response) {
        try {
            String title = request.getParameter("t");
            Date release = new SimpleDateFormat("yyyy-MM-dd").parse(request.getParameter("r"));
            
            MoviePosterDTO poster = movies.findMoviePoster(title, release);
            if(poster != null) {
                response.setContentType(poster.getMimeType());
                OutputStream out = response.getOutputStream();
                ByteArrayInputStream bytesIn = new ByteArrayInputStream(poster.getData());
                int read;
                byte[] b = new byte[1024];
                while((read = bytesIn.read(b)) != -1) {
                    out.write(b, 0, read);
                }
                
            }
        } catch (ParseException p) {
            try {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}