package jdbc;

import java.sql.Connection;
import java.sql.SQLException;

public class GenericDAODerbyImpl {
    protected Connection conn;

    public GenericDAODerbyImpl() throws SQLException {
        this.conn = DBConnFactory.getConnection();
    }
}
