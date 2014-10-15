package jdbc.management;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import jdbc.GenericDAODerbyImpl;

public class AgeRatingDAODerbyImpl extends GenericDAODerbyImpl implements
        AgeRatingDAO {

    public AgeRatingDAODerbyImpl() throws SQLException {
        super();
    }

    @Override
    public List<String> findAll() {
        List<String> ageRatings = new ArrayList<String>();
        
        try {
            PreparedStatement ratingsQ = conn.prepareStatement("SELECT rating FROM age_ratings");
            ResultSet ratings = ratingsQ.executeQuery();
            while(ratings.next()) {
                ageRatings.add(ratings.getString(1));
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
        
        return ageRatings;
    }

}
