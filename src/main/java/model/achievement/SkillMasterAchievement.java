package model.achievement;

/**
 * Achievement granted when a player masters a set of skills.
 *
 * Follows Single Responsibility Principle by representing achievement data and conditions only.
 */

import model.Achievement;
import model.User;

public class SkillMasterAchievement extends Achievement {
    public SkillMasterAchievement(String id, String name, String description, int rewardMoney, int requiredValue) {
        // construct skill-master achievement
        super(id, name, description, rewardMoney, AchievementType.SKILL_MASTER, requiredValue);
    }

    
    // check if user learned all skills
    public boolean checkRequirement(User user) {
        return user.getSkills().size() >= 6; // We have 6 skills total
    }

    
    // return progress percent toward learning all skills
    public int getProgress(User user) {
        int current = user.getSkills().size();
        return Math.min(100, (current * 100) / 6);
    }
}
