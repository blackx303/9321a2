package logic;
import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jdbc.ReviewDTO;
import jdbc.SearchDAO;
import jdbc.SearchDAODerbyImpl;
import jdbc.management.GenreDAO;
import jdbc.management.GenreDAODerbyImpl;
import jdbc.management.MovieDTO;
import jdbc.management.MovieDAODerbyImpl;
import jdbc.users.UserDTO;


/**
 * Servlet implementation class Controller
 */
@WebServlet(urlPatterns = {"/search", "/details", "/review"})
public class Controller extends HttpServlet {
	private static final long serialVersionUID = 1L;
	static Logger logger = Logger.getLogger(Controller.class.getName());
	private SearchDAO searchs;
    private GenreDAO genres;
       
    /**
     * @throws ServletException 
     * @throws SQLException 
     * @see HttpServlet#HttpServlet()
     */
    public Controller() throws ServletException, SQLException {
    	// TODO Auto-generated constructor stub
        super();
        searchs = SearchDAODerbyImpl.get();
        genres = GenreDAODerbyImpl.get();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String forwardPage = "";
		String urlPattern = request.getServletPath();
		System.out.println(urlPattern);
		
		if (urlPattern.equals("/search")) {
			String query = request.getParameter("search");
			request.setAttribute("search", query);
			
			Enumeration<String> paramNames = request.getParameterNames();
            List<String> genresChecked = new ArrayList<String>();
            while(paramNames.hasMoreElements()) {
                String name = paramNames.nextElement();
                if(name.startsWith("genre_")) {
                    String genre = name.replaceFirst("genre_", "");
                    genresChecked.add(genre);
                }
            }
            genresChecked.retainAll(genres.findAll());
			ArrayList<MovieDTO> results = searchs.getResults(query, genresChecked);
			request.setAttribute("results", results);
			request.setAttribute("showing", results.size());
			forwardPage = "search.jsp";
		} else if(urlPattern.equals("/details")) {
			handleDetailsPage(request, response);
			forwardPage = "details.jsp";
		}
		RequestDispatcher rd = request.getRequestDispatcher("/"+forwardPage);
		 rd.forward(request, response);
	}


	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String urlPattern = request.getServletPath();

		if(urlPattern.equals("/review")) {
			handlePostReview(request, response);
		}
	}

	
	private void handleDetailsPage(HttpServletRequest request, HttpServletResponse response) {
		String title = request.getParameter("title");
		Date releaseDate = java.sql.Date.valueOf(request.getParameter("releaseDate"));
		MovieDTO movie = searchs.getMovie(title, releaseDate);
		ArrayList<ReviewDTO> reviews = searchs.getReviews(title, releaseDate);
		request.setAttribute("movie", movie);
		request.setAttribute("reviews", reviews);
	}
	
	private void handlePostReview(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String title = request.getParameter("title");
		Date releaseDate = java.sql.Date.valueOf(request.getParameter("releaseDate"));
		System.out.println(title+releaseDate);
		String user = (String) request.getSession().getAttribute("login");
		int rating = Integer.parseInt(request.getParameter("rating"));
		String reviewText = request.getParameter("reviewText");
		ReviewDTO review = new ReviewDTO(title,releaseDate,user,rating,reviewText);
		
		searchs.storeReview(review);
		MovieDTO movie = searchs.getMovie(title, releaseDate);
		ArrayList<ReviewDTO> reviews = searchs.getReviews(title, releaseDate);
		request.setAttribute("movie", movie);
		request.setAttribute("reviews", reviews);
		response.sendRedirect("details"+"?title="+title+"&releaseDate="+releaseDate);
	}

}
