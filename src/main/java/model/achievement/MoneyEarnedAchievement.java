package model.achievement;

/**
 * Achievement awarded for earning a target amount of in-game money.
 *
 * Follows Single Responsibility Principle by representing the achievement metadata only.
 */

import model.Achievement;
import model.User;

public class MoneyEarnedAchievement extends Achievement {
    public MoneyEarnedAchievement(String id, String name, String description, int rewardMoney, int requiredValue) {
        // construct money-earned achievement
        super(id, name, description, rewardMoney, AchievementType.MONEY_EARNED, requiredValue);
    }

    
    // check if user earned required total money
    public boolean checkRequirement(User user) {
        return user.getTotalMoneyEarned() >= getRequiredValue();
    }

    
    // return progress percent based on total money earned
    public int getProgress(User user) {
        int current = user.getTotalMoneyEarned();
        return Math.min(100, (current * 100) / getRequiredValue());
    }
}
