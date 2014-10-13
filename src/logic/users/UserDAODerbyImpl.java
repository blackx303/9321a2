package logic.users;

import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import javax.xml.bind.DatatypeConverter;

import jdbc.DBConnFactory;
import jdbc.users.PendingUserDTO;
import jdbc.users.UserDAO;
import jdbc.users.UserDTO;
import jdbc.users.ViewerDTO;

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

    @Override
    public String createPending(String username, String saltString, String hashString, String email) {
        String key = null;
        
        try {
            PreparedStatement statement = conn.prepareStatement("INSERT INTO ACCOUNT (username, salt, password_and_salt_hash)"
                    + "values (?, ?, ?)");
            statement.setString(1, username);
            statement.setString(2, saltString);
            statement.setString(3, hashString);
            
            if(statement.executeUpdate() == 1) {
                //insert was successful
                
                SecureRandom r = new SecureRandom();
                byte[] keyBytes = new byte[32];
                r.nextBytes(keyBytes);
                key = DatatypeConverter.printHexBinary(keyBytes);
                
                PreparedStatement insertpendingtable = conn.prepareStatement("INSERT INTO PENDING_ACCOUNT (username, confirmation_key, email, created_at)"
                        + "values (?, ?, ?, ?)");
                insertpendingtable.setString(1, username);
                insertpendingtable.setString(2, key);//confirmation key
                insertpendingtable.setString(3, email);//email
                insertpendingtable.setTimestamp(4, new Timestamp(System.currentTimeMillis()));//time now
                
                if(insertpendingtable.executeUpdate() != 1) {
                    key = null;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            key = null;
        }
        
        return key;
    }

    @Override
    public boolean confirmPending(String username, String confirmationKey) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void storeViewer(ViewerDTO user) {
        try {
            //update viewer_account table
            PreparedStatement update = conn.prepareStatement("UPDATE viewer_account "
                    + "SET nickname = ?, email = ?, first_name = ?, last_name = ? "
                    + "WHERE username = ?");
            update.setString(1, user.getNickname());
            update.setString(2, user.getEmail());
            update.setString(3, user.getFirstName());
            update.setString(4, user.getLastName());
            update.setString(5, user.getUsername());
            
            update.executeUpdate();
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public PendingUserDTO findPendingUser(String username) {
        PendingUserDTO user = null;

        try {
            PreparedStatement pend_statement = conn.prepareStatement("SELECT username, confirmation_key, email FROM "
                    + "pending_account WHERE username = ?");
            pend_statement.setString(1, username);
            ResultSet p = pend_statement.executeQuery();
            PreparedStatement acc_statement = conn.prepareStatement("SELECT salt, password_and_salt_hash FROM account "
                    + "WHERE (username = ?)");
            acc_statement.setString(1, username);
            ResultSet a = acc_statement.executeQuery();
            
            if(p.next() && a.next()) {
                user = new PendingUserDTO(p.getString(1), a.getString(1), a.getString(2), p.getString(2), p.getString(3));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return user;
    }

    @Override
    public void confirmPendingUser(String username) {
        PendingUserDTO user = findPendingUser(username);
        
        try {
            PreparedStatement insert = conn.prepareStatement("INSERT INTO viewer_account (email, username) "
                    + "values (?, ?)");
            insert.setString(1, user.getEmail());
            insert.setString(2, username);
            insert.executeUpdate();
            
            PreparedStatement remove = conn.prepareStatement("DELETE FROM pending_account "
                    + "WHERE (username = ?)");
            remove.setString(1, username);
            remove.executeUpdate();
        } catch(SQLException e) {
            e.printStackTrace();
        }
        
    }

    @Override
    public ViewerDTO findNormalUser(String username) {
        ViewerDTO user = null;

        try {
            PreparedStatement statement = 
                    conn.prepareStatement("SELECT a.username, a.salt, a.password_and_salt_hash, "
                            + "v.email, v.nickname, v.first_name, v.last_name "
                            + "FROM account a "
                            + "JOIN viewer_account v "
                            + "ON a.username = v.username "
                            + "WHERE a.username = ?");
            statement.setString(1, username);
            ResultSet p = statement.executeQuery();
            
            if(p.next()) {
                user = new ViewerDTO(p.getString(1), p.getString(2), p.getString(3), p.getString(4),
                        p.getString(5), p.getString(6), p.getString(7));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return user;
    }

}
