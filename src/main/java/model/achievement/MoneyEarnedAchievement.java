package model.achievement;

import model.Achievement;
import model.User;

public class MoneyEarnedAchievement extends Achievement {
    public MoneyEarnedAchievement(String id, String name, String description, int rewardMoney, int requiredValue) {
        super(id, name, description, rewardMoney, AchievementType.MONEY_EARNED, requiredValue);
    }

    
    public boolean checkRequirement(User user) {
        return user.getTotalMoneyEarned() >= getRequiredValue();
    }

    
    public int getProgress(User user) {
        int current = user.getTotalMoneyEarned();
        return Math.min(100, (current * 100) / getRequiredValue());
    }
}
