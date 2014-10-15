package jdbc.management;

import java.sql.Blob;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

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

    @Override
    public boolean create(MovieDTO movie, MoviePosterDTO poster) {
        boolean success = false;
        try {
            PreparedStatement existsMovie = conn.prepareStatement("SELECT * FROM movies WHERE title = ? and release_date = ?");
            existsMovie.setString(1, movie.getTitle());
            existsMovie.setDate(2, new java.sql.Date(movie.getReleaseDate().getTime()));
            
            ResultSet exists = existsMovie.executeQuery();
            
            if(!exists.next()) {
                //insert the movie to the movies table
                PreparedStatement insertMovie = conn.prepareStatement("INSERT INTO movies (title, release_date, age_rating, director, actors, synopsis, poster_mimetype, poster) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
                insertMovie.setString(1, movie.getTitle());
                insertMovie.setDate(2, new java.sql.Date(movie.getReleaseDate().getTime()));
                insertMovie.setString(3, movie.getAgeRating());
                insertMovie.setString(4, movie.getDirector());
                insertMovie.setString(5, movie.getActors());
                insertMovie.setString(6, movie.getSynopsis());
                insertMovie.setString(7, poster.getMimeType());
                Blob blob = conn.createBlob();
                blob.setBytes(1, poster.getData());
                insertMovie.setBlob(8, blob);
                
                insertMovie.executeUpdate();
                
                //insert each genre for the movie into movies_have_genres table
                PreparedStatement insertMovieGenre = conn.prepareStatement("INSERT INTO movies_have_genres (title, release_date, genre_title) "
                        + "VALUES (?, ?, ?)");
                insertMovieGenre.setString(1, movie.getTitle());
                insertMovieGenre.setDate(2, new java.sql.Date(movie.getReleaseDate().getTime()));
                for(String genre : movie.getGenres()) {
                    insertMovieGenre.setString(3, genre);
                    insertMovieGenre.executeUpdate();
                }
                
                success = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return success;
    }

    @Override
    public MoviePosterDTO findMoviePoster(String title,
            java.util.Date releaseDate) {
        MoviePosterDTO poster = null;
        
        try {
            PreparedStatement movieQ = conn.prepareStatement("SELECT poster, poster_mimetype FROM movies "
                    + "WHERE title = ? and release_date = ?");
            movieQ.setString(1, title);
            movieQ.setDate(2, new java.sql.Date(releaseDate.getTime()));
            ResultSet r = movieQ.executeQuery();
            if(r.next()) {
                Blob blob = r.getBlob(1);
                if(blob != null) {
                    poster = new MoviePosterDTO(title, releaseDate, r.getString(2), blob.getBytes(1, (int) blob.length()));
                }
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }

        return poster;
    }

}
