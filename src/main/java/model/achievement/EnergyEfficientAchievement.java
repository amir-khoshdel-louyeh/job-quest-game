package model.achievement;

import model.Achievement;
import model.User;

public class EnergyEfficientAchievement extends Achievement {
    public EnergyEfficientAchievement(String id, String name, String description, int rewardMoney, int requiredValue) {
        super(id, name, description, rewardMoney, AchievementType.ENERGY_EFFICIENT, requiredValue);
    }

    
    public boolean checkRequirement(User user) {
        // Requires tracking efficiency, not implemented
        return false;
    }

    
    public int getProgress(User user) {
        // Not tracked here
        return 0;
    }
}
