package model.achievement;

/**
 * Achievement for maintaining or efficiently using energy in the game.
 *
 * Follows Single Responsibility Principle by encapsulating achievement metadata only.
 */

import model.Achievement;
import model.User;

public class EnergyEfficientAchievement extends Achievement {
    public EnergyEfficientAchievement(String id, String name, String description, int rewardMoney, int requiredValue) {
        // construct energy-efficient achievement
        super(id, name, description, rewardMoney, AchievementType.ENERGY_EFFICIENT, requiredValue);
    }

    
    // check if user meets energy efficiency requirement
    public boolean checkRequirement(User user) {
        // Requires tracking efficiency, not implemented
        return false;
    }

    
    // return progress percent for energy efficiency
    public int getProgress(User user) {
        // Not tracked here
        return 0;
    }
}
