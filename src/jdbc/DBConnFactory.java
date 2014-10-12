package jdbc;

import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class DBConnFactory {
    private static DBConnFactory factory = null;
    private DataSource ds = null;
    
    private DBConnFactory() {
        try {
            InitialContext context = new InitialContext();
            ds = (DataSource) context.lookup("java:comp/env/jdbc/9321ass2");
            System.out.println("db found");
        } catch (NamingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
    }
    
    public static Connection getConnection() throws SQLException {
        if(factory == null) {
            factory = new DBConnFactory();
        }
        
        Connection conn = factory.getDataSource().getConnection();
        
        return conn;
    }

    private DataSource getDataSource() {
        return ds;
    }

}
