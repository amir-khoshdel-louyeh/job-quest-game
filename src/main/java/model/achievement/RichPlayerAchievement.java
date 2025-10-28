package model.achievement;

import model.Achievement;
import model.User;

public class RichPlayerAchievement extends Achievement {
    public RichPlayerAchievement(String id, String name, String description, int rewardMoney, int requiredValue) {
        super(id, name, description, rewardMoney, AchievementType.RICH_PLAYER, requiredValue);
    }

    @Override
    public boolean checkRequirement(User user) {
        return user.getBalance() >= getRequiredValue();
    }

    @Override
    public int getProgress(User user) {
        int current = user.getBalance();
        return Math.min(100, (current * 100) / getRequiredValue());
    }
}
