package model;

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
    private List<Quest> activeQuests = new ArrayList<>();

    public User(String username, String password, Identity identity, int balance) {
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
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public Identity getIdentity() { return identity; }
    public void setIdentity(Identity identity) { this.identity = identity; }

    public int getBalance() { return balance; }
    public void setBalance(int balance) { this.balance = balance; }

    public int getHealth() { return health; }
    public void setHealth(int health) { this.health = Math.max(0, Math.min(100, health)); }

    public int getEnergy() { return energy; }
    public void setEnergy(int energy) { this.energy = Math.max(0, Math.min(100_000, energy)); }

    public Inventory getInventory() { return inventory; } // Return the Inventory object

    public List<Skill> getSkills() { return skills; }
    public void setSkills(List<Skill> skills) { this.skills = skills; }

    public long getTotalPlayTime() { return totalPlayTime; }
    public void setTotalPlayTime(long totalPlayTime) { this.totalPlayTime = totalPlayTime; }

    // ---------------- Block / Unblock ----------------
    public void setBlockedUntil(long time) { this.blockedUntil = time; }
    public long getBlockedUntil() { return blockedUntil; }
    public boolean isBlocked() { return System.currentTimeMillis() < blockedUntil; }

    // ---------------- Last Sickness ----------------
    public void setLastSicknessTime(long time) { this.lastSicknessTime = time; }
    public long getLastSicknessTime() { return lastSicknessTime; }
    
    // ---------------- Progression System ----------------
    public int getLevel() { return level; }
    public void setLevel(int level) { this.level = level; }
    
    public int getExperience() { return experience; }
    public void setExperience(int experience) { 
        this.experience = experience;
        checkLevelUp();
    }
    
    public void addExperience(int amount) {
        this.experience += amount;
        checkLevelUp();
    }
    
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
    
    public int getRequiredExperienceForNextLevel() {
        return level * 1000; // Each level requires level * 1000 XP
    }
    
    public int getExperiencePercentage() {
        return (int) ((experience / (double) getRequiredExperienceForNextLevel()) * 100);
    }
    
    // ---------------- Reputation ----------------
    public int getReputation() { return reputation; }
    public void setReputation(int reputation) { 
        this.reputation = Math.max(0, Math.min(100, reputation)); 
    }
    public void addReputation(int amount) { 
        setReputation(reputation + amount); 
    }
    
    public String getReputationTitle() {
        if (reputation >= 90) return "Legendary";
        if (reputation >= 75) return "Expert";
        if (reputation >= 60) return "Professional";
        if (reputation >= 40) return "Skilled";
        if (reputation >= 20) return "Beginner";
        return "Novice";
    }
    
    // ---------------- Statistics ----------------
    public int getTotalMoneyEarned() { return totalMoneyEarned; }
    public void setTotalMoneyEarned(int totalMoneyEarned) { 
        this.totalMoneyEarned = totalMoneyEarned; 
    }
    public void addToTotalMoneyEarned(int amount) { 
        this.totalMoneyEarned += amount; 
    }
    
    public int getTotalJobsCompleted() { return totalJobsCompleted; }
    public void setTotalJobsCompleted(int totalJobsCompleted) { 
        this.totalJobsCompleted = totalJobsCompleted; 
    }
    public void incrementJobsCompleted() { 
        this.totalJobsCompleted++; 
    }
    
    // ---------------- Streak System ----------------
    public int getCurrentStreak() { return currentStreak; }
    public void setCurrentStreak(int currentStreak) { 
        this.currentStreak = currentStreak;
        if (currentStreak > maxStreak) {
            maxStreak = currentStreak;
        }
    }
    
    public int getMaxStreak() { return maxStreak; }
    public void setMaxStreak(int maxStreak) { this.maxStreak = maxStreak; }
    
    public long getLastLoginDate() { return lastLoginDate; }
    public void setLastLoginDate(long lastLoginDate) { 
        this.lastLoginDate = lastLoginDate; 
    }
    
    // ---------------- Achievements ----------------
    public List<String> getUnlockedAchievements() { 
        return unlockedAchievements; 
    }
    public void setUnlockedAchievements(List<String> achievements) { 
        this.unlockedAchievements = achievements; 
    }
    public void unlockAchievement(String achievementId) {
        if (!unlockedAchievements.contains(achievementId)) {
            unlockedAchievements.add(achievementId);
        }
    }
    public boolean hasAchievement(String achievementId) {
        return unlockedAchievements.contains(achievementId);
    }
    
    // ---------------- Quests ----------------
    public List<Quest> getActiveQuests() { 
        return activeQuests; 
    }
    public void setActiveQuests(List<Quest> quests) { 
        this.activeQuests = quests; 
    }
    public void addQuest(Quest quest) {
        activeQuests.add(quest);
    }
    public void removeQuest(Quest quest) {
        activeQuests.remove(quest);
    }
}
