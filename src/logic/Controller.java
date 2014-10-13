package logic;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jdbc.SearchDAO;
import jdbc.SearchDAODerbyImpl;


/**
 * Servlet implementation class Controller
 */
@WebServlet(urlPatterns="/controller",displayName="Controller")
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
		processRequest(request, response);
	}

	private void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String forwardPage = "";
		String action = request.getParameter("action");
		
		if (action.equals("login")) {
			forwardPage = "home.jsp";
		} else if (action.equals("search")) {
			String query = request.getParameter("search");
			request.setAttribute("search", query);
			request.setAttribute("results", searchs.getResults(query));
			System.out.println(searchs.getResults(query).size());
			forwardPage = "search.jsp";
		} else if (action.equals("details")) {
			request.setAttribute("title", request.getParameter("title"));
			forwardPage = "details.jsp";
		}
		
		RequestDispatcher rd = request.getRequestDispatcher("/"+forwardPage);
		 rd.forward(request, response);
	}

}
