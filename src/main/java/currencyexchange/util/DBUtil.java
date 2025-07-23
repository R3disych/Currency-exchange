package currencyexchange.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBUtil {
    private static final String DB_URL = "jdbc:postgresql://localhost/currency-exchange";
    private static final String DB_USER = "postgres";
    private static final String DB_PASSWORD = "pipipopa";

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("PostgreSQL JDBC Driver not found");
            e.printStackTrace();
            throw new SQLException("PostgreSQL JDBC Driver not found: " + e.getMessage());
        }
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }
}
