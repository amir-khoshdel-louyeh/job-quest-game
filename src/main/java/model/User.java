package model;

/**
 * Domain model representing a player/user, their stats, progression and inventories.
 *
 * Follows Single Responsibility Principle by containing only user-related data and behavior.
 */

import java.util.ArrayList;
import java.util.List;

public class User {
    private String username;
    private String password;
    private Identity identity;
    private int balance;
    private int health;      // 0 to 100
    private int energy;      // 0 to 100,000
    private Inventory inventory; // Use the Inventory object
    private List<Skill> skills;

    private long blockedUntil = 0;      // timestamp end of block in milliseconds
    private long lastSicknessTime = 0;  // timestamp of last sickness
    private long totalPlayTime = 0;     // Total time played in milliseconds
    
    // New progression fields
    private int level = 1;              // Player level
    private int experience = 0;         // Current experience points
    private int reputation = 0;         // Reputation score (0-100)
    private int totalMoneyEarned = 0;   // Total money earned (lifetime)
    private int totalJobsCompleted = 0; // Total jobs completed
    private int currentStreak = 0;      // Current daily login streak
    private int maxStreak = 0;          // Maximum streak achieved
    private long lastLoginDate = 0;     // Last login timestamp
    private List<String> unlockedAchievements = new ArrayList<>();
    private List<Challenge> activeChallenges = new ArrayList<>();

    public User(String username, String password, Identity identity, int balance) {
    // construct a new User with identity and starting balance
        this.username = username;
        this.password = password;
        this.identity = identity;
        this.balance = balance;
        this.health = 100;
        this.energy = 100_000; // Initial energy
        this.inventory = new Inventory(); // Initialize the Inventory object
        this.skills = new ArrayList<>();
    }

    // ---------------- Getters & Setters ----------------
    // return username
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    // return password
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    // return identity
    public Identity getIdentity() { return identity; }
    public void setIdentity(Identity identity) { this.identity = identity; }

    // return current balance
    public int getBalance() { return balance; }
    private void setBalance(int balance) { this.balance = Math.max(0, balance); }

    // add amount to balance
    public void deposit(int amount) {
        if (amount > 0) setBalance(this.balance + amount);
    }

    // subtract amount from balance
    public void withdraw(int amount) {
        if (amount > 0) setBalance(this.balance - amount);
    }

    // return current health
    public int getHealth() { return health; }
    private void setHealth(int health) { this.health = Math.max(0, Math.min(100, health)); }

    // increase health by amount
    public void gainHealth(int amount) {
        if (amount > 0) setHealth(this.health + amount);
    }

    // decrease health by amount
    public void loseHealth(int amount) {
        if (amount > 0) setHealth(this.health - amount);
    }

    // return current energy
    public int getEnergy() { return energy; }
    private void setEnergy(int energy) { this.energy = Math.max(0, Math.min(100_000, energy)); }

    // increase energy by amount
    public void gainEnergy(int amount) {
        if (amount > 0) setEnergy(this.energy + amount);
    }

    // decrease energy by amount
    public void loseEnergy(int amount) {
        if (amount > 0) setEnergy(this.energy - amount);
    }

    // return the Inventory object
    public Inventory getInventory() { return inventory; } // Return the Inventory object

    // return list of skills
    public List<Skill> getSkills() { return skills; }
    // replace skills list
    public void setSkills(List<Skill> skills) { this.skills = skills; }

    // return total play time in ms
    public long getTotalPlayTime() { return totalPlayTime; }
    // set total play time in ms
    public void setTotalPlayTime(long totalPlayTime) { this.totalPlayTime = totalPlayTime; }

    // ---------------- Block / Unblock ----------------
    // set block-until timestamp
    public void setBlockedUntil(long time) { this.blockedUntil = time; }
    // get block-until timestamp
    public long getBlockedUntil() { return blockedUntil; }
    // check if user is currently blocked
    public boolean isBlocked() { return System.currentTimeMillis() < blockedUntil; }

    // ---------------- Last Sickness ----------------
    // set last sickness timestamp
    public void setLastSicknessTime(long time) { this.lastSicknessTime = time; }
    // get last sickness timestamp
    public long getLastSicknessTime() { return lastSicknessTime; }
    
    // ---------------- Progression System ----------------
    // return player level
    public int getLevel() { return level; }
    // set player level
    public void setLevel(int level) { this.level = level; }
    
