package model.achievement;

import model.Achievement;
import model.User;

// Rewards holding a lot of money at once.
public class RichPlayerAchievement extends Achievement {
    public RichPlayerAchievement(String id, String name, String description, int rewardMoney, int requiredValue) {
        super(id, name, description, rewardMoney, AchievementType.RICH_PLAYER, requiredValue);
    }

    public boolean checkRequirement(User user) {
        return user.getBalance() >= getRequiredValue();
    }

    // Progress shown as a number from 0 to 100.
    public int getProgress(User user) {
        int current = user.getBalance();
        return Math.min(100, (current * 100) / getRequiredValue());
    }
}
