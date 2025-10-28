package model.achievement;

import model.Achievement;
import model.User;

public class SkillMasterAchievement extends Achievement {
    public SkillMasterAchievement(String id, String name, String description, int rewardMoney, int requiredValue) {
        super(id, name, description, rewardMoney, AchievementType.SKILL_MASTER, requiredValue);
    }

    
    public boolean checkRequirement(User user) {
        return user.getSkills().size() >= 6; // We have 6 skills total
    }

    
    public int getProgress(User user) {
        int current = user.getSkills().size();
        return Math.min(100, (current * 100) / 6);
    }
}
