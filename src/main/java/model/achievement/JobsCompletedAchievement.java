package model.achievement;

/**
 * Achievement rewarded for completing a target number of jobs.
 *
 * Follows Single Responsibility Principle by encapsulating achievement metadata only.
 */

import model.Achievement;
import model.User;

public class JobsCompletedAchievement extends Achievement {
    public JobsCompletedAchievement(String id, String name, String description, int rewardMoney, int requiredValue) {
        // construct jobs-completed achievement
        super(id, name, description, rewardMoney, AchievementType.JOBS_COMPLETED, requiredValue);
    }

    
    // check if user completed enough jobs
    public boolean checkRequirement(User user) {
        return user.getTotalJobsCompleted() >= getRequiredValue();
    }

    
    // return progress percent based on jobs completed
    public int getProgress(User user) {
        int current = user.getTotalJobsCompleted();
        return Math.min(100, (current * 100) / getRequiredValue());
    }
}
