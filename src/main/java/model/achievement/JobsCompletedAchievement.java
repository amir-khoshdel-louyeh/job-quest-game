package model.achievement;

import model.Achievement;
import model.User;

public class JobsCompletedAchievement extends Achievement {
    public JobsCompletedAchievement(String id, String name, String description, int rewardMoney, int requiredValue) {
        super(id, name, description, rewardMoney, AchievementType.JOBS_COMPLETED, requiredValue);
    }

    
    public boolean checkRequirement(User user) {
        return user.getTotalJobsCompleted() >= getRequiredValue();
    }

    
    public int getProgress(User user) {
        int current = user.getTotalJobsCompleted();
        return Math.min(100, (current * 100) / getRequiredValue());
    }
}
