package jdbc;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import jdbc.management.MovieDTO;


public interface SearchDAO {
	
	public ArrayList<MovieDTO> getResults(String query);
	
	public MovieDTO getMovie(String title, Date releaseDate);
	
	public ArrayList<ReviewDTO> getReviews(String title, Date releaseDate);
	
	public void storeReview(ReviewDTO review);
	
	public ArrayList<MovieDTO> getNowShowing();
	
	public ArrayList<MovieDTO> getComingSoon();

    public ArrayList<MovieDTO> getResults(String query,
            List<String> withinGenres);
	
	


}
