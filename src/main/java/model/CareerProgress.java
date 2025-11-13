package model;

// Tracks level, experience, and reputation.
public class CareerProgress {
    public static final int MAX_LEVEL = 100;

    private int level = 1;
    private int experience = 0;
    private int reputation = 0;

    public int getLevel() {
        return level;
    }

    // Keeps the level between 1 and the max.
    public void setLevel(int newLevel) {
        if (newLevel < 1) {
            newLevel = 1;
        }
        if (newLevel > MAX_LEVEL) {
            newLevel = MAX_LEVEL;
        }
        this.level = newLevel;
    }

    public int getExperience() {
        return experience;
    }

    // Returns true when there is enough XP to move up one level.
    public boolean setExperience(int newExp) {
        if (newExp < 0) {
            newExp = 0;
        }
        this.experience = newExp;
        return checkLevelUp();
    }

    // Returns true when there is enough XP to move up one level.
    public boolean addExperience(int amount) {
        if (amount <= 0) {
            return false;
        }
        this.experience = this.experience + amount;
        return checkLevelUp();
    }

    // Moves up one level when the XP reaches what the next level needs.
    private boolean checkLevelUp() {
        int need = getRequiredExperienceForNextLevel();
        if (experience >= need && level < MAX_LEVEL) {
            level = level + 1;
            experience = experience - need;
            return true;
        }
        return false;
    }

    public int getRequiredExperienceForNextLevel() {
        return level * 1000;
    }

    // XP shown as a number from 0 to 100.
    public int getExperiencePercentage() {
        int need = getRequiredExperienceForNextLevel();
        double percent = (experience / (double) need) * 100;
        return (int) percent;
    }

    public int getReputation() {
        return reputation;
    }

    // Keeps reputation between 0 and 100.
    public void setReputation(int newRep) {
        if (newRep < 0) {
            newRep = 0;
        }
        if (newRep > 100) {
            newRep = 100;
        }
        this.reputation = newRep;
    }

    public void addReputation(int amount) {
        setReputation(reputation + amount);
    }

    // Picks a title based on the reputation score.
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
