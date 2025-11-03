package model.achievement;

/**
 * Achievement awarded for accumulating a large amount of in-game money.
 *
 * Follows Single Responsibility Principle by modelling achievement metadata and checks.
 */

import model.Achievement;
import model.User;

public class RichPlayerAchievement extends Achievement {
    public RichPlayerAchievement(String id, String name, String description, int rewardMoney, int requiredValue) {
        // construct rich-player achievement
        super(id, name, description, rewardMoney, AchievementType.RICH_PLAYER, requiredValue);
    }

    
    // check if user balance meets requirement
    public boolean checkRequirement(User user) {
        return user.getBalance() >= getRequiredValue();
    }

    
    // return progress percent based on current balance
    public int getProgress(User user) {
        int current = user.getBalance();
        return Math.min(100, (current * 100) / getRequiredValue());
    }
}
