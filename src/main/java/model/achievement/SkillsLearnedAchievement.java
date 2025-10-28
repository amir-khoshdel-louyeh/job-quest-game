package model.achievement;

import model.Achievement;
import model.User;

public class SkillsLearnedAchievement extends Achievement {
    public SkillsLearnedAchievement(String id, String name, String description, int rewardMoney, int requiredValue) {
        super(id, name, description, rewardMoney, AchievementType.SKILLS_LEARNED, requiredValue);
    }

    @Override
    public boolean checkRequirement(User user) {
        return user.getSkills().size() >= getRequiredValue();
    }

    @Override
    public int getProgress(User user) {
        int current = user.getSkills().size();
        return Math.min(100, (current * 100) / getRequiredValue());
    }
}
