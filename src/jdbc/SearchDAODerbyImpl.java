package jdbc;

import java.util.ArrayList;
import java.sql.Date;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import jdbc.management.MovieDTO;



public class SearchDAODerbyImpl implements SearchDAO{
	
	Connection conn;
	
	public SearchDAODerbyImpl() throws SQLException {
        this.conn = DBConnFactory.getConnection();
	}

	@Override
	public ArrayList<MovieDTO> getResults(String query) {
		ArrayList<MovieDTO> results = new ArrayList<MovieDTO>();
		String searchStatement = "SELECT * "
        		+ "FROM movie m "
        		+ "INNER JOIN movies_have_genres mg ON mg.title=m.title "
        		+ "WHERE LOWER(m.title) LIKE LOWER(?) "
        		+ "OR LOWER(mg.genre_title) LIKE (?)";
		
		try {
            PreparedStatement statement = conn.prepareStatement(searchStatement);
            statement.setString(1, "%" + query + "%");
            statement.setString(2, "%" + query + "%");
            ResultSet res = statement.executeQuery();
            while(res.next()){
				String title = res.getString("title");
				Date releaseDate = res.getDate("release_date");
				String genre = res.getString("genre_title");
				System.out.println(releaseDate);
				MovieDTO movie = new MovieDTO(title,releaseDate);
				movie.setGenre(genre);
				results.add(movie);
            }

        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
		return results;
	}

}
