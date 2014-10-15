package jdbc.management;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jdbc.GenericDAODerbyImpl;

public class CinemaDAODerbyImpl extends GenericDAODerbyImpl implements CinemaDAO {

    public CinemaDAODerbyImpl() throws SQLException {
        super();
    }

    @Override
    public List<CinemaDTO> findAll() {
        List<CinemaDTO> cinemas = new ArrayList<CinemaDTO>();
        
        try {
            PreparedStatement allCinemasQ = conn.prepareStatement("SELECT location, capacity FROM cinemas");
            PreparedStatement cinemaAmenitiesQ = conn.prepareStatement("SELECT amenity "
                    + "FROM cinemas_have_amenities WHERE location = ?");
            ResultSet allCinemas = allCinemasQ.executeQuery();
            
            while(allCinemas.next()) {
                cinemaAmenitiesQ.setString(1, allCinemas.getString(1));
                ResultSet cinemaAmenities = cinemaAmenitiesQ.executeQuery();
                Set<String> amenities = new HashSet<String>();
                
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

    @Override
    public boolean createCinema(CinemaDTO newCinema) {
        boolean success = false;
        
        try {
            PreparedStatement exists = conn.prepareStatement("SELECT * FROM cinemas WHERE location = ?");
            exists.setString(1, newCinema.getLocation());
            if(exists.executeQuery().next()) {
                success = false;
            } else {
                PreparedStatement insertCinema = conn.prepareStatement("INSERT INTO cinemas (location, capacity) "
                        + "VALUES (?, ?)");
                insertCinema.setString(1, newCinema.getLocation());
                insertCinema.setInt(2, newCinema.getCapacity());
                insertCinema.execute();
                
                PreparedStatement insertAmenity = conn.prepareStatement("INSERT INTO cinemas_have_amenities (location, amenity) "
                        + "VALUES (?, ?)");
                insertAmenity.setString(1, newCinema.getLocation());
                for(String amenity : newCinema.getAmenities()) {
                    insertAmenity.setString(2, amenity);
                    insertAmenity.execute();
                }
            }
            success = true;
        } catch(SQLException e) {
            e.printStackTrace();
            success = false;
        }
        
        return success;
    }

}
