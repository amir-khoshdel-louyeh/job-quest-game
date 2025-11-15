package model.achievement;

import model.Achievement;
import model.User;

// Rewards finishing a target number of jobs.
public class JobsCompletedAchievement extends Achievement {
    public JobsCompletedAchievement(String id, String name, String description, int rewardMoney, int requiredValue) {
        super(id, name, description, rewardMoney, AchievementType.JOBS_COMPLETED, requiredValue);
    }

    @Override
    protected int getCurrentValue(User user) {
        return user.getTotalJobsCompleted();
    }
}
