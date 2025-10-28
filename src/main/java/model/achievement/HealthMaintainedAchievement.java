package model.achievement;

import model.Achievement;
import model.User;

public class HealthMaintainedAchievement extends Achievement {
    public HealthMaintainedAchievement(String id, String name, String description, int rewardMoney, int requiredValue) {
        super(id, name, description, rewardMoney, AchievementType.HEALTH_MAINTAINED, requiredValue);
    }

    
    public boolean checkRequirement(User user) {
        // Requires time tracking, not implemented
        return false;
    }

    
    public int getProgress(User user) {
        // Not tracked here
        return 0;
    }
}
