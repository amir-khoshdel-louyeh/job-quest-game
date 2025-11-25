package model.player;

// Tracks level, experience, and reputation.
// Used by model.player.User (sole owner); read via User/UserController/StatsController.
public class CareerProgress {
    public static final int MAX_LEVEL = 100;
    public static final int MAX_REPUTATION = 100;

    private int level = 1;
    private int experience = 0;
    private int reputation = 0;

    // ----- Getter -----
    public int getLevel() {
        return level;
    }

    public int getExperience() {
        return experience;
    }

    public int getReputation() {
        return reputation;
    }

    // ----- Setter -----
    private void setLevel(int newLevel) {
        if (newLevel < 1) { newLevel = 1;}
        if (newLevel > MAX_LEVEL) { newLevel = MAX_LEVEL;}
        this.level = newLevel;
    }

    private void setReputation(int newRep) {
        if (newRep < 0) {
            newRep = 0;
        }
        if (newRep > MAX_REPUTATION) {
            newRep = MAX_REPUTATION;
        }
        this.reputation = newRep;
    }

    private void setExperience(int newExp) {
        if (newExp < 0) {
            newExp = 0;
        }
        this.experience = newExp;
    }

    // ----- Commands -----
    public boolean addExperience(int amount) {
        if (amount <= 0) {
            return false;
        }
        setExperience(experience + amount);
        return checkLevelUp();
    }

    private boolean checkLevelUp() {
        int need = getRequiredExperienceForNextLevel();
        if (experience >= need && level < MAX_LEVEL) {
            setLevel(level + 1);
            setExperience(experience - need);
            return true;
        }
        return false;
    }

    public void gainReputation(int amount) {
        if (amount > 0) {
            setReputation(reputation + amount);
        }
    }

    public void loseReputation(int amount) {
        if (amount > 0) {
            setReputation(reputation - amount);
        }
    }

    // ----- Queries -----
    public int getRequiredExperienceForNextLevel() {
        return level * 200;
    }

    public int getExperiencePercentage() {
        return utils.ProgressUtil.percent(experience, getRequiredExperienceForNextLevel());
    }

    // ----- Cheat harness -----
    // Package-private: only User/CheatService (same package) may call these.
    void setLevelForCheat(int newLevel) {
        setLevel(newLevel);
    }

    void setReputationForCheat(int newRep) {
        setReputation(newRep);
    }
}
