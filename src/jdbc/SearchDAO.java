import java.util.ArrayList;


public interface SearchDAO {
	
	public ArrayList<MovieDTO> getResults(String query);

}
