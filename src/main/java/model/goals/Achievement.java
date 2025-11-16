package model.goals;

import model.player.User;

// An unlockable goal with a reward.
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
        return utils.ProgressUtil.percent(getCurrentValue(user), getRequiredValue());
    }

    public String toString() {
        return name + " - " + description;
    }
}
