package jdbc.management;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import jdbc.GenericDAODerbyImpl;

public class MovieDAODerbyImpl extends GenericDAODerbyImpl implements MovieDAO {

    public MovieDAODerbyImpl() throws SQLException {
        super();
    }

    @Override
    public List<MovieDTO> findAll() {
        List<MovieDTO> movies = new ArrayList<MovieDTO>();
        
        try {
            PreparedStatement allMoviesQ = conn.prepareStatement("SELECT title, release_date, age_rating, director, actors, synopsis FROM movies");
            ResultSet allMovies = allMoviesQ.executeQuery();
            
            while(allMovies.next()) {
                String title = allMovies.getString(1);
                Date release_date = allMovies.getDate(2);
                String age_rating = allMovies.getString(3);
                String director = allMovies.getString(4);
                String actors = allMovies.getString(5);
                String synopsis = allMovies.getString(6);
                List<String> genres = new ArrayList<String>();
                
                PreparedStatement movieGenresQ = conn.prepareStatement("SELECT genre_title "
                        + "FROM movies_have_genres WHERE title = ? and release_date = ?");
                movieGenresQ.setString(1, title);
                movieGenresQ.setDate(2, release_date);
                
                ResultSet mGenres = movieGenresQ.executeQuery();
                while(mGenres.next()) {
                    genres.add(mGenres.getString(1));
                }
                
                MovieDTO m = new MovieDTO(title, release_date, age_rating, genres, director, actors, synopsis);
                movies.add(m);
                
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return movies;
    }

}
