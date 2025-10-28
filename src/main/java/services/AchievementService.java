package services;

import model.Achievement;
import model.User;
import java.util.ArrayList;
import java.util.List;
import provider.AchievementProvider;

/**
 * Manages achievements and checks for unlocking them.
 */
public class AchievementService {
    private static AchievementService instance;
    
    private AchievementService() {}
    
    public static AchievementService getInstance() {
        if (instance == null) {
            instance = new AchievementService();
        }
        return instance;
    }
    
    /** Check and unlock new achievements */
    public List<Achievement> checkAndUnlockAchievements(User user) {
        List<Achievement> newlyUnlocked = new ArrayList<>();
        
    for (Achievement achievement : provider.AchievementProvider.getAllAchievements()) {
            if (!user.hasAchievement(achievement.getId()) && 
                checkAchievementRequirement(user, achievement)) {
                
                user.unlockAchievement(achievement.getId());
                achievement.unlock();
                // Grant reward
                user.deposit(achievement.getRewardMoney());
                newlyUnlocked.add(achievement);
            }
        }
        
        return newlyUnlocked;
    }
    
    /** Check the requirements for unlocking an achievement */
    private boolean checkAchievementRequirement(User user, Achievement achievement) {
        return achievement.checkRequirement(user);
    }
    
    /** Get achievement progress percentage */
    public int getAchievementProgress(User user, Achievement achievement) {
        return achievement.getProgress(user);
    }
    
    /** Get the list of achievements with unlock status */
    public List<Achievement> getAchievementsWithStatus(User user) {
        List<Achievement> achievements = new ArrayList<>();
        
        for (Achievement achievement : AchievementProvider.getAllAchievements()) {
            if (user.hasAchievement(achievement.getId())) {
                achievement.unlock();
            }
            achievements.add(achievement);
        }
        
        return achievements;
    }
}
