package database;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.stream.Collectors;

/**
 * Handles database and table initialization if they don't exist
 */
public class DatabaseInitializer {
    
    private static final String MYSQL_URL = "jdbc:mysql://localhost:3306/";
    private static final String DB_NAME = "job_quest";
    private static final String USER = "jobuser";
    private static final String PASSWORD = "jobpass";

    /**
     * Check and create database and tables if needed
     */
    public static void initialize() {
        try {
            System.out.println("Checking and initializing database...");
            
            // Step 1: Connect to MySQL and create database if it doesn't exist
            createDatabaseIfNotExists();
            
            // Step 2: Connect to database and create tables
            createTablesIfNotExists();
            
            System.out.println("Database initialized successfully.");
        } catch (Exception e) {
            System.err.println("Error initializing database:");
            e.printStackTrace();
            System.err.println("\n⚠️ Warning: Please ensure MySQL is installed and running.");
            System.err.println("   Also ensure root user has access or user '" + USER + "' with password '" + PASSWORD + "' is created.");
        }
    }

    /**
     * Create database if it doesn't exist
     */
    private static void createDatabaseIfNotExists() throws Exception {
        // Try to connect with defined user
        try (Connection conn = DriverManager.getConnection(MYSQL_URL, USER, PASSWORD);
             Statement stmt = conn.createStatement()) {
            
            String sql = "CREATE DATABASE IF NOT EXISTS " + DB_NAME + " CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci";
            stmt.executeUpdate(sql);
            System.out.println("✓ Database '" + DB_NAME + "' is ready.");
            
        } catch (Exception e) {
            // If defined user doesn't have access, try with root
            System.out.println("Attempting to connect with root user...");
            try (Connection conn = DriverManager.getConnection(MYSQL_URL, "root", "");
                 Statement stmt = conn.createStatement()) {
                
                // Create database
                String createDbSql = "CREATE DATABASE IF NOT EXISTS " + DB_NAME + " CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci";
                stmt.executeUpdate(createDbSql);
                System.out.println("✓ Database '" + DB_NAME + "' created.");
                
                // Create user if it doesn't exist
                try {
                    String createUserSql = "CREATE USER IF NOT EXISTS '" + USER + "'@'localhost' IDENTIFIED BY '" + PASSWORD + "'";
                    stmt.executeUpdate(createUserSql);
                    
                    String grantSql = "GRANT ALL PRIVILEGES ON " + DB_NAME + ".* TO '" + USER + "'@'localhost'";
                    stmt.executeUpdate(grantSql);
                    
                    stmt.executeUpdate("FLUSH PRIVILEGES");
                    System.out.println("✓ User '" + USER + "' created and privileges granted.");
                } catch (Exception userEx) {
                    // If user already exists or error occurred
                    System.out.println("ℹ User already exists or needs manual configuration.");
                }
            }
        }
    }

    /**
     * Create tables if they don't exist
     */
    private static void createTablesIfNotExists() throws Exception {
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement()) {
            
            // Create users table with all required columns
            String createUsersTable = """
                CREATE TABLE IF NOT EXISTS users (
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    username VARCHAR(50) NOT NULL UNIQUE,
                    password VARCHAR(100) NOT NULL,
                    identity VARCHAR(50) NOT NULL,
                    balance INT DEFAULT 0,
                    health INT DEFAULT 100,
                    energy INT DEFAULT 100,
                    blocked_until BIGINT DEFAULT 0,
                    last_sickness_time BIGINT DEFAULT 0,
                    inventory TEXT,
                    skills TEXT,
                    total_play_time BIGINT DEFAULT 0
                ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci
                """;
            
            stmt.executeUpdate(createUsersTable);
            System.out.println("✓ Table 'users' is ready.");
            
            // Create items table (for future use)
            String createItemsTable = """
                CREATE TABLE IF NOT EXISTS items (
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    name VARCHAR(50) NOT NULL,
                    type VARCHAR(30),
                    owner_id INT,
                    FOREIGN KEY (owner_id) REFERENCES users(id) ON DELETE CASCADE
                ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci
                """;
            
            stmt.executeUpdate(createItemsTable);
            System.out.println("✓ Table 'items' is ready.");
        }
    }

    /**
     * Read SQL file content from resources (optional - for direct SQL file execution)
     */
    private static String readSqlFile(String filename) throws Exception {
        InputStream is = DatabaseInitializer.class.getResourceAsStream("/" + filename);
        if (is == null) {
            is = DatabaseInitializer.class.getResourceAsStream(filename);
        }
        if (is == null) {
            throw new Exception("SQL file not found: " + filename);
        }
        
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
            return reader.lines().collect(Collectors.joining("\n"));
        }
    }
}
