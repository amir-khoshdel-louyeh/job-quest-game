package model;

/**
 * Domain model representing a player/user — now a composition root (facade).
 *
 * <p>OOP Step 6: money lives in {@link Wallet}, health/energy in
 * {@link Vitals}, level/XP/reputation in {@link CareerProgress}. This class
 * keeps identity, inventory, skills, streaks and social state, delegating
 * stat calls so the public API is unchanged (zero caller breakage).
 * Streaks/statistics are the next split candidates.</p>
 */

import java.util.ArrayList;
import java.util.List;

public class User {
    private String username;
    private String password;
    private Identity identity;
    private final Wallet wallet;
    private final Vitals vitals = new Vitals();
    private final CareerProgress progress = new CareerProgress();
    private Inventory inventory; // Use the Inventory object
    private List<Skill> skills;

    private long blockedUntil = 0;      // timestamp end of block in milliseconds
    private long lastSicknessTime = 0;  // timestamp of last sickness
    private long totalPlayTime = 0;     // Total time played in milliseconds

    // Career activity (next split candidates — kept here for this step)
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
        this.wallet = new Wallet(balance);
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
    public int getBalance() { return wallet.getBalance(); }

    // add amount to balance
    public void deposit(int amount) { wallet.deposit(amount); }

    // subtract amount from balance
    public void withdraw(int amount) { wallet.withdraw(amount); }

    // return current health
    public int getHealth() { return vitals.getHealth(); }

    // increase health by amount
    public void gainHealth(int amount) { vitals.gainHealth(amount); }

    // decrease health by amount
    public void loseHealth(int amount) { vitals.loseHealth(amount); }

    // return current energy
    public int getEnergy() { return vitals.getEnergy(); }

    // increase energy by amount
    public void gainEnergy(int amount) { vitals.gainEnergy(amount); }

    // decrease energy by amount
    public void loseEnergy(int amount) { vitals.loseEnergy(amount); }

    // return the Inventory object
    public Inventory getInventory() { return inventory; } // Return the Inventory object

    // return list of skills
    public List<Skill> getSkills() { return java.util.Collections.unmodifiableList(skills); }
    // replace skills list
    public void setSkills(List<Skill> skills) {
        this.skills = new ArrayList<>(skills != null ? skills : List.of());
    }

    /**
     * OOP Step 4: encapsulated mutation — callers add via this method,
     * never via {@code getSkills().add(...)} (that list is now unmodifiable).
     * @return true when the skill was newly added
     */
    public boolean addSkill(Skill skill) {
        if (skill == null || skills.contains(skill)) return false;
        skills.add(skill);
        return true;
    }

    /**
     * OOP Step 1: encapsulated skill check — callers ask the User,
     * they don't stream/map its internal list to Strings.
     */
    public boolean hasSkill(Skill skill) {
        if (skill == null) return false;
        return skills.contains(skill);
    }

    /** String convenience for DB/UI boundaries. */
    public boolean hasSkill(String skillName) {
        if (skillName == null) return false;
        return skills.contains(new Skill(skillName));
    }

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
    
    // ---------------- Progression System (delegates to CareerProgress) ----------------
    // return player level
    public int getLevel() { return progress.getLevel(); }
    // set player level
    public void setLevel(int level) { progress.setLevel(level); }
    
    // return current experience
    public int getExperience() { return progress.getExperience(); }
    // set experience and check for level up
    public void setExperience(int experience) {
        if (progress.setExperience(experience)) vitals.restoreFull();
    }
    
    // add experience and check level up
    public void addExperience(int amount) {
        if (progress.addExperience(amount)) vitals.restoreFull();
    }
    
    // compute XP required for next level
    public int getRequiredExperienceForNextLevel() {
        return progress.getRequiredExperienceForNextLevel();
    }
    
    // return experience as percentage of next level
    public int getExperiencePercentage() {
        return progress.getExperiencePercentage();
    }
    
    // ---------------- Reputation ----------------
    // return reputation (0-100)
    public int getReputation() { return progress.getReputation(); }
    // set reputation clamped to 0-100
    public void setReputation(int reputation) { progress.setReputation(reputation); }
    // add to reputation
    public void addReputation(int amount) { progress.addReputation(amount); }
    
    // return a title describing reputation level
    public String getReputationTitle() { return progress.getReputationTitle(); }
    
    // ---------------- Statistics ----------------
    // return total lifetime money earned
    public int getTotalMoneyEarned() { return wallet.getTotalMoneyEarned(); }
    // set total lifetime money earned
    public void setTotalMoneyEarned(int totalMoneyEarned) {
        wallet.setTotalMoneyEarned(totalMoneyEarned);
    }
    // add to lifetime money earned
    public void addToTotalMoneyEarned(int amount) {
        wallet.addToTotalMoneyEarned(amount);
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
        return java.util.Collections.unmodifiableList(unlockedAchievements);
    }
    // replace unlocked achievements
    public void setUnlockedAchievements(List<String> achievements) {
        this.unlockedAchievements = new ArrayList<>(achievements != null ? achievements : List.of());
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
        return java.util.Collections.unmodifiableList(activeChallenges);
    }
    // replace active challenges
    public void setActiveChallenges(List<Challenge> challenges) {
        this.activeChallenges = new ArrayList<>(challenges != null ? challenges : List.of());
    }
    // add a challenge to active list
    public void addChallenge(Challenge challenge) {
        if (challenge != null && !hasChallenge(challenge.getId())) {
            activeChallenges.add(challenge);
        }
    }
    /**
     * OOP Step 4: bulk add without exposing the live list.
     * Skips duplicates by id.
     */
    public void addChallenges(java.util.Collection<Challenge> challenges) {
        if (challenges == null) return;
        for (Challenge c : challenges) addChallenge(c);
    }
    /** Check by id (encapsulated lookup, no stream in callers). */
    public boolean hasChallenge(String challengeId) {
        if (challengeId == null) return false;
        return activeChallenges.stream().anyMatch(c -> challengeId.equals(c.getId()));
    }
    /**
     * OOP Step 4: behavior lives in the owner — prune completed daily
     * challenges internally instead of callers calling
     * {@code getActiveChallenges().removeIf(...)}.
     */
    public void pruneCompletedDailyChallenges() {
        activeChallenges.removeIf(q ->
            q.getId().startsWith("daily_") && q.isCompleted()
        );
    }
    // remove a challenge from active list
    public void removeChallenge(Challenge challenge) {
        activeChallenges.remove(challenge);
    }
}
