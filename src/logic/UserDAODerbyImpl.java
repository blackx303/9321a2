package logic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import jdbc.DBConnFactory;
import jdbc.UserDAO;
import jdbc.UserDTO;

public class UserDAODerbyImpl implements UserDAO {
    private Connection conn;
    
    public UserDAODerbyImpl() throws SQLException {
        this.conn = DBConnFactory.getConnection();
    }

    @Override
    public void storeUser(UserDTO user) {
        // TODO Auto-generated method stub

    }

    @Override
    public UserDTO findUser(String username) {
        UserDTO user = null;
        
        try {
            PreparedStatement statement = 
                    conn.prepareStatement("SELECT username, salt, password_and_salt_hash " +
                    		"FROM ACCOUNT WHERE username = ?");
            statement.setString(1, username);
        
        
            ResultSet results = statement.executeQuery();
            if(results.next()) {
                //if we actually got a matching user

                user = new UserDTO(results.getString(1), results.getString(2), results.getString(3));
            }
            System.out.println("query ran");
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        
        return user;
    }

}
