package model.achievement;

import model.Achievement;
import model.User;

public class DaysPlayedAchievement extends Achievement {
    public DaysPlayedAchievement(String id, String name, String description, int rewardMoney, int requiredValue) {
        super(id, name, description, rewardMoney, AchievementType.DAYS_PLAYED, requiredValue);
    }

    @Override
    public boolean checkRequirement(User user) {
        // Requires time tracking, not implemented
        return false;
    }

    @Override
    public int getProgress(User user) {
        // Not tracked here
        return 0;
    }
}
