package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

/**
 * Handles database and table initialization if they don't exist
 */
public class DatabaseInitializer {
    
    private static final String MYSQL_URL = "jdbc:mysql://localhost:3306/";
    private static final String DB_NAME = "job_quest";
    private static final String USER = "jobuser";
    private static final String PASSWORD = "jobpass";

    public static void initialize() {
        // ensure database and tables exist, creating them if necessary
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

    private static void createDatabaseIfNotExists() throws Exception {
        // create the database and user if missing
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

    private static void createTablesIfNotExists() throws Exception {
        // create required tables in the database if absent
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement()) {
            
            // Create users table with all required columns
            // Step 4: progression columns persist level/XP/reputation,
            // lifetime stats, streaks and unlocks (Repository can round-trip User).
            String createUsersTable = """
                CREATE TABLE IF NOT EXISTS users (
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    username VARCHAR(50) NOT NULL UNIQUE,
                    password VARCHAR(100) NOT NULL,
                    identity VARCHAR(50) NOT NULL,
                    balance INT DEFAULT 0,
                    health INT DEFAULT 100,
                    energy INT DEFAULT 100000,
                    blocked_until BIGINT DEFAULT 0,
                    last_sickness_time BIGINT DEFAULT 0,
                    inventory TEXT,
                    skills TEXT,
                    total_play_time BIGINT DEFAULT 0,
                    level INT DEFAULT 1,
                    experience INT DEFAULT 0,
                    reputation INT DEFAULT 0,
                    total_money_earned INT DEFAULT 0,
                    total_jobs_completed INT DEFAULT 0,
                    current_streak INT DEFAULT 0,
                    max_streak INT DEFAULT 0,
                    last_login_date BIGINT DEFAULT 0,
                    unlocked_achievements TEXT,
                    active_challenges TEXT
                ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci
                """;
            
            stmt.executeUpdate(createUsersTable);
            System.out.println("✓ Table 'users' is ready.");

            // Auto-migrate databases created before Step 4 (idempotent).
            ensureProgressionColumns(conn);
            
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
     * Add Step-4 progression columns to pre-existing installs.
     * Idempotent: checks INFORMATION_SCHEMA first, so fresh and old DBs converge.
     * Encapsulated here so Repository code stays clean (SRP).
     */
    private static void ensureProgressionColumns(java.sql.Connection conn) {
        String[][] columns = {
            {"level", "INT DEFAULT 1"},
            {"experience", "INT DEFAULT 0"},
            {"reputation", "INT DEFAULT 0"},
            {"total_money_earned", "INT DEFAULT 0"},
            {"total_jobs_completed", "INT DEFAULT 0"},
            {"current_streak", "INT DEFAULT 0"},
            {"max_streak", "INT DEFAULT 0"},
            {"last_login_date", "BIGINT DEFAULT 0"},
            {"unlocked_achievements", "TEXT"},
            {"active_challenges", "TEXT"},
        };
        // NOTE: DB_NAME is fixed for this project; table is `users`.
        for (String[] col : columns) {
            String name = col[0];
            String def = col[1];
            try (java.sql.PreparedStatement check = conn.prepareStatement(
                    "SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS " +
                    "WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'users' AND COLUMN_NAME = ?")) {
                check.setString(1, name);
                try (java.sql.ResultSet rs = check.executeQuery()) {
                    boolean exists = rs.next() && rs.getInt(1) > 0;
                    if (!exists) {
                        try (java.sql.Statement alter = conn.createStatement()) {
                            alter.executeUpdate("ALTER TABLE users ADD COLUMN " + name + " " + def);
                            System.out.println("✓ Migrated users." + name + " column.");
                        }
                    }
                }
            } catch (Exception e) {
                System.err.println("⚠️ Could not ensure column users." + name + ": " + e.getMessage());
            }
        }
        // Fix legacy default: energy was 100, game uses 100_000.
        try (java.sql.Statement fix = conn.createStatement()) {
            fix.executeUpdate("ALTER TABLE users ALTER COLUMN energy SET DEFAULT 100000");
        } catch (Exception e) {
            System.err.println("ℹ Could not update energy default: " + e.getMessage());
        }
    }

}
