package provider;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import model.Achievement;

/**
 * Provides all available achievements in the game.
 */
public class AchievementProvider {
    
    private static final List<Achievement> ALL_ACHIEVEMENTS = new ArrayList<>();
    
    static {
        // Money achievements
        ALL_ACHIEVEMENTS.add(new Achievement(
            "first_dollar", 
            "First Dollar", 
            "Earn your first $100", 
            50, 
            Achievement.AchievementType.MONEY_EARNED, 
            100
        ));
        
        ALL_ACHIEVEMENTS.add(new Achievement(
            "money_maker", 
            "Money Maker", 
            "Earn $5,000 in total", 
            500, 
            Achievement.AchievementType.MONEY_EARNED, 
            5000
        ));
        
        ALL_ACHIEVEMENTS.add(new Achievement(
            "wealthy", 
            "Wealthy", 
            "Earn $50,000 in total", 
            5000, 
            Achievement.AchievementType.MONEY_EARNED, 
            50000
        ));
        
        ALL_ACHIEVEMENTS.add(new Achievement(
            "millionaire", 
            "Millionaire", 
            "Have $100,000 at once", 
            20000, 
            Achievement.AchievementType.RICH_PLAYER, 
            100000
        ));
        
        // Job achievements
        ALL_ACHIEVEMENTS.add(new Achievement(
            "first_job", 
            "First Job", 
            "Complete your first job", 
            100, 
            Achievement.AchievementType.JOBS_COMPLETED, 
            1
        ));
        
        ALL_ACHIEVEMENTS.add(new Achievement(
            "hard_worker", 
            "Hard Worker", 
            "Complete 50 jobs", 
            1000, 
            Achievement.AchievementType.JOBS_COMPLETED, 
            50
        ));
        
        ALL_ACHIEVEMENTS.add(new Achievement(
            "workaholic", 
            "Workaholic", 
            "Complete 10 jobs in one session", 
            2000, 
            Achievement.AchievementType.WORKAHOLIC, 
            10
        ));
        
        // Skill achievements
        ALL_ACHIEVEMENTS.add(new Achievement(
            "student", 
            "Student", 
            "Learn your first skill", 
            200, 
            Achievement.AchievementType.SKILLS_LEARNED, 
            1
        ));
        
        ALL_ACHIEVEMENTS.add(new Achievement(
            "skill_collector", 
            "Skill Collector", 
            "Learn 5 different skills", 
            1000, 
            Achievement.AchievementType.SKILLS_LEARNED, 
            5
        ));
        
        ALL_ACHIEVEMENTS.add(new Achievement(
            "master", 
            "Master", 
            "Learn all available skills", 
            5000, 
            Achievement.AchievementType.SKILL_MASTER, 
            1
        ));
        
        // Health achievements
        ALL_ACHIEVEMENTS.add(new Achievement(
            "healthy_lifestyle", 
            "Healthy Lifestyle", 
            "Maintain 100 health for 7 days", 
            1500, 
            Achievement.AchievementType.HEALTH_MAINTAINED, 
            7
        ));
        
        // Play time achievements
        ALL_ACHIEVEMENTS.add(new Achievement(
            "dedicated", 
            "Dedicated Player", 
            "Play for 30 days", 
            3000, 
            Achievement.AchievementType.DAYS_PLAYED, 
            30
        ));
    }
    
    public static List<Achievement> getAllAchievements() {
        return Collections.unmodifiableList(ALL_ACHIEVEMENTS);
    }
    
    public static Achievement getAchievementById(String id) {
        return ALL_ACHIEVEMENTS.stream()
            .filter(a -> a.getId().equals(id))
            .findFirst()
            .orElse(null);
    }
}
