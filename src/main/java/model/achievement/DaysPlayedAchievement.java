package model.achievement;

/**
 * Achievement unlocked for playing the game on multiple or consecutive days.
 *
 * Follows Single Responsibility Principle by representing days-played achievement data only.
 */

import model.Achievement;
import model.User;

public class DaysPlayedAchievement extends Achievement {
    public DaysPlayedAchievement(String id, String name, String description, int rewardMoney, int requiredValue) {
        // construct days-played achievement
        super(id, name, description, rewardMoney, AchievementType.DAYS_PLAYED, requiredValue);
    }

    
    // check if user meets days-played requirement
    public boolean checkRequirement(User user) {
        // Requires time tracking, not implemented
        return false;
    }

    
    // return progress percent for days played
    public int getProgress(User user) {
        // Not tracked here
        return 0;
    }
}