    // return current experience
    public int getExperience() { return experience; }
    // set experience and check for level up
    public void setExperience(int experience) { 
        this.experience = experience;
        checkLevelUp();
    }
    
    // add experience and check level up
    public void addExperience(int amount) {
        this.experience += amount;
        checkLevelUp();
    }
    
    // check and handle level up
    private void checkLevelUp() {
        int requiredXP = getRequiredExperienceForNextLevel();
        if (experience >= requiredXP && level < 100) {
            level++;
            experience -= requiredXP;
            // Level up bonus
            setHealth(100);
            setEnergy(100_000);
        }
    }
    
    // compute XP required for next level
    public int getRequiredExperienceForNextLevel() {
        return level * 1000; // Each level requires level * 1000 XP
    }
    
    // return experience as percentage of next level
    public int getExperiencePercentage() {
        return (int) ((experience / (double) getRequiredExperienceForNextLevel()) * 100);
    }
    
    // ---------------- Reputation ----------------
    // return reputation (0-100)
    public int getReputation() { return reputation; }
    // set reputation clamped to 0-100
    public void setReputation(int reputation) { 
        this.reputation = Math.max(0, Math.min(100, reputation)); 
    }
    // add to reputation
    public void addReputation(int amount) { 
        setReputation(reputation + amount); 
    }
    
    // return a title describing reputation level
    public String getReputationTitle() {
        if (reputation >= 90) return "Legendary";
        if (reputation >= 75) return "Expert";
        if (reputation >= 60) return "Professional";
        if (reputation >= 40) return "Skilled";
        if (reputation >= 20) return "Beginner";
        return "Newbie";
    }
    
    // ---------------- Statistics ----------------
    // return total lifetime money earned
    public int getTotalMoneyEarned() { return totalMoneyEarned; }
    // set total lifetime money earned
    public void setTotalMoneyEarned(int totalMoneyEarned) { 
        this.totalMoneyEarned = totalMoneyEarned; 
    }
    // add to lifetime money earned
    public void addToTotalMoneyEarned(int amount) { 
        this.totalMoneyEarned += amount; 
    }
    
    // return total jobs completed
    public int getTotalJobsCompleted() { return totalJobsCompleted; }
    // set total jobs completed
    public void setTotalJobsCompleted(int totalJobsCompleted) { 
        this.totalJobsCompleted = totalJobsCompleted; 
    }
    // increment total jobs completed by one
    public void incrementJobsCompleted() { 
        this.totalJobsCompleted++; 
    }
    
    // ---------------- Streak System ----------------
    // return current daily streak
    public int getCurrentStreak() { return currentStreak; }
    // set current streak and update max if needed
    public void setCurrentStreak(int currentStreak) { 
        this.currentStreak = currentStreak;
        if (currentStreak > maxStreak) {
            maxStreak = currentStreak;
        }
    }
    
    // return max streak achieved
    public int getMaxStreak() { return maxStreak; }
    // set max streak
    public void setMaxStreak(int maxStreak) { this.maxStreak = maxStreak; }
    
    // return last login timestamp
    public long getLastLoginDate() { return lastLoginDate; }
    // set last login timestamp
    public void setLastLoginDate(long lastLoginDate) { 
        this.lastLoginDate = lastLoginDate; 
    }
    
    // ---------------- Achievements ----------------
    // return unlocked achievement ids
    public List<String> getUnlockedAchievements() { 
        return unlockedAchievements; 
    }
    // replace unlocked achievements
    public void setUnlockedAchievements(List<String> achievements) { 
        this.unlockedAchievements = achievements; 
    }
    // unlock an achievement by id
    public void unlockAchievement(String achievementId) {
        if (!unlockedAchievements.contains(achievementId)) {
            unlockedAchievements.add(achievementId);
        }
    }
    // check if achievement is unlocked
    public boolean hasAchievement(String achievementId) {
        return unlockedAchievements.contains(achievementId);
    }
    
    // ---------------- Challenges ----------------
    // return active challenges
    public List<Challenge> getActiveChallenges() { 
        return activeChallenges; 
    }
    // replace active challenges
    public void setActiveChallenges(List<Challenge> challenges) { 
        this.activeChallenges = challenges; 
    }
    // add a challenge to active list
    public void addChallenge(Challenge challenge) {
        activeChallenges.add(challenge);
    }
    // remove a challenge from active list
    public void removeChallenge(Challenge challenge) {
        activeChallenges.remove(challenge);
    }
}
