package services;

import model.*;
import java.util.ArrayList;
import java.util.List;

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
    
    /**
     * Check all achievements and unlock any that meet requirements.
     * @return List of newly unlocked achievements
     */
    public List<Achievement> checkAndUnlockAchievements(User user) {
        List<Achievement> newlyUnlocked = new ArrayList<>();
        
        for (Achievement achievement : AchievementProvider.getAllAchievements()) {
            if (!user.hasAchievement(achievement.getId()) && 
                checkAchievementRequirement(user, achievement)) {
                
                user.unlockAchievement(achievement.getId());
                achievement.unlock();
                
                // Grant reward
                user.setBalance(user.getBalance() + achievement.getRewardMoney());
                
                newlyUnlocked.add(achievement);
            }
        }
        
        return newlyUnlocked;
    }
    
    /**
     * Check if a specific achievement's requirements are met.
     */
    private boolean checkAchievementRequirement(User user, Achievement achievement) {
        return switch (achievement.getType()) {
            case MONEY_EARNED -> 
                user.getTotalMoneyEarned() >= achievement.getRequiredValue();
                
            case JOBS_COMPLETED -> 
                user.getTotalJobsCompleted() >= achievement.getRequiredValue();
                
            case SKILLS_LEARNED -> 
                user.getSkills().size() >= achievement.getRequiredValue();
                
            case RICH_PLAYER -> 
                user.getBalance() >= achievement.getRequiredValue();
                
            case SKILL_MASTER -> 
                user.getSkills().size() >= 6; // We have 6 skills total
                
            case WORKAHOLIC -> 
                false; // Checked during session
                
            case HEALTH_MAINTAINED -> 
                false; // Requires time tracking
                
            case DAYS_PLAYED -> 
                false; // Requires time tracking
                
            case ENERGY_EFFICIENT -> 
                false; // Requires tracking efficiency
        };
    }
    
    /**
     * Get achievement progress for display.
     */
    public int getAchievementProgress(User user, Achievement achievement) {
        int current = switch (achievement.getType()) {
            case MONEY_EARNED -> user.getTotalMoneyEarned();
            case JOBS_COMPLETED -> user.getTotalJobsCompleted();
            case SKILLS_LEARNED -> user.getSkills().size();
            case RICH_PLAYER -> user.getBalance();
            case SKILL_MASTER -> user.getSkills().size();
            default -> 0;
        };
        
        return Math.min(100, (current * 100) / achievement.getRequiredValue());
    }
    
    /**
     * Get all achievements with user's unlock status.
     */
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
