package jdbc.management;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import jdbc.GenericDAODerbyImpl;

public class AmenityDAODerbyImpl extends GenericDAODerbyImpl implements AmenityDAO {

    public AmenityDAODerbyImpl() throws SQLException {
        super();
    }

    @Override
    public Set<String> findAllTypes() {
        Set<String> types = new HashSet<String>();
        
        try {
            PreparedStatement statement = conn.prepareStatement("SELECT amenity_type FROM amenities");
            ResultSet r = statement.executeQuery();
    
            while(r.next()) {
                types.add(r.getString(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return types;
    }

}
