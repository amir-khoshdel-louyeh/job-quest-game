package model.goals;

// An unlockable goal with a reward.
// Depends only on AchievementStats (DIP), never on player.User:
// no goals -> player package cycle.
public abstract class Achievement {
    private final String id;
    private final String name;
    private final String description;
    private final int rewardMoney;
    private final int requiredValue;

    public Achievement(String id, String name, String description, int rewardMoney,
                      int requiredValue) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.rewardMoney = rewardMoney;
        this.requiredValue = requiredValue;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public int getRewardMoney() { return rewardMoney; }
    public int getRequiredValue() { return requiredValue; }

    public boolean isUnlocked(AchievementStats stats) {
        if (stats == null) {
            return false;
        }
        return stats.hasAchievement(id);
    }

    protected abstract int getCurrentValue(AchievementStats stats);

    public boolean checkRequirement(AchievementStats stats) {
        if (stats == null) {
            return false;
        }
        return getCurrentValue(stats) >= getRequiredValue();
    }

    public int getProgress(AchievementStats stats) {
        if (stats == null) {
            return 0;
        }
        return utils.ProgressUtil.percent(getCurrentValue(stats), getRequiredValue());
    }

    public String toString() {
        return name + " - " + description;
    }
}
