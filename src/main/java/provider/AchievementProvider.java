
package provider;

import java.util.ArrayList;
import java.util.List;

import model.Achievement;
import model.achievement.MoneyEarnedAchievement;
import model.achievement.JobsCompletedAchievement;
import model.achievement.SkillsLearnedAchievement;
import model.achievement.RichPlayerAchievement;

// Holds all achievements the player can earn.
public class AchievementProvider {

    private AchievementProvider() {
    }

    private static final List<Achievement> ALL_ACHIEVEMENTS = new ArrayList<Achievement>();

    static {
        ALL_ACHIEVEMENTS.add(new MoneyEarnedAchievement("first_dollar", "First Dollar", "Earn your first $100", 50, 100));
        ALL_ACHIEVEMENTS.add(new MoneyEarnedAchievement("pocket_money", "Pocket Money", "Earn $1,000 in total", 200, 1000));
        ALL_ACHIEVEMENTS.add(new MoneyEarnedAchievement("money_maker", "Money Maker", "Earn $5,000 in total", 500, 5000));
        ALL_ACHIEVEMENTS.add(new MoneyEarnedAchievement("earner", "Big Earner", "Earn $15,000 in total", 1500, 15000));
        ALL_ACHIEVEMENTS.add(new MoneyEarnedAchievement("wealthy", "Wealthy", "Earn $50,000 in total", 5000, 50000));
        ALL_ACHIEVEMENTS.add(new MoneyEarnedAchievement("tycoon", "Tycoon", "Earn $150,000 in total", 15000, 150000));
        
        ALL_ACHIEVEMENTS.add(new RichPlayerAchievement("saver", "Saver", "Have $10,000 at once", 1000, 10000));
        ALL_ACHIEVEMENTS.add(new RichPlayerAchievement("rich", "Rich", "Have $50,000 at once", 8000, 50000));
        ALL_ACHIEVEMENTS.add(new RichPlayerAchievement("millionaire", "Millionaire", "Have $100,000 at once", 20000, 100000));
        
        ALL_ACHIEVEMENTS.add(new JobsCompletedAchievement("first_job", "First Job", "Complete your first job", 100, 1));
        ALL_ACHIEVEMENTS.add(new JobsCompletedAchievement("getting_started", "Getting Started", "Complete 10 jobs", 300, 10));
        ALL_ACHIEVEMENTS.add(new JobsCompletedAchievement("steady_worker", "Steady Worker", "Complete 25 jobs", 600, 25));
        ALL_ACHIEVEMENTS.add(new JobsCompletedAchievement("hard_worker", "Hard Worker", "Complete 50 jobs", 1000, 50));
        ALL_ACHIEVEMENTS.add(new JobsCompletedAchievement("professional", "Professional", "Complete 100 jobs", 2500, 100));
        ALL_ACHIEVEMENTS.add(new JobsCompletedAchievement("legend", "Legend", "Complete 200 jobs", 6000, 200));
        
        ALL_ACHIEVEMENTS.add(new SkillsLearnedAchievement("student", "Student", "Learn your first skill", 200, 1));
        ALL_ACHIEVEMENTS.add(new SkillsLearnedAchievement("apprentice", "Apprentice", "Learn 2 different skills", 400, 2));
        ALL_ACHIEVEMENTS.add(new SkillsLearnedAchievement("adept", "Adept", "Learn 3 different skills", 700, 3));
        ALL_ACHIEVEMENTS.add(new SkillsLearnedAchievement("skilled", "Skilled", "Learn 4 different skills", 1200, 4));
        ALL_ACHIEVEMENTS.add(new SkillsLearnedAchievement("skill_collector", "Skill Collector", "Learn 5 different skills", 2500, 5));
        ALL_ACHIEVEMENTS.add(new SkillsLearnedAchievement("master", "Master", "Learn all available skills", 5000, 6));
    }

    public static List<Achievement> getAllAchievements() {
        return new ArrayList<Achievement>(ALL_ACHIEVEMENTS);
    }
}
