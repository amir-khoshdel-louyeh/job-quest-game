package model.achievement;

import model.Achievement;
import model.User;

// Rewards keeping health up over time.
public class HealthMaintainedAchievement extends Achievement {
    public HealthMaintainedAchievement(String id, String name, String description, int rewardMoney, int requiredValue) {
        super(id, name, description, rewardMoney, AchievementType.HEALTH_MAINTAINED, requiredValue);
    }

    // Needs time tracking, so always false here.
    public boolean checkRequirement(User user) {
        return false;
    }

    public int getProgress(User user) {
        return 0;
    }
}
