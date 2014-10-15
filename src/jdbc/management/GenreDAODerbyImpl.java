package jdbc.management;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import jdbc.GenericDAODerbyImpl;
import jdbc.management.GenreDAO;

public class GenreDAODerbyImpl extends GenericDAODerbyImpl implements GenreDAO {

    public GenreDAODerbyImpl() throws SQLException {
        super();
    }

    @Override
    public Set<String> findAll() {
        Set<String> allGenres = new HashSet<String>();

        try {
            PreparedStatement genresQ = conn.prepareStatement("SELECT genre_title FROM genres");
            ResultSet genres = genresQ.executeQuery();
            
            while(genres.next()) {
                allGenres.add(genres.getString(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return allGenres;
    }

}
