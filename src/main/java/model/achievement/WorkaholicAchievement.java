package model.achievement;

/**
 * Achievement for players who complete many jobs or play for long sessions.
 *
 * Follows Single Responsibility Principle by representing this achievement's data only.
 */

import model.Achievement;
import model.User;

public class WorkaholicAchievement extends Achievement {
    public WorkaholicAchievement(String id, String name, String description, int rewardMoney, int requiredValue) {
        // construct workaholic achievement
        super(id, name, description, rewardMoney, AchievementType.WORKAHOLIC, requiredValue);
    }

    
    // check if user met workaholic requirement (session-tracked)
    public boolean checkRequirement(User user) {
        // This should be checked during session, so always return false here
        return false;
    }

    
    // return progress percent for workaholic achievement
    public int getProgress(User user) {
        // Not tracked here
        return 0;
    }
}
