package database.repository;

import database.DatabaseConnection;
import model.Identity;
import model.Skill;
import model.User;
import model.identity.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * MySQL implementation for UserRepository
 */
public class UserRepositoryImpl implements UserRepository {
    private static final Logger LOGGER = Logger.getLogger(UserRepositoryImpl.class.getName());
    private final DatabaseConnection dbConnection;
    
    public UserRepositoryImpl() {
        // default constructor using singleton DBConnection
        this.dbConnection = DatabaseConnection.getInstance();
    }
    
    public UserRepositoryImpl(DatabaseConnection dbConnection) {
        this.dbConnection = dbConnection;
    }
    
    
    public User findByUsername(String username) throws SQLException {
        // find and return a user by username
        String sql = "SELECT * FROM users WHERE username = ?";
        
        try (Connection conn = dbConnection.createConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, username);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToUser(rs);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error finding user: " + username, e);
            throw e;
        }
        
        return null;
    }
    
    
    public boolean save(User user) throws SQLException {
        // insert a new user record into the database
        String sql = "INSERT INTO users (username, password, identity, balance, health, energy, " +
                     "inventory, skills, blocked_until, last_sickness_time, total_play_time) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = dbConnection.createConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            setUserParameters(stmt, user);
            stmt.executeUpdate();
            return true;
            
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error saving user: " + user.getUsername(), e);
            throw e;
        }
    }
    
    
    public boolean update(User user) throws SQLException {
        // update fields of an existing user record
        String sql = "UPDATE users SET balance = ?, health = ?, energy = ?, inventory = ?, " +
                     "skills = ?, blocked_until = ?, last_sickness_time = ?, total_play_time = ? " +
                     "WHERE username = ?";
        
        try (Connection conn = dbConnection.createConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, user.getBalance());
            stmt.setInt(2, user.getHealth());
            stmt.setInt(3, user.getEnergy());
            stmt.setString(4, String.join(",", user.getInventory().getItemNames()));
            stmt.setString(5, serializeSkills(user));
            stmt.setLong(6, user.getBlockedUntil());
            stmt.setLong(7, user.getLastSicknessTime());
            stmt.setLong(8, user.getTotalPlayTime());
            stmt.setString(9, user.getUsername());
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error updating user: " + user.getUsername(), e);
            throw e;
        }
    }
    
    
    public boolean delete(String username) throws SQLException {
        // delete a user record by username
        String sql = "DELETE FROM users WHERE username = ?";
        
        try (Connection conn = dbConnection.createConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, username);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error deleting user: " + username, e);
            throw e;
        }
    }
    
    /** Map a ResultSet row to a User object */
    private User mapResultSetToUser(ResultSet rs) throws SQLException {
        // build a User object from a DB row
        String username = rs.getString("username");
        String password = rs.getString("password");
        String identityStr = rs.getString("identity");
        int balance = rs.getInt("balance");
        int health = rs.getInt("health");
        int energy = rs.getInt("energy");
        long blockedUntil = rs.getLong("blocked_until");
        long lastSicknessTime = rs.getLong("last_sickness_time");
        String inventoryStr = rs.getString("inventory");
        String skillsStr = rs.getString("skills");
        long totalPlayTime = rs.getLong("total_play_time");
        
        Identity identity = createIdentityFromString(identityStr);
        User user = new User(username, password, identity, balance);
        if (health > user.getHealth()) {
            user.gainHealth(health - user.getHealth());
        } else if (health < user.getHealth()) {
            user.loseHealth(user.getHealth() - health);
        }
        if (energy > user.getEnergy()) {
            user.gainEnergy(energy - user.getEnergy());
        } else if (energy < user.getEnergy()) {
            user.loseEnergy(user.getEnergy() - energy);
        }
        user.setBlockedUntil(blockedUntil);
        user.setLastSicknessTime(lastSicknessTime);
        user.setTotalPlayTime(totalPlayTime);
        
        deserializeInventory(user, inventoryStr);
        deserializeSkills(user, skillsStr);
        
        return user;
    }
    
    /** Set parameters for INSERT */
    private void setUserParameters(PreparedStatement stmt, User user) throws SQLException {
        // bind user fields to an INSERT prepared statement
        stmt.setString(1, user.getUsername());
        stmt.setString(2, user.getPassword());
        stmt.setString(3, user.getIdentity().getClass().getSimpleName());
        stmt.setInt(4, user.getBalance());
        stmt.setInt(5, user.getHealth());
        stmt.setInt(6, user.getEnergy());
        stmt.setString(7, String.join(",", user.getInventory().getItemNames()));
        stmt.setString(8, serializeSkills(user));
        stmt.setLong(9, user.getBlockedUntil());
        stmt.setLong(10, user.getLastSicknessTime());
        stmt.setLong(11, user.getTotalPlayTime());
    }
    
    /** Create Identity from class name string */
    private Identity createIdentityFromString(String identityStr) {
        // convert stored identity name to an Identity object
        return switch (identityStr) {
            case "Doctor" -> new Doctor();
            case "Programmer" -> new Programmer();
            case "LogoDesigner" -> new LogoDesigner();
            case "Typist" -> new Typist();
            default -> {
                LOGGER.warning("Unknown identity: " + identityStr + ", defaulting to Typist");
                yield new Typist();
            }
        };
    }
    
    /** Convert inventory string to Inventory object */
    private void deserializeInventory(User user, String inventoryStr) {
        // parse inventory CSV and add items to the user's inventory
        if (inventoryStr != null && !inventoryStr.isEmpty()) {
            Arrays.stream(inventoryStr.split(","))
                  .map(provider.ShopItemProvider::getItemByName)
                  .filter(Objects::nonNull)
                  .forEach(item -> user.getInventory().addItem(item));
        }
    }
    
    /** Convert skills string to Skill list */
    private void deserializeSkills(User user, String skillsStr) {
        // parse skills CSV and add Skill objects to the user
        if (skillsStr != null && !skillsStr.isEmpty()) {
            Arrays.stream(skillsStr.split(","))
                  .forEach(skillName -> user.getSkills().add(new Skill(skillName)));
        }
    }
    
    /** Convert user skills to string for database storage */
    private String serializeSkills(User user) {
        // serialize user's skills to a CSV string
        return user.getSkills().stream()
                   .map(Skill::getName)
                   .reduce((a, b) -> a + "," + b)
                   .orElse("");
    }
}
