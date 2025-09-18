package database;

import model.User;
import model.identity.Freelancer;
import model.identity.Chef;
import model.identity.Doctor;
import model.Skill;
import model.Identity;
import model.ShopItemProvider;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Arrays;
import java.util.Objects;

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
                    int health = rs.getInt("health");
                    int energy = rs.getInt("energy");
                    long blockedUntil = rs.getLong("blocked_until");
                    long lastSicknessTime = rs.getLong("last_sickness_time");
                    String inventoryStr = rs.getString("inventory");
                    String skillsStr = rs.getString("skills");

                    Identity identity;
                    switch (identityStr) {
                        case "Chef": identity = new Chef(); break;
                        case "Doctor": identity = new Doctor(); break;
                        default: identity = new Freelancer();
                    }
                    User user = new User(username, password, identity, balance);
                    user.setHealth(health);
                    user.setEnergy(energy);
                    user.setBlockedUntil(blockedUntil);
                    user.setLastSicknessTime(lastSicknessTime);

                    // Deserialize inventory
                    if (inventoryStr != null && !inventoryStr.isEmpty()) {
                        Arrays.stream(inventoryStr.split(","))
                              .map(ShopItemProvider::getItemByName) // Convert name to Item object
                              .filter(Objects::nonNull) // In case an item was removed from the game
                              .forEach(item -> user.getInventory().addItem(item));
                    }

                    // Deserialize skills
                    if (skillsStr != null && !skillsStr.isEmpty()) {
                        Arrays.stream(skillsStr.split(",")).forEach(skillName -> user.getSkills().add(new Skill(skillName)));
                    }

                    return user;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Creates a new user in the database from a User object.
     * @param user The user object to persist.
     * @return true if the user was created successfully.
     */
    public static boolean createUser(User user) {
        String sql = "INSERT INTO users (username, password, identity, balance, health, energy, inventory, skills, blocked_until, last_sickness_time) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPassword());
            stmt.setString(3, user.getIdentity().getClass().getSimpleName());
            stmt.setInt(4, user.getBalance());
            stmt.setInt(5, user.getHealth());
            stmt.setInt(6, user.getEnergy());
            stmt.setString(7, String.join(",", user.getInventory().getItemNames()));
            stmt.setString(8, user.getSkills().stream().map(Skill::getName).reduce((a, b) -> a + "," + b).orElse(""));
            stmt.setLong(9, user.getBlockedUntil());
            stmt.setLong(10, user.getLastSicknessTime());
            stmt.executeUpdate();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Updates an existing user's data in the database.
     * @param user The user object with updated information.
     * @return true if the update was successful.
     */
    public static boolean updateUser(User user) {
        String sql = "UPDATE users SET balance = ?, health = ?, energy = ?, inventory = ?, skills = ?, blocked_until = ?, last_sickness_time = ? WHERE username = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, user.getBalance());
            stmt.setInt(2, user.getHealth());
            stmt.setInt(3, user.getEnergy());
            stmt.setString(4, String.join(",", user.getInventory().getItemNames()));
            stmt.setString(5, user.getSkills().stream().map(Skill::getName).reduce((a, b) -> a + "," + b).orElse(""));
            stmt.setLong(6, user.getBlockedUntil());
            stmt.setLong(7, user.getLastSicknessTime());
            stmt.setString(8, user.getUsername());

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0; // Return true if at least one row was updated
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
