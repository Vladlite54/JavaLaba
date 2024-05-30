package DBcontrol;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class DBConnectionManager {
    private static final String url = "jdbc:postgresql://localhost:5432/cars";
    private static final String username = "postgres";
    private static final String password = "153426";

    static {
        loadDriver();
    }

    private DBConnectionManager() {
    }

    public static Connection open() {
        try {
            return DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static void loadDriver() {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
