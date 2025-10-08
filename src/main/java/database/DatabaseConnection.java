package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Manages database connections using configuration.
 * Implements Singleton pattern for connection management.
 */
public class DatabaseConnection {
    private static DatabaseConnection instance;
    private final DatabaseConfig config;
    
    private DatabaseConnection(DatabaseConfig config) {
        this.config = config;
    }
    
    public static DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection(DatabaseConfig.getDefault());
        }
        return instance;
    }
    
    public static DatabaseConnection getInstance(DatabaseConfig config) {
        if (instance == null) {
            instance = new DatabaseConnection(config);
        }
        return instance;
    }
    
    public Connection createConnection() throws SQLException {
        return DriverManager.getConnection(
            config.getUrl(),
            config.getUser(),
            config.getPassword()
        );
    }
    
    // For backward compatibility - delegates to instance method
    public static Connection getConnection() throws SQLException {
        return getInstance().createConnection();
    }
}


