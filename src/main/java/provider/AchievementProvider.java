
package provider;
import model.achievement.HealthMaintainedAchievement;
import model.achievement.DaysPlayedAchievement;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import model.Achievement;
import model.achievement.MoneyEarnedAchievement;
import model.achievement.JobsCompletedAchievement;
import model.achievement.SkillsLearnedAchievement;
import model.achievement.RichPlayerAchievement;
import model.achievement.SkillMasterAchievement;
import model.achievement.WorkaholicAchievement;

/**
 * Provides all available achievements in the game.
 */
public class AchievementProvider {
    
    private static final List<Achievement> ALL_ACHIEVEMENTS = new ArrayList<>();
    
    static {
        // Money achievements
        ALL_ACHIEVEMENTS.add(new MoneyEarnedAchievement(
            "first_dollar", 
            "First Dollar", 
            "Earn your first $100", 
            50, 
            100
        ));
        
        ALL_ACHIEVEMENTS.add(new MoneyEarnedAchievement(
            "money_maker", 
            "Money Maker", 
            "Earn $5,000 in total", 
            500, 
            5000
        ));
        
        ALL_ACHIEVEMENTS.add(new MoneyEarnedAchievement(
            "wealthy", 
            "Wealthy", 
            "Earn $50,000 in total", 
            5000, 
            50000
        ));
        
        ALL_ACHIEVEMENTS.add(new RichPlayerAchievement(
            "millionaire", 
            "Millionaire", 
            "Have $100,000 at once", 
            20000, 
            100000
        ));
        
        // Job achievements
        ALL_ACHIEVEMENTS.add(new JobsCompletedAchievement(
            "first_job", 
            "First Job", 
            "Complete your first job", 
            100, 
            1
        ));
        
        ALL_ACHIEVEMENTS.add(new JobsCompletedAchievement(
            "hard_worker", 
            "Hard Worker", 
            "Complete 50 jobs", 
            1000, 
            50
        ));
        
        ALL_ACHIEVEMENTS.add(new WorkaholicAchievement(
            "workaholic", 
            "Workaholic", 
            "Complete 10 jobs in one session", 
            2000, 
            10
        ));
        
        // Skill achievements
        ALL_ACHIEVEMENTS.add(new SkillsLearnedAchievement(
            "student", 
            "Student", 
            "Learn your first skill", 
            200, 
            1
        ));
        
        ALL_ACHIEVEMENTS.add(new SkillsLearnedAchievement(
            "skill_collector", 
            "Skill Collector", 
            "Learn 5 different skills", 
            1000, 
            5
        ));
        
        ALL_ACHIEVEMENTS.add(new SkillMasterAchievement(
            "master", 
            "Master", 
            "Learn all available skills", 
            5000, 
            1
        ));
        
        // Health achievements
        ALL_ACHIEVEMENTS.add(new HealthMaintainedAchievement(
            "healthy_lifestyle", 
            "Healthy Lifestyle", 
            "Maintain 100 health for 7 days", 
            1500, 
            7
        ));

        // Play time achievements
        ALL_ACHIEVEMENTS.add(new DaysPlayedAchievement(
            "dedicated", 
            "Dedicated Player", 
            "Play for 30 days", 
            3000, 
            30
        ));
    }
    
    public static List<Achievement> getAllAchievements() {
        return Collections.unmodifiableList(ALL_ACHIEVEMENTS);
    }
    
    public static Achievement getAchievementById(String id) {
        // find an achievement by its id
        return ALL_ACHIEVEMENTS.stream()
            .filter(a -> a.getId().equals(id))
            .findFirst()
            .orElse(null);
    }
}
