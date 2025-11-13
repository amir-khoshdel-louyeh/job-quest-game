package model.achievement;

import model.Achievement;
import model.User;

// Rewards playing a lot of jobs in one session.
public class WorkaholicAchievement extends Achievement {
    public WorkaholicAchievement(String id, String name, String description, int rewardMoney, int requiredValue) {
        super(id, name, description, rewardMoney, AchievementType.WORKAHOLIC, requiredValue);
    }

    // Checked during the session, so always false here.
    public boolean checkRequirement(User user) {
        return false;
    }

    public int getProgress(User user) {
        return 0;
    }
}
