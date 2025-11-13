package model.achievement;

import model.Achievement;
import model.User;

// Rewards playing on multiple days.
public class DaysPlayedAchievement extends Achievement {
    public DaysPlayedAchievement(String id, String name, String description, int rewardMoney, int requiredValue) {
        super(id, name, description, rewardMoney, AchievementType.DAYS_PLAYED, requiredValue);
    }

    // Needs time tracking, so always false here.
    public boolean checkRequirement(User user) {
        return false;
    }

    public int getProgress(User user) {
        return 0;
    }
}
