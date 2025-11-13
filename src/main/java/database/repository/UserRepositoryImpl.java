package database.repository;

import database.DatabaseConnection;
import model.Challenge;
import model.Identity;
import model.Skill;
import model.User;
import model.identity.Doctor;
import model.identity.LogoDesigner;
import model.identity.Programmer;
import model.identity.Typist;
import provider.ChallengeProvider;
import provider.ShopItemProvider;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

// This class saves and loads users from MySQL database.
// It shows Encapsulation: SQL code is hidden here.
public class UserRepositoryImpl implements UserRepository {
    private DatabaseConnection dbConnection;

    // Default constructor
    public UserRepositoryImpl() {
        this.dbConnection = DatabaseConnection.getInstance();
    }

    public UserRepositoryImpl(DatabaseConnection dbConnection) {
        this.dbConnection = dbConnection;
    }

    // Find user by name, return null if not found
    public User findByUsername(String username) throws SQLException {
        String sql = "SELECT * FROM users WHERE username = ?";
        try {
            Connection conn = dbConnection.createConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            User result = null;
            if (rs.next()) {
                result = makeUserFromRow(rs);
            }
            rs.close();
            stmt.close();
            conn.close();
            return result;
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    // Save new user
    public boolean save(User user) throws SQLException {
        String sql = "INSERT INTO users (username, password, identity, balance, health, energy, " +
                     "inventory, skills, blocked_until, last_sickness_time, total_play_time, " +
                     "level, experience, reputation, total_money_earned, total_jobs_completed, " +
                     "current_streak, max_streak, last_login_date, unlocked_achievements, active_challenges) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            Connection conn = dbConnection.createConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            fillInsert(stmt, user);
            stmt.executeUpdate();
            stmt.close();
            conn.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    // Update existing user
    public boolean update(User user) throws SQLException {
        String sql = "UPDATE users SET balance = ?, health = ?, energy = ?, inventory = ?, " +
                     "skills = ?, blocked_until = ?, last_sickness_time = ?, total_play_time = ?, " +
                     "level = ?, experience = ?, reputation = ?, total_money_earned = ?, " +
                     "total_jobs_completed = ?, current_streak = ?, max_streak = ?, " +
                     "last_login_date = ?, unlocked_achievements = ?, active_challenges = ? " +
                     "WHERE username = ?";
        try {
            Connection conn = dbConnection.createConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, user.getBalance());
            stmt.setInt(2, user.getHealth());
            stmt.setInt(3, user.getEnergy());
            stmt.setString(4, joinNames(user.getInventory().getItemNames()));
            stmt.setString(5, skillsToString(user));
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
            stmt.setString(17, joinNames(user.getUnlockedAchievements()));
            stmt.setString(18, challengesToString(user));
            stmt.setString(19, user.getUsername());
            int rows = stmt.executeUpdate();
            stmt.close();
            conn.close();
            if (rows > 0) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    // Delete user by name
    public boolean delete(String username) throws SQLException {
        String sql = "DELETE FROM users WHERE username = ?";
        try {
            Connection conn = dbConnection.createConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            int rows = stmt.executeUpdate();
            stmt.close();
            conn.close();
            if (rows > 0) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    // Make User object from database row, uses simple if (no Math.max)
    private User makeUserFromRow(ResultSet rs) throws SQLException {
        String username = rs.getString("username");
        String password = rs.getString("password");
        String identityStr = rs.getString("identity");
        int balance = rs.getInt("balance");
        int health = rs.getInt("health");
        int energy = rs.getInt("energy");
        long blockedUntil = rs.getLong("blocked_until");
        long lastSickness = rs.getLong("last_sickness_time");
        String inventoryStr = rs.getString("inventory");
        String skillsStr = rs.getString("skills");
        long totalPlay = rs.getLong("total_play_time");

        Identity identity = nameToIdentity(identityStr);
        User user = new User(username, password, identity, balance);
        // set health with simple if
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
        user.setLastSicknessTime(lastSickness);
        user.setTotalPlayTime(totalPlay);

        // progression columns, use simple if for safe values
        int level = rs.getInt("level");
        if (level < 1) {
            level = 1;
        }
        user.setLevel(level);
        int exp = rs.getInt("experience");
        if (exp < 0) {
            exp = 0;
        }
        user.setExperience(exp);
        user.setReputation(rs.getInt("reputation"));
        int earned = rs.getInt("total_money_earned");
        if (earned < 0) {
            earned = 0;
        }
        user.setTotalMoneyEarned(earned);
        int jobs = rs.getInt("total_jobs_completed");
        if (jobs < 0) {
            jobs = 0;
        }
        user.setTotalJobsCompleted(jobs);
        int streak = rs.getInt("current_streak");
        if (streak < 0) {
            streak = 0;
        }
        user.setCurrentStreak(streak);
        int maxStreak = rs.getInt("max_streak");
        if (maxStreak < user.getMaxStreak()) {
            maxStreak = user.getMaxStreak();
        }
        user.setMaxStreak(maxStreak);
        user.setLastLoginDate(rs.getLong("last_login_date"));

        loadAchievements(user, rs.getString("unlocked_achievements"));
        loadChallenges(user, rs.getString("active_challenges"));
        loadInventory(user, inventoryStr);
        loadSkills(user, skillsStr);
        return user;
    }

    // Fill INSERT statement, uses simple helpers
    private void fillInsert(PreparedStatement stmt, User user) throws SQLException {
        stmt.setString(1, user.getUsername());
        stmt.setString(2, user.getPassword());
        stmt.setString(3, user.getIdentity().getProfessionName());
        stmt.setInt(4, user.getBalance());
        stmt.setInt(5, user.getHealth());
        stmt.setInt(6, user.getEnergy());
        stmt.setString(7, joinNames(user.getInventory().getItemNames()));
        stmt.setString(8, skillsToString(user));
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
        stmt.setString(20, joinNames(user.getUnlockedAchievements()));
        stmt.setString(21, challengesToString(user));
    }

    // Convert name to Identity with simple if-else (no switch expression)
    private Identity nameToIdentity(String name) {
        if (name == null) {
            return new Typist();
        }
        if (name.equals("Doctor")) {
            return new Doctor();
        } else if (name.equals("Programmer")) {
            return new Programmer();
        } else if (name.equals("LogoDesigner")) {
            return new LogoDesigner();
        } else if (name.equals("Typist")) {
            return new Typist();
        } else {
            System.out.println("Unknown identity: " + name + ", using Typist");
            return new Typist();
        }
    }

    // Join list with comma, uses for loop (no String.join)
    private String joinNames(List<String> list) {
        if (list == null || list.isEmpty()) {
            return "";
        }
        String result = "";
        for (int i = 0; i < list.size(); i++) {
            if (i > 0) {
                result = result + ",";
            }
            result = result + list.get(i);
        }
        return result;
    }

    // Load inventory from "a,b,c" string, uses for loop (no stream)
    private void loadInventory(User user, String text) {
        if (text == null || text.trim().equals("")) {
            return;
        }
        String[] parts = text.split(",");
        for (int i = 0; i < parts.length; i++) {
            String name = parts[i].trim();
            if (!name.equals("")) {
                model.Item item = ShopItemProvider.getItemByName(name);
                if (item != null) {
                    user.getInventory().addItem(item);
                }
            }
        }
    }

    // Load skills from string, uses for loop
    private void loadSkills(User user, String text) {
        if (text == null || text.trim().equals("")) {
            return;
        }
        String[] parts = text.split(",");
        for (int i = 0; i < parts.length; i++) {
            String name = parts[i].trim();
            if (!name.equals("")) {
                user.addSkill(new Skill(name));
            }
        }
    }

    // Skills to "a,b,c" string, uses for loop (no stream/reduce)
    private String skillsToString(User user) {
        List<Skill> skills = user.getSkills();
        String result = "";
        for (int i = 0; i < skills.size(); i++) {
            if (i > 0) {
                result = result + ",";
            }
            result = result + skills.get(i).getName();
        }
        return result;
    }

    // Challenges to "id|prog|done,..." string, uses for loop
    private String challengesToString(User user) {
        List<Challenge> list = user.getActiveChallenges();
        if (list == null || list.isEmpty()) {
            return "";
        }
        String result = "";
        for (int i = 0; i < list.size(); i++) {
            Challenge c = list.get(i);
            if (i > 0) {
                result = result + ",";
            }
            int done = 0;
            if (c.isCompleted()) {
                done = 1;
            }
            result = result + c.getId() + "|" + c.getCurrentProgress() + "|" + done;
        }
        return result;
    }

    // Load achievements from string
    private void loadAchievements(User user, String text) {
        if (text == null || text.trim().equals("")) {
            return;
        }
        ArrayList<String> ids = new ArrayList<String>();
        String[] parts = text.split(",");
        for (int i = 0; i < parts.length; i++) {
            String id = parts[i].trim();
            if (!id.equals("") && !ids.contains(id)) {
                ids.add(id);
            }
        }
        user.setUnlockedAchievements(ids);
    }

    // Load challenges, uses simple loops and if (no putIfAbsent, no streams)
    private void loadChallenges(User user, String text) {
        if (text == null || text.trim().equals("")) {
            return;
        }
        // build template map
        HashMap<String, Challenge> map = new HashMap<String, Challenge>();
        List<Challenge> main = ChallengeProvider.getMainChallenges();
        for (int i = 0; i < main.size(); i++) {
            Challenge t = main.get(i);
            if (!map.containsKey(t.getId())) {
                map.put(t.getId(), t);
            }
        }
        List<Challenge> daily = ChallengeProvider.getDailyChallenges();
        for (int i = 0; i < daily.size(); i++) {
            Challenge t = daily.get(i);
            if (!map.containsKey(t.getId())) {
                map.put(t.getId(), t);
            }
        }
        List<Challenge> weekly = ChallengeProvider.getWeeklyChallenges();
        for (int i = 0; i < weekly.size(); i++) {
            Challenge t = weekly.get(i);
            if (!map.containsKey(t.getId())) {
                map.put(t.getId(), t);
            }
        }
        ArrayList<Challenge> restored = new ArrayList<Challenge>();
        String[] entries = text.split(",");
        for (int i = 0; i < entries.length; i++) {
            String entry = entries[i];
            String[] p = entry.split("\\|");
            if (p.length == 0) {
                continue;
            }
            String id = p[0].trim();
            if (id.equals("")) {
                continue;
            }
            Challenge template = map.get(id);
            if (template == null) {
                continue;
            }
            Challenge copy = new Challenge(template.getId(), template.getName(), template.getDescription(),
                    template.getType(), template.getTargetValue(),
                    template.getRewardMoney(), template.getRewardExperience());
            if (p.length >= 2) {
                try {
                    int prog = Integer.parseInt(p[1].trim());
                    if (prog < 0) {
                        prog = 0;
                    }
                    copy.setCurrentProgress(prog);
                } catch (Exception e) {
                    // keep 0
                }
            }
            if (p.length >= 3) {
                if (p[2].trim().equals("1")) {
                    copy.setCompleted(true);
                }
            } else {
                if (copy.getCurrentProgress() >= copy.getTargetValue()) {
                    copy.setCompleted(true);
                }
            }
            restored.add(copy);
        }
        if (!restored.isEmpty()) {
            user.setActiveChallenges(restored);
        }
    }
}
