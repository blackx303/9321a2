package jdbc;

import java.util.ArrayList;

import jdbc.management.MovieDTO;


public interface SearchDAO {
	
	public ArrayList<MovieDTO> getResults(String query);

}
