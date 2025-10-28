package model.achievement;

import model.Achievement;
import model.User;

public class WorkaholicAchievement extends Achievement {
    public WorkaholicAchievement(String id, String name, String description, int rewardMoney, int requiredValue) {
        super(id, name, description, rewardMoney, AchievementType.WORKAHOLIC, requiredValue);
    }

    @Override
    public boolean checkRequirement(User user) {
        // This should be checked during session, so always return false here
        return false;
    }

    @Override
    public int getProgress(User user) {
        // Not tracked here
        return 0;
    }
}
