package jdbc;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.sql.Date;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import jdbc.management.MovieDTO;

public class SearchDAODerbyImpl implements SearchDAO{
	
	private static SearchDAODerbyImpl instance;
    Connection conn;
	
	private SearchDAODerbyImpl() throws SQLException {
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
		String exists = "SELECT username,title,release_date FROM viewers_rate_movies "
				+ "WHERE username = ? AND title = ? AND release_date = ?";
		PreparedStatement statement1;
		PreparedStatement statement2;
		try {
			statement2 = conn.prepareStatement(exists);
			statement2.setString(1, review.getUsername());
			statement2.setString(2, review.getTitle());
			statement2.setDate(3, review.getReleaseDate());
			ResultSet result = statement2.executeQuery();
			if(!result.next()) {
				statement1 = conn.prepareStatement(sql);
				statement1.setString(1, review.getUsername());
				statement1.setString(2, review.getTitle());
				statement1.setDate(3, review.getReleaseDate());
				statement1.setInt(4, review.getRating());
				statement1.setString(5, review.getReview_text());
				statement1.executeUpdate();
			} else {
				
			}
			
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

    @Override
    public ArrayList<MovieDTO> getResults(String query,
            List<String> withinGenres) {
        System.out.println("query: " + query);
        Set<MovieDTO> sresults = new TreeSet<MovieDTO>();
        // SAMPLE QUERY
        // select * from movies m join movies_have_genres j on m.title = j.title and m.release_date = j.release_date 
        //         where (j.genre_title = 'Drama' or j.genre_title = 'Thriller')
        //             and (lower(m.title) like '%bird%' or lower(m.title) like '%girl%');
        
        String[] keywordSplit = query.split("[^a-zA-Z]");
        List<String> keywords = new ArrayList<String>();
        for(String k : keywordSplit) {
            if(! k.isEmpty()) {
                keywords.add(k);
            }
        }
        
        String queryStr = "SELECT m.title, m.release_date, m.age_rating, m.director, m.actors, m.synopsis " +
        		"FROM movies m JOIN movies_have_genres g " +
        		"ON m.title = g.title AND m.release_date = g.release_date " +
        		"WHERE (g.genre_title = 'no genre really'";//hack so we don't have an empty ()
        
        for(int i = 0; i < withinGenres.size(); ++i) {
            queryStr = queryStr + " OR g.genre_title = ?";
        }
        
        queryStr = queryStr + ") " +
        		"OR (lower(m.title) LIKE '%aaaaaaaaaaa%'";//another hack dummy string
        
        for(int i = 0; i < keywords.size(); ++i) {
            queryStr = queryStr + " OR lower(m.title) LIKE ?";
        }
        
        queryStr = queryStr + ")";
        
        try {
            PreparedStatement find = conn.prepareStatement(queryStr);
            System.out.println("querystr looks like " + queryStr);
            for(int i = 0; i < withinGenres.size(); ++i) {
                System.out.println("setting " + (i + 1) + " to " + withinGenres.get(i));
                find.setString(i + 1, withinGenres.get(i));
            }
            
            for(int i = 0; i < keywords.size(); ++i) {
                System.out.println("setting " + (i + withinGenres.size() + 1) + " to %" + keywords.get(i).toLowerCase() + "%");
                find.setString(i + withinGenres.size() + 1, "%" + keywords.get(i).toLowerCase() + "%");
            }
            
            
            System.out.println("about to execute:::: " + queryStr + "::::");
            ResultSet results = find.executeQuery();
            System.out.println("executed");
            
            
            while(results.next()) {
                List<String> genres = new ArrayList<String>();
                PreparedStatement mGenres = conn.prepareStatement("SELECT g.genre_title " +
            		"FROM movies m JOIN movies_have_genres g " +
            		"ON m.title = g.title AND m.release_date = g.release_date " +
            		"WHERE m.title = ? AND m.release_date = ?");
                mGenres.setString(1, results.getString(1));
                mGenres.setDate(2, results.getDate(2));
                ResultSet genreResults = mGenres.executeQuery();
                while(genreResults.next()) {
                    genres.add(genreResults.getString(1));
                }
                
                sresults.add(new MovieDTO(results.getString(1), results.getDate(2), results.getString(3),
                        genres, results.getString(4), results.getString(5), results.getString(6)));
            }
        } catch(SQLException s) {
            s.printStackTrace();
        }
        System.out.println("result size " + sresults.size());
        for(MovieDTO r : sresults) {
            System.out.println("result: " + r.getTitle() + " " + r.getReleaseDate());
        }
        return new ArrayList<MovieDTO>(sresults);
    }

    public static SearchDAO get() throws SQLException {
        if(instance == null) {
            instance = new SearchDAODerbyImpl();
        }
        return instance;
    }

}
