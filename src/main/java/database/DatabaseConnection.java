package database;  // یا اگر اسم پکیجت database هست، اینو تغییر بده

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/job_quest";
    private static final String USER = "jobuser";       // یوزری که ساختی
    private static final String PASSWORD = "jobpass";   // پسوردی که بهش دادی

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
