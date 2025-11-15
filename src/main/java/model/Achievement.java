package model;

// An unlockable goal with a reward.
public abstract class Achievement {
    private final String id;
    private final String name;
    private final String description;
    private final int rewardMoney;
    private final AchievementType type;
    private final int requiredValue;

    public enum AchievementType {
        MONEY_EARNED,
        JOBS_COMPLETED,
        SKILLS_LEARNED,
        RICH_PLAYER
    }

    public Achievement(String id, String name, String description, int rewardMoney,
                      AchievementType type, int requiredValue) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.rewardMoney = rewardMoney;
        this.type = type;
        this.requiredValue = requiredValue;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public int getRewardMoney() { return rewardMoney; }
    public AchievementType getType() { return type; }
    public int getRequiredValue() { return requiredValue; }

    public boolean isUnlocked(User user) {
        if (user == null) {
            return false;
        }
        return user.hasAchievement(id);
    }

    protected abstract int getCurrentValue(User user);

    public boolean checkRequirement(User user) {
        return getCurrentValue(user) >= getRequiredValue();
    }

    public int getProgress(User user) {
        if (getRequiredValue() <= 0) {
            return 100;
        }
        long current = getCurrentValue(user);
        return (int) Math.min(100, (current * 100) / getRequiredValue());
    }

    public String toString() {
        return name + " - " + description;
    }

    public String toString(User user) {
        return (isUnlocked(user) ? "✅ " : "🔒 ") + name + " - " + description;
    }
}
