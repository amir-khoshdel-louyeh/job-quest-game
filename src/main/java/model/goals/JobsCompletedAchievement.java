package model.goals;

// Rewards finishing a target number of jobs.
public class JobsCompletedAchievement extends Achievement {
    public JobsCompletedAchievement(String id, String name, String description, int rewardMoney, int requiredValue) {
        super(id, name, description, rewardMoney, requiredValue);
    }

    @Override
    protected int getCurrentValue(AchievementStats stats) {
        return stats.getTotalJobsCompleted();
    }
}
