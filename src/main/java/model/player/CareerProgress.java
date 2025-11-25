package model.player;

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
    // Private: levels are earned via XP, not forged (see addExperience).
    private void setLevel(int newLevel) {
        if (newLevel < 1) { newLevel = 1;}
        if (newLevel > MAX_LEVEL) { newLevel = MAX_LEVEL;}
        this.level = newLevel;
    }

    public int getExperience() {
        return experience;
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

    // 10-hour run tuning: 200 XP per level keeps L15 within ~200 jobs.
    public int getRequiredExperienceForNextLevel() {
        return level * 200;
    }

    // XP shown as a number from 0 to 100.
    public int getExperiencePercentage() {
        return utils.ProgressUtil.percent(experience, getRequiredExperienceForNextLevel());
    }

    public int getReputation() {
        return reputation;
    }

    // Keeps reputation between 0 and 100.
    // Private: game logic must use addReputation so the 0..100 rule stays inside.
    private void setReputation(int newRep) {
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

    // ----- Demo scenario -----
    public void debugSetLevel(int newLevel) {
        setLevel(newLevel);
    }

    public void debugSetReputation(int newRep) {
        setReputation(newRep);
    }
}
