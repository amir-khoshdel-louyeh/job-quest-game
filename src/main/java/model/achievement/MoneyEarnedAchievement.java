package model.achievement;

import model.Achievement;
import model.User;

// Rewards earning a target amount of money overall.
public class MoneyEarnedAchievement extends Achievement {
    public MoneyEarnedAchievement(String id, String name, String description, int rewardMoney, int requiredValue) {
        super(id, name, description, rewardMoney, AchievementType.MONEY_EARNED, requiredValue);
    }

    public boolean checkRequirement(User user) {
        return user.getTotalMoneyEarned() >= getRequiredValue();
    }

    // Progress shown as a number from 0 to 100.
    public int getProgress(User user) {
        int current = user.getTotalMoneyEarned();
        return Math.min(100, (current * 100) / getRequiredValue());
    }
}
