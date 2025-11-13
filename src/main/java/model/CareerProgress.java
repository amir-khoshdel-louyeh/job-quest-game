package model;

// This class stores level, experience and reputation.
// It is used by User class (Composition).
public class CareerProgress {
    public static final int MAX_LEVEL = 100;

    private int level = 1;
    private int experience = 0;
    private int reputation = 0; // 0 to 100

    // return level
    public int getLevel() {
        return level;
    }

    // set level between 1 and 100
    public void setLevel(int newLevel) {
        if (newLevel < 1) {
            newLevel = 1;
        }
        if (newLevel > MAX_LEVEL) {
            newLevel = MAX_LEVEL;
        }
        this.level = newLevel;
    }

    // return experience
    public int getExperience() {
        return experience;
    }

    // set experience, check for level up
    // returns true if level increased
    public boolean setExperience(int newExp) {
        if (newExp < 0) {
            newExp = 0;
        }
        this.experience = newExp;
        return checkLevelUp();
    }

    // add experience, check for level up
    public boolean addExperience(int amount) {
        if (amount <= 0) {
            return false;
        }
        this.experience = this.experience + amount;
        return checkLevelUp();
    }

    // private helper: if enough XP, go to next level
    private boolean checkLevelUp() {
        int need = getRequiredExperienceForNextLevel();
        if (experience >= need && level < MAX_LEVEL) {
            level = level + 1;
            experience = experience - need;
            return true;
        }
        return false;
    }

    // how much XP needed for next level
    public int getRequiredExperienceForNextLevel() {
        return level * 1000;
    }

    // XP as percent 0-100
    public int getExperiencePercentage() {
        int need = getRequiredExperienceForNextLevel();
        double percent = (experience / (double) need) * 100;
        return (int) percent;
    }

    // return reputation
    public int getReputation() {
        return reputation;
    }

    // set reputation 0-100
    public void setReputation(int newRep) {
        if (newRep < 0) {
            newRep = 0;
        }
        if (newRep > 100) {
            newRep = 100;
        }
        this.reputation = newRep;
    }

    // add reputation
    public void addReputation(int amount) {
        setReputation(reputation + amount);
    }

    // return title based on reputation, uses simple if-else
    public String getReputationTitle() {
        if (reputation >= 90) {
            return "Legendary";
        }
        if (reputation >= 75) {
            return "Expert";
        }
        if (reputation >= 60) {
            return "Professional";
        }
        if (reputation >= 40) {
            return "Skilled";
        }
        if (reputation >= 20) {
            return "Beginner";
        }
        return "Newbie";
    }
}
