package model.goals;

// Rewards learning a target number of skills.
public class SkillsLearnedAchievement extends Achievement {
    public SkillsLearnedAchievement(String id, String name, String description, int rewardMoney, int requiredValue) {
        super(id, name, description, rewardMoney, requiredValue);
    }

    @Override
    protected int getCurrentValue(AchievementStats stats) {
        return stats.getSkillCount();
    }
}
