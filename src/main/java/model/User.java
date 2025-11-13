package model;

import java.util.ArrayList;
import java.util.List;

// This class represents one player in the game.
// It shows Encapsulation: all fields are private, we use getters and setters.
// It also shows Composition: User HAS-A Wallet, Vitals, CareerProgress, Inventory.
public class User {
    // basic info
    private String username;
    private String password;
    // profession of player, example Programmer or Doctor
    // Identity is an interface, so this shows Polymorphism
    private Identity identity;
    // money is stored in Wallet object
    private Wallet wallet;
    // health and energy are stored in Vitals object
    private Vitals vitals;
    // level and reputation are stored in CareerProgress object
    private CareerProgress progress;
    // items of player
    private Inventory inventory;
    // skills that player has learned
    private ArrayList<Skill> skills;

    private long blockedUntil = 0; // time until player is blocked
    private long lastSicknessTime = 0; // time of last sickness
    private long totalPlayTime = 0; // total time played

    private int totalJobsCompleted = 0; // how many jobs done
    private int currentStreak = 0; // daily login streak
    private int maxStreak = 0; // best streak
    private long lastLoginDate = 0; // last login time
    private ArrayList<String> unlockedAchievements = new ArrayList<String>();
    private ArrayList<Challenge> activeChallenges = new ArrayList<Challenge>();

    // Constructor: create a new user
    public User(String username, String password, Identity identity, int balance) {
        this.username = username;
        this.password = password;
        this.identity = identity;
        this.wallet = new Wallet(balance);
        this.vitals = new Vitals();
        this.progress = new CareerProgress();
        this.inventory = new Inventory();
        this.skills = new ArrayList<Skill>();
    }

    // return username
    public String getUsername() {
        return username;
    }

    // set username
    public void setUsername(String username) {
        this.username = username;
    }

    // return password
    public String getPassword() {
        return password;
    }

    // set password
    public void setPassword(String password) {
        this.password = password;
    }

    // return identity (profession)
    public Identity getIdentity() {
        return identity;
    }

    // set identity
    public void setIdentity(Identity identity) {
        this.identity = identity;
    }

    // return current money
    public int getBalance() {
        return wallet.getBalance();
    }

    // add money
    public void deposit(int amount) {
        wallet.deposit(amount);
    }

    // take away money
    public void withdraw(int amount) {
        wallet.withdraw(amount);
    }

    // return health
    public int getHealth() {
        return vitals.getHealth();
    }

    // increase health
    public void gainHealth(int amount) {
        vitals.gainHealth(amount);
    }

    // decrease health
    public void loseHealth(int amount) {
        vitals.loseHealth(amount);
    }

    // return energy
    public int getEnergy() {
        return vitals.getEnergy();
    }

    // increase energy
    public void gainEnergy(int amount) {
        vitals.gainEnergy(amount);
    }

    // decrease energy
    public void loseEnergy(int amount) {
        vitals.loseEnergy(amount);
    }

    // return inventory object
    public Inventory getInventory() {
        return inventory;
    }

    // return copy of skills list
    // Uses simple for loop, no stream, no unmodifiableList
    public List<Skill> getSkills() {
        ArrayList<Skill> copy = new ArrayList<Skill>();
        for (int i = 0; i < skills.size(); i++) {
            copy.add(skills.get(i));
        }
        return copy;
    }

    // replace skills list
    // Uses simple for loop
    public void setSkills(List<Skill> newSkills) {
        skills = new ArrayList<Skill>();
        if (newSkills != null) {
            for (int i = 0; i < newSkills.size(); i++) {
                skills.add(newSkills.get(i));
            }
        }
    }

    // add one skill if not already there
    // returns true if skill was added
    public boolean addSkill(Skill skill) {
        if (skill == null) {
            return false;
        }
        if (skills.contains(skill)) {
            return false;
        }
        skills.add(skill);
        return true;
    }

    // check if player has this skill object
    public boolean hasSkill(Skill skill) {
        if (skill == null) {
            return false;
        }
        return skills.contains(skill);
    }

    // check if player has skill by name, uses simple for loop
    public boolean hasSkill(String skillName) {
        if (skillName == null) {
            return false;
        }
        for (int i = 0; i < skills.size(); i++) {
            Skill s = skills.get(i);
            if (s.getName().equals(skillName)) {
                return true;
            }
        }
        return false;
    }

    // return total play time
    public long getTotalPlayTime() {
        return totalPlayTime;
    }

    // set total play time
    public void setTotalPlayTime(long totalPlayTime) {
        this.totalPlayTime = totalPlayTime;
    }

    // set blocked time
    public void setBlockedUntil(long time) {
        this.blockedUntil = time;
    }

    // get blocked time
    public long getBlockedUntil() {
        return blockedUntil;
    }

    // check if user is blocked now
    public boolean isBlocked() {
        long now = System.currentTimeMillis();
        if (now < blockedUntil) {
            return true;
        } else {
            return false;
        }
    }

    // set last sickness time
    public void setLastSicknessTime(long time) {
        this.lastSicknessTime = time;
    }

    // get last sickness time
    public long getLastSicknessTime() {
        return lastSicknessTime;
    }

    // return level
    public int getLevel() {
        return progress.getLevel();
    }

    // set level
    public void setLevel(int level) {
        progress.setLevel(level);
    }

