package database;

import model.User;
import model.Freelancer;
import model.Chef;
import model.Doctor;
import model.Identity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DatabaseUtil {

    // متد برای گرفتن کاربر از دیتابیس
    public static User getUser(String username) {
        String sql = "SELECT * FROM users WHERE username = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String password = rs.getString("password");
                    String identityStr = rs.getString("identity");
                    int balance = rs.getInt("balance");

                    Identity identity;
                    switch (identityStr) {
                        case "Chef": identity = new Chef(); break;
                        case "Doctor": identity = new Doctor(); break;
                        default: identity = new Freelancer();
                    }
                    return new User(username, password, identity, balance);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // متد برای ذخیره کاربر در دیتابیس
    public static boolean saveUser(String username, String password, String identity, int balance) {
        String sql = "INSERT INTO users (username, password, identity, balance) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.setString(3, identity);
            stmt.setInt(4, balance);
            stmt.executeUpdate();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
