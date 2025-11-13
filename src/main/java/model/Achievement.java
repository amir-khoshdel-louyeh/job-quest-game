package model;

// An unlockable goal with a reward.
public abstract class Achievement {
    private final String id;
    private final String name;
    private final String description;
    private final int rewardMoney;
    private final AchievementType type;
    private final int requiredValue;
    private boolean unlocked;

    public enum AchievementType {
        MONEY_EARNED,
        JOBS_COMPLETED,
        SKILLS_LEARNED,
        DAYS_PLAYED,
        HEALTH_MAINTAINED,
        ENERGY_EFFICIENT,
        RICH_PLAYER,
        SKILL_MASTER,
        WORKAHOLIC
    }

    public Achievement(String id, String name, String description, int rewardMoney, 
                      AchievementType type, int requiredValue) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.rewardMoney = rewardMoney;
        this.type = type;
        this.requiredValue = requiredValue;
        this.unlocked = false;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public int getRewardMoney() { return rewardMoney; }
    public AchievementType getType() { return type; }
    public int getRequiredValue() { return requiredValue; }
    public boolean isUnlocked() { return unlocked; }

    public void unlock() { this.unlocked = true; }
    public abstract boolean checkRequirement(model.User user);
    public abstract int getProgress(model.User user);

    public String toString() {
        return (unlocked ? "✅ " : "🔒 ") + name + " - " + description;
    }
}
