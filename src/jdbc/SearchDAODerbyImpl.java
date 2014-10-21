package jdbc;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
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
        		+ "FROM movies m "
        		+ "INNER JOIN movies_have_genres mg ON mg.title=m.title "
        		+ "WHERE LOWER(m.title) LIKE LOWER(?) "
        		+ "OR LOWER(mg.genre_title) LIKE (?)"
        		+ "ORDER BY m.title";
	
		
		String genreStatement = "SELECT genre_title "
        		+ "FROM movies m "
        		+ "INNER JOIN movies_have_genres mg ON mg.title=m.title "
        		+ "WHERE LOWER(m.title) = LOWER(?) ";
		
		
		try {
            PreparedStatement statement = conn.prepareStatement(searchStatement);
            statement.setString(1, "%" + query + "%");
            statement.setString(2, "%" + query + "%");
            
            ResultSet res = statement.executeQuery();
            // get search results
            while(res.next()){
				String title = res.getString("title");
				Date release_date = res.getDate("release_date");
				String age_rating = res.getString("age_rating");
				String director = res.getString("director");
				String actors = res.getString("actors");
				String synopsis = res.getString("synopsis");
				List<String> genres = new ArrayList<String>();
				MovieDTO movie = new MovieDTO(title, release_date, age_rating, genres, director, actors, synopsis);
			
				
				if(results.contains(movie) == true) {
					// already processed
					
				} else {
					// get genres
					PreparedStatement g = conn.prepareStatement(genreStatement);
					g.setString(1, title);
					ResultSet res2 = g.executeQuery();
					while(res2.next()) {
						String genre = res2.getString("genre_title");
						genres.add(genre);
					}
					movie.setGenres(genres);
					results.add(movie);
				}
            }

        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
		return results;
	}
	
	public MovieDTO getMovie(String title, Date releaseDate) {

		MovieDTO movie = new MovieDTO(title, releaseDate);
		List<String> genres = new ArrayList<String>();
		
		String searchStatement = "SELECT * "
        		+ "FROM movies m "
        		+ "WHERE LOWER(m.title) = LOWER(?) "
        		+ "AND m.release_date = ?";
	
		
		String genreStatement = "SELECT genre_title "
        		+ "FROM movies m "
        		+ "INNER JOIN movies_have_genres mg ON mg.title=m.title "
        		+ "WHERE LOWER(m.title) = LOWER(?) ";
		
		
		try {
            PreparedStatement statement = conn.prepareStatement(searchStatement);
            statement.setString(1, title);
            statement.setDate(2, releaseDate);
            
            ResultSet res = statement.executeQuery();

            while(res.next()){
				String ageRating = res.getString("age_rating");
				movie.setAgeRating(ageRating);
				String director = res.getString("director");
				movie.setDirector(director);
				String actors = res.getString("actors");
				movie.setActors(actors);
				String synopsis = res.getString("synopsis");
				movie.setSynopsis(synopsis);

				
				// get genres
				PreparedStatement g = conn.prepareStatement(genreStatement);
				g.setString(1, title);
				ResultSet res2 = g.executeQuery();
				while(res2.next()) {
					String genre = res2.getString("genre_title");
					genres.add(genre);
				}
				movie.setGenres(genres);
            }
		} catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
		
		return movie;
	}
	
	public ArrayList<ReviewDTO> getReviews(String title, Date releaseDate) {
		ArrayList<ReviewDTO> reviews = new ArrayList<ReviewDTO>();
		
		String ratingStatement = "SELECT username, rating, review_text "
				+ "FROM movies m "
				+ "INNER JOIN viewers_rate_movies v ON m.title = v.title "
				+ "WHERE LOWER(m.title) = LOWER(?) "
				+ "AND m.release_date = ?";
		
		// get ratings
		PreparedStatement ratings;
		try {
			ratings = conn.prepareStatement(ratingStatement);
			ratings.setString(1, title);
			ratings.setDate(2, releaseDate);
			ResultSet res = ratings.executeQuery();
			while(res.next()) {
				Integer rating = res.getInt("rating");
				String review_text = res.getString("review_text");
				String username = res.getString("username");
				ReviewDTO review = new ReviewDTO(title, releaseDate, username, rating, review_text);
				reviews.add(review);
				
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return reviews;
	}

	@Override
	public void storeReview(ReviewDTO review) {
		String sql = "INSERT INTO viewers_rate_movies (username, title, release_date, rating, review_text) "
				+ "VALUES (?, ?, ?, ?, ?)";
		PreparedStatement statement;
		try {
			statement = conn.prepareStatement(sql);
			statement.setString(1, review.getUsername());
			statement.setString(2, review.getTitle());
			statement.setDate(3, review.getReleaseDate());
			statement.setInt(4, review.getRating());
			statement.setString(5, review.getReview_text());
			statement.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

	@Override
	public ArrayList<MovieDTO> getNowShowing() {
		String nowShowingQuery = "SELECT * FROM movies WHERE release_date < CURRENT DATE ORDER BY release_date DESC";
		PreparedStatement statement;
		ArrayList<MovieDTO> nowShowing = new ArrayList<MovieDTO>();
		try {
			statement = conn.prepareStatement(nowShowingQuery);
			statement.setMaxRows(4);
			ResultSet res = statement.executeQuery();
			while (res.next()) {
				String title = res.getString("title");
				Date releaseDate = res.getDate("release_date");
				String ageRating = res.getString("age_rating");
				MovieDTO m = new MovieDTO(title, releaseDate);
				m.setAgeRating(ageRating);
				nowShowing.add(m);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return nowShowing;
	}

	@Override
	public ArrayList<MovieDTO> getComingSoon() {
		String comingSoonQuery = "SELECT * FROM movies WHERE release_date > CURRENT DATE ORDER BY release_date";
		PreparedStatement statement;
		ArrayList<MovieDTO> comingSoon = new ArrayList<MovieDTO>();
		try {
			statement = conn.prepareStatement(comingSoonQuery);
			statement.setMaxRows(4);
			ResultSet res = statement.executeQuery();
			while (res.next()) {
				String title = res.getString("title");
				Date releaseDate = res.getDate("release_date");
				String ageRating = res.getString("age_rating");
				MovieDTO m = new MovieDTO(title, releaseDate);
				m.setAgeRating(ageRating);
				comingSoon.add(m);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return comingSoon;
	}

}
