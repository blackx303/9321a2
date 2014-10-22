package logic;
import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
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
       
    /**
     * @throws ServletException 
     * @throws SQLException 
     * @see HttpServlet#HttpServlet()
     */
    public Controller() throws ServletException, SQLException {
    	// TODO Auto-generated constructor stub
        super();
        searchs = new SearchDAODerbyImpl();
        
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		processRequest(request,response);
	}


	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("post reuest");
		processRequest(request, response);
	}

	private void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String forwardPage = "";
		String urlPattern = request.getServletPath();
		System.out.println(urlPattern);
		
		if (urlPattern.equals("/search")) {
			String query = request.getParameter("search");
			request.setAttribute("search", query);
			ArrayList<MovieDTO> results = searchs.getResults(query);
			request.setAttribute("results", results);
			request.setAttribute("showing", results.size());
			forwardPage = "search.jsp";
		} else if(urlPattern.equals("/details")) {
			handleDetailsPage(request, response);
			forwardPage = "details.jsp";
		} else if(urlPattern.equals("/review")) {
			handlePostReview(request, response);
			forwardPage = "details.jsp";
		} 
		
		RequestDispatcher rd = request.getRequestDispatcher("/"+forwardPage);
		 rd.forward(request, response);
	}
	
	private void handleDetailsPage(HttpServletRequest request, HttpServletResponse response) {
		String title = request.getParameter("title");
		Date releaseDate = java.sql.Date.valueOf(request.getParameter("releaseDate"));
		MovieDTO movie = searchs.getMovie(title, releaseDate);
		ArrayList<ReviewDTO> reviews = searchs.getReviews(title, releaseDate);
		request.setAttribute("movie", movie);
		request.setAttribute("reviews", reviews);
	}
	
	private void handlePostReview(HttpServletRequest request, HttpServletResponse response) {
		String title = request.getParameter("title");
		Date releaseDate = java.sql.Date.valueOf(request.getParameter("releaseDate"));
		String user = (String) request.getSession().getAttribute("login");
		int rating = Integer.parseInt(request.getParameter("rating"));
		String reviewText = request.getParameter("reviewText");
		ReviewDTO review = new ReviewDTO(title,releaseDate,user,rating,reviewText);
		
		searchs.storeReview(review);
		handleDetailsPage(request, response);
	}

}
