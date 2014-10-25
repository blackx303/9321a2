package jdbc.management;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import jdbc.GenericDAODerbyImpl;

public class ScreeningDAODerbyImpl extends GenericDAODerbyImpl implements ScreeningDAO {

    public ScreeningDAODerbyImpl() throws SQLException {
        super();
    }

    @Override
    public List<ScreeningDTO> findAll() {
        List<ScreeningDTO> screenings = new ArrayList<ScreeningDTO>();
        
        try {
            PreparedStatement findall = conn.prepareStatement("SELECT title, release_date, "
                    + "location, screening_time "
                    + "FROM movies_screen_in_cinemas");
            
            ResultSet all = findall.executeQuery();
            
            while(all.next()) {
                screenings.add(new ScreeningDTO(all.getString(1), all.getDate(2), all.getString(3), all.getTimestamp(4)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return screenings;
    }

    @Override
    public boolean create(ScreeningDTO screening) {
        try {
            PreparedStatement add = conn.prepareStatement("INSERT INTO "
                    + "movies_screen_in_cinemas(title, release_date, location, screening_time) "
                    + "VALUES (?, ?, ?, ?)");
            add.setString(1, screening.getTitle());
            add.setDate(2, new java.sql.Date(screening.getReleaseDate().getTime()));
            add.setString(3, screening.getCinema());
            add.setTimestamp(4, new java.sql.Timestamp(screening.getScreeningTime().getTime()));
            
            add.execute();
            return true;
        } catch(SQLException s) {
            s.printStackTrace();
        }
        
        return false;
    }

}
