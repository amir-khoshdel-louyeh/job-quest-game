package database;

/**
 * Configuration class for database connection settings.
 * Follows Single Responsibility Principle by managing only configuration.
 */
public class DatabaseConfig {
    private final String url;
    private final String user;
    private final String password;
    
    // Default configuration
    private static final DatabaseConfig DEFAULT = new DatabaseConfig(
        "jdbc:mysql://localhost:3306/job_quest",
        "jobuser",
        "jobpass"
    );
    
    public DatabaseConfig(String url, String user, String password) {
        this.url = url;
        this.user = user;
        this.password = password;
    }
    
    public static DatabaseConfig getDefault() {
        return DEFAULT;
    }
    
    public String getUrl() {
        return url;
    }
    
    public String getUser() {
        return user;
    }
    
    public String getPassword() {
        return password;
    }
}
