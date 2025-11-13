package model.achievement;

import model.Achievement;
import model.User;

// Rewards finishing a target number of jobs.
public class JobsCompletedAchievement extends Achievement {
    public JobsCompletedAchievement(String id, String name, String description, int rewardMoney, int requiredValue) {
        super(id, name, description, rewardMoney, AchievementType.JOBS_COMPLETED, requiredValue);
    }

    public boolean checkRequirement(User user) {
        return user.getTotalJobsCompleted() >= getRequiredValue();
    }

    // Progress shown as a number from 0 to 100.
    public int getProgress(User user) {
        int current = user.getTotalJobsCompleted();
        return Math.min(100, (current * 100) / getRequiredValue());
    }
}
