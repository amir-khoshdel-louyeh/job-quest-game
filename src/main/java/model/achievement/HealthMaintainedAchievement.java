package model.achievement;

/**
 * Achievement for keeping the player's health within healthy bounds.
 *
 * Follows Single Responsibility Principle by modelling the achievement's data and checks.
 */

import model.Achievement;
import model.User;

public class HealthMaintainedAchievement extends Achievement {
    public HealthMaintainedAchievement(String id, String name, String description, int rewardMoney, int requiredValue) {
        // construct health-maintained achievement
        super(id, name, description, rewardMoney, AchievementType.HEALTH_MAINTAINED, requiredValue);
    }

    
    // check if user has maintained health requirement
    public boolean checkRequirement(User user) {
        // Requires time tracking, not implemented
        return false;
    }

    
    // return progress percent for health-maintained
    public int getProgress(User user) {
        // Not tracked here
        return 0;
    }
}
