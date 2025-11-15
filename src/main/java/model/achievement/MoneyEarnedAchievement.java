package model.achievement;

import model.Achievement;
import model.User;

// Rewards earning a target amount of money overall.
public class MoneyEarnedAchievement extends Achievement {
    public MoneyEarnedAchievement(String id, String name, String description, int rewardMoney, int requiredValue) {
        super(id, name, description, rewardMoney, AchievementType.MONEY_EARNED, requiredValue);
    }

    @Override
    protected int getCurrentValue(User user) {
        return user.getTotalMoneyEarned();
    }
}
