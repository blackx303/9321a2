package jdbc.management;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import jdbc.DBConnFactory;
import jdbc.GenericDAODerbyImpl;

public class AmenityDAODerbyImpl extends GenericDAODerbyImpl implements AmenityDAO {

    public AmenityDAODerbyImpl() throws SQLException {
        super();
    }

    @Override
    public List<String> findAllTypes() {
        List<String> types = new ArrayList<String>();
        
        try {
            PreparedStatement statement = conn.prepareStatement("SELECT amenity_type FROM amenity");
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
