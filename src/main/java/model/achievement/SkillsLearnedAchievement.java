package model.achievement;

/**
 * Achievement for learning a specified number of skills.
 *
 * Follows Single Responsibility Principle by describing the achievement data and conditions only.
 */

import model.Achievement;
import model.User;

public class SkillsLearnedAchievement extends Achievement {
    public SkillsLearnedAchievement(String id, String name, String description, int rewardMoney, int requiredValue) {
        // construct skills-learned achievement
        super(id, name, description, rewardMoney, AchievementType.SKILLS_LEARNED, requiredValue);
    }

    
    // check if user has learned required number of skills
    public boolean checkRequirement(User user) {
        return user.getSkills().size() >= getRequiredValue();
    }

    
    // return progress percent toward skills learned requirement
    public int getProgress(User user) {
        int current = user.getSkills().size();
        return Math.min(100, (current * 100) / getRequiredValue());
    }
}
