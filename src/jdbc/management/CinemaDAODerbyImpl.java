package jdbc.management;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import jdbc.GenericDAODerbyImpl;

public class CinemaDAODerbyImpl extends GenericDAODerbyImpl implements CinemaDAO {

    public CinemaDAODerbyImpl() throws SQLException {
        super();
    }

    @Override
    public List<CinemaDTO> findAll() {
        List<CinemaDTO> cinemas = new ArrayList<CinemaDTO>();
        
        try {
            PreparedStatement allCinemasQ = conn.prepareStatement("SELECT location, capacity FROM cinema");
            PreparedStatement cinemaAmenitiesQ = conn.prepareStatement("SELECT amenity "
                    + "FROM cinemas_have_amenities WHERE location = ?");
            ResultSet allCinemas = allCinemasQ.executeQuery();
            
            while(allCinemas.next()) {
                cinemaAmenitiesQ.setString(1, allCinemas.getString(1));
                ResultSet cinemaAmenities = cinemaAmenitiesQ.executeQuery();
                List<String> amenities = new ArrayList<String>();
                
                while(cinemaAmenities.next()) {
                    amenities.add(cinemaAmenities.getString(1));
                }
                
                cinemas.add(new CinemaDTO(allCinemas.getString(1), allCinemas.getInt(2), amenities));
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
        
        return cinemas;
    }

}