    // return experience
    public int getExperience() {
        return progress.getExperience();
    }

    // set experience, refill health and energy if level up
    public void setExperience(int experience) {
        boolean levelUp = progress.setExperience(experience);
        if (levelUp) {
            vitals.restoreFull();
        }
    }

    // add experience
    public void addExperience(int amount) {
        boolean levelUp = progress.addExperience(amount);
        if (levelUp) {
            vitals.restoreFull();
        }
    }

    // how much XP needed for next level
    public int getRequiredExperienceForNextLevel() {
        return progress.getRequiredExperienceForNextLevel();
    }

    // XP as percent
    public int getExperiencePercentage() {
        return progress.getExperiencePercentage();
    }

    // return reputation 0-100
    public int getReputation() {
        return progress.getReputation();
    }

    // set reputation
    public void setReputation(int reputation) {
        progress.setReputation(reputation);
    }

    // add reputation
    public void addReputation(int amount) {
        progress.addReputation(amount);
    }

    // return title like Beginner, Expert
    public String getReputationTitle() {
        return progress.getReputationTitle();
    }

    // return total money earned in life
    public int getTotalMoneyEarned() {
        return wallet.getTotalMoneyEarned();
    }

    // set total money earned
    public void setTotalMoneyEarned(int total) {
        wallet.setTotalMoneyEarned(total);
    }

    // add to total money earned
    public void addToTotalMoneyEarned(int amount) {
        wallet.addToTotalMoneyEarned(amount);
    }

    // return total jobs completed
    public int getTotalJobsCompleted() {
        return totalJobsCompleted;
    }

    // set total jobs completed
    public void setTotalJobsCompleted(int total) {
        this.totalJobsCompleted = total;
    }

    // add one to jobs completed
    public void incrementJobsCompleted() {
        this.totalJobsCompleted++;
    }

    // return current streak
    public int getCurrentStreak() {
        return currentStreak;
    }

    // set streak, update max streak too
    public void setCurrentStreak(int streak) {
        this.currentStreak = streak;
        if (streak > maxStreak) {
            maxStreak = streak;
        }
    }

    // return max streak
    public int getMaxStreak() {
        return maxStreak;
    }

    // set max streak
    public void setMaxStreak(int maxStreak) {
        this.maxStreak = maxStreak;
    }

    // return last login date
    public long getLastLoginDate() {
        return lastLoginDate;
    }

    // set last login date
    public void setLastLoginDate(long date) {
        this.lastLoginDate = date;
    }

    // return copy of unlocked achievements
    // Uses simple for loop
    public List<String> getUnlockedAchievements() {
        ArrayList<String> copy = new ArrayList<String>();
        for (int i = 0; i < unlockedAchievements.size(); i++) {
            copy.add(unlockedAchievements.get(i));
        }
        return copy;
    }

    // replace unlocked achievements
    public void setUnlockedAchievements(List<String> list) {
        unlockedAchievements = new ArrayList<String>();
        if (list != null) {
            for (int i = 0; i < list.size(); i++) {
                unlockedAchievements.add(list.get(i));
            }
        }
    }

    // unlock one achievement
    public void unlockAchievement(String id) {
        if (!unlockedAchievements.contains(id)) {
            unlockedAchievements.add(id);
        }
    }

    // check if achievement is unlocked
    public boolean hasAchievement(String id) {
        return unlockedAchievements.contains(id);
    }

    // return copy of active challenges
    public List<Challenge> getActiveChallenges() {
        ArrayList<Challenge> copy = new ArrayList<Challenge>();
        for (int i = 0; i < activeChallenges.size(); i++) {
            copy.add(activeChallenges.get(i));
        }
        return copy;
    }

    // replace active challenges
    public void setActiveChallenges(List<Challenge> list) {
        activeChallenges = new ArrayList<Challenge>();
        if (list != null) {
            for (int i = 0; i < list.size(); i++) {
                activeChallenges.add(list.get(i));
            }
        }
    }

    // add one challenge if not already there
    public void addChallenge(Challenge challenge) {
        if (challenge == null) {
            return;
        }
        if (!hasChallenge(challenge.getId())) {
            activeChallenges.add(challenge);
        }
    }

    // add many challenges, skip duplicates, uses simple for loop
    public void addChallenges(List<Challenge> list) {
        if (list == null) {
            return;
        }
        for (int i = 0; i < list.size(); i++) {
            Challenge c = list.get(i);
            addChallenge(c);
        }
    }

    // check if we have challenge with this id, uses for loop (no stream)
    public boolean hasChallenge(String challengeId) {
        if (challengeId == null) {
            return false;
        }
        for (int i = 0; i < activeChallenges.size(); i++) {
            Challenge c = activeChallenges.get(i);
            if (challengeId.equals(c.getId())) {
                return true;
            }
        }
        return false;
    }

    // remove completed daily challenges, uses simple loop backwards
    public void pruneCompletedDailyChallenges() {
        // loop backwards so removing is safe
        for (int i = activeChallenges.size() - 1; i >= 0; i--) {
            Challenge c = activeChallenges.get(i);
            if (c.getId().startsWith("daily_") && c.isCompleted()) {
                activeChallenges.remove(i);
            }
        }
    }

    // remove one challenge
    public void removeChallenge(Challenge challenge) {
        activeChallenges.remove(challenge);
    }
}
