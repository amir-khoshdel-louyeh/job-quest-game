package model.achievement;

import model.Achievement;
import model.User;

// Rewards learning a target number of skills.
public class SkillsLearnedAchievement extends Achievement {
    public SkillsLearnedAchievement(String id, String name, String description, int rewardMoney, int requiredValue) {
        super(id, name, description, rewardMoney, AchievementType.SKILLS_LEARNED, requiredValue);
    }

    @Override
    protected int getCurrentValue(User user) {
        return user.getSkills().size();
    }
}
