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
        // insert a new user record into the database (incl. Step-4 progression)
        String sql = "INSERT INTO users (username, password, identity, balance, health, energy, " +
                      "inventory, skills, blocked_until, last_sickness_time, total_play_time, " +
                      "level, experience, reputation, total_money_earned, total_jobs_completed, " +
                      "current_streak, max_streak, last_login_date, unlocked_achievements, active_challenges) " +
                      "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
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
        // update fields of an existing user record (incl. Step-4 progression)
        String sql = "UPDATE users SET balance = ?, health = ?, energy = ?, inventory = ?, " +
                      "skills = ?, blocked_until = ?, last_sickness_time = ?, total_play_time = ?, " +
                      "level = ?, experience = ?, reputation = ?, total_money_earned = ?, " +
                      "total_jobs_completed = ?, current_streak = ?, max_streak = ?, " +
                      "last_login_date = ?, unlocked_achievements = ?, active_challenges = ? " +
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
            stmt.setInt(9, user.getLevel());
            stmt.setInt(10, user.getExperience());
            stmt.setInt(11, user.getReputation());
            stmt.setInt(12, user.getTotalMoneyEarned());
            stmt.setInt(13, user.getTotalJobsCompleted());
            stmt.setInt(14, user.getCurrentStreak());
            stmt.setInt(15, user.getMaxStreak());
            stmt.setLong(16, user.getLastLoginDate());
            stmt.setString(17, String.join(",", user.getUnlockedAchievements()));
            stmt.setString(18, serializeChallenges(user));
            stmt.setString(19, user.getUsername());
            
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

        // Step-4 progression restore (tolerant: missing columns on very old
        // DBs read as 0/null — game still starts, next save backfills).
        try { user.setLevel(Math.max(1, rs.getInt("level"))); } catch (SQLException ignored) {}
        try { user.setExperience(Math.max(0, rs.getInt("experience"))); } catch (SQLException ignored) {}
        try { user.setReputation(rs.getInt("reputation")); } catch (SQLException ignored) {}
        try { user.setTotalMoneyEarned(Math.max(0, rs.getInt("total_money_earned"))); } catch (SQLException ignored) {}
        try { user.setTotalJobsCompleted(Math.max(0, rs.getInt("total_jobs_completed"))); } catch (SQLException ignored) {}
        try { user.setCurrentStreak(Math.max(0, rs.getInt("current_streak"))); } catch (SQLException ignored) {}
        try { user.setMaxStreak(Math.max(user.getMaxStreak(), rs.getInt("max_streak"))); } catch (SQLException ignored) {}
        try { user.setLastLoginDate(rs.getLong("last_login_date")); } catch (SQLException ignored) {}
        try { deserializeAchievements(user, rs.getString("unlocked_achievements")); } catch (SQLException ignored) {}
        try { deserializeChallenges(user, rs.getString("active_challenges")); } catch (SQLException ignored) {}
        
        deserializeInventory(user, inventoryStr);
        deserializeSkills(user, skillsStr);
        
        return user;
    }
    
    /** Set parameters for INSERT (incl. Step-4 progression) */
    private void setUserParameters(PreparedStatement stmt, User user) throws SQLException {
        // bind user fields to an INSERT prepared statement
        stmt.setString(1, user.getUsername());
        stmt.setString(2, user.getPassword());
        stmt.setString(3, user.getIdentity().getProfessionName());
        stmt.setInt(4, user.getBalance());
        stmt.setInt(5, user.getHealth());
        stmt.setInt(6, user.getEnergy());
        stmt.setString(7, String.join(",", user.getInventory().getItemNames()));
        stmt.setString(8, serializeSkills(user));
        stmt.setLong(9, user.getBlockedUntil());
        stmt.setLong(10, user.getLastSicknessTime());
        stmt.setLong(11, user.getTotalPlayTime());
        stmt.setInt(12, user.getLevel());
        stmt.setInt(13, user.getExperience());
        stmt.setInt(14, user.getReputation());
        stmt.setInt(15, user.getTotalMoneyEarned());
        stmt.setInt(16, user.getTotalJobsCompleted());
        stmt.setInt(17, user.getCurrentStreak());
        stmt.setInt(18, user.getMaxStreak());
        stmt.setLong(19, user.getLastLoginDate());
        stmt.setString(20, String.join(",", user.getUnlockedAchievements()));
        stmt.setString(21, serializeChallenges(user));
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

    /**
     * Serialize challenges as id|progress|completed CSV.
     * Templates (name/rewards) come from ChallengeProvider on load,
     * so only mutable progress is stored (Encapsulation).
     */
    private String serializeChallenges(User user) {
        if (user.getActiveChallenges() == null || user.getActiveChallenges().isEmpty()) {
            return "";
        }
        return user.getActiveChallenges().stream()
                .map(c -> c.getId() + "|" + c.getCurrentProgress() + "|" + (c.isCompleted() ? 1 : 0))
                .reduce((a, b) -> a + "," + b)
                .orElse("");
    }

    /** Restore unlocked achievement ids (CSV). */
    private void deserializeAchievements(User user, String achievementsStr) {
        if (achievementsStr == null || achievementsStr.isBlank()) return;
        java.util.List<String> ids = new java.util.ArrayList<>();
        for (String id : achievementsStr.split(",")) {
            String trimmed = id.trim();
            if (!trimmed.isEmpty() && !ids.contains(trimmed)) {
                ids.add(trimmed);
            }
        }
        user.setUnlockedAchievements(ids);
    }

    /**
     * Restore challenges: template by id from provider, then replay progress.
     * Unknown ids are skipped (forward-compatible with removed challenges).
     */
    private void deserializeChallenges(User user, String challengesStr) {
        if (challengesStr == null || challengesStr.isBlank()) return;
        java.util.Map<String, model.Challenge> templates = new java.util.HashMap<>();
        for (model.Challenge t : provider.ChallengeProvider.getMainChallenges()) {
            templates.putIfAbsent(t.getId(), t);
        }
        for (model.Challenge t : provider.ChallengeProvider.getDailyChallenges()) {
            templates.putIfAbsent(t.getId(), t);
        }
        for (model.Challenge t : provider.ChallengeProvider.getWeeklyChallenges()) {
            templates.putIfAbsent(t.getId(), t);
        }
        java.util.List<model.Challenge> restored = new java.util.ArrayList<>();
        for (String entry : challengesStr.split(",")) {
            String[] parts = entry.split("\\|");
            if (parts.length == 0) continue;
            String id = parts[0].trim();
            model.Challenge template = templates.get(id);
            if (template == null || id.isEmpty()) continue;
            model.Challenge copy = new model.Challenge(
                    template.getId(), template.getName(), template.getDescription(),
                    template.getType(), template.getTargetValue(),
                    template.getRewardMoney(), template.getRewardExperience());
            if (parts.length >= 2) {
                try {
                    copy.setCurrentProgress(Math.max(0, Integer.parseInt(parts[1].trim())));
                } catch (NumberFormatException ignored) {}
            }
            if (parts.length >= 3) {
                copy.setCompleted("1".equals(parts[2].trim()));
            } else if (copy.getCurrentProgress() >= copy.getTargetValue()) {
                copy.setCompleted(true);
            }
            restored.add(copy);
        }
        if (!restored.isEmpty()) {
            user.setActiveChallenges(restored);
        }
    }
}
