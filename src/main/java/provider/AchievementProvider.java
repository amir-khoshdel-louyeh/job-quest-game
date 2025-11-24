
package provider;

import java.util.ArrayList;
import java.util.List;

import model.goals.Achievement;
import model.goals.AchievementStats;

// Holds all achievements the player can earn.
// Single-list catalog: extends AbstractCatalog<Achievement> (defensive-copy
// mechanics inherited). Achievement templates come from outside, so
// AchievementService can inject/mock it (DIP) — same pattern as the
// other providers. One concrete Achievement + Strategy lambdas replaces
// the four one-line subclasses (composition over inheritance).
public class AchievementProvider extends AbstractCatalog<Achievement> {

    public AchievementProvider() {
        this(buildDefaultCatalog());
    }

    // Test seam: custom catalog makes every run reproducible.
    public AchievementProvider(List<Achievement> achievements) {
        super(achievements);
    }

    private static List<Achievement> buildDefaultCatalog() {
        List<Achievement> all = new ArrayList<Achievement>();
        // 10-hour run tuning: money thresholds cut ~3-4x to match ~150-200 jobs
        // at ~800$ average (total ~120-160k incl. bonuses/quests). Job counts
        // capped at 100 so Legend is reachable in one run.
        all.add(new Achievement("first_dollar", "First Dollar", "Earn your first $100", 50, 100, AchievementStats::getTotalMoneyEarned));
        all.add(new Achievement("pocket_money", "Pocket Money", "Earn $1,000 in total", 200, 1000, AchievementStats::getTotalMoneyEarned));
        all.add(new Achievement("money_maker", "Money Maker", "Earn $5,000 in total", 500, 5000, AchievementStats::getTotalMoneyEarned));
        all.add(new Achievement("earner", "Big Earner", "Earn $12,000 in total", 1500, 12000, AchievementStats::getTotalMoneyEarned));
        all.add(new Achievement("wealthy", "Wealthy", "Earn $25,000 in total", 5000, 25000, AchievementStats::getTotalMoneyEarned));
        all.add(new Achievement("tycoon", "Tycoon", "Earn $40,000 in total", 15000, 40000, AchievementStats::getTotalMoneyEarned));

        all.add(new Achievement("saver", "Saver", "Have $8,000 at once", 1000, 8000, AchievementStats::getBalance));
        all.add(new Achievement("rich", "Rich", "Have $15,000 at once", 8000, 15000, AchievementStats::getBalance));
        all.add(new Achievement("millionaire", "Millionaire", "Have $25,000 at once", 20000, 25000, AchievementStats::getBalance));

        all.add(new Achievement("first_job", "First Job", "Complete your first job", 100, 1, AchievementStats::getTotalJobsCompleted));
        all.add(new Achievement("getting_started", "Getting Started", "Complete 10 jobs", 300, 10, AchievementStats::getTotalJobsCompleted));
        all.add(new Achievement("steady_worker", "Steady Worker", "Complete 25 jobs", 600, 25, AchievementStats::getTotalJobsCompleted));
        all.add(new Achievement("hard_worker", "Hard Worker", "Complete 35 jobs", 1000, 35, AchievementStats::getTotalJobsCompleted));
        all.add(new Achievement("professional", "Professional", "Complete 60 jobs", 2500, 60, AchievementStats::getTotalJobsCompleted));
        all.add(new Achievement("legend", "Legend", "Complete 100 jobs", 6000, 100, AchievementStats::getTotalJobsCompleted));

        all.add(new Achievement("student", "Student", "Learn your first skill", 200, 1, AchievementStats::getSkillCount));
        all.add(new Achievement("apprentice", "Apprentice", "Learn 2 different skills", 400, 2, AchievementStats::getSkillCount));
        all.add(new Achievement("adept", "Adept", "Learn 3 different skills", 700, 3, AchievementStats::getSkillCount));
        all.add(new Achievement("skilled", "Skilled", "Learn 4 different skills", 1200, 4, AchievementStats::getSkillCount));
        all.add(new Achievement("skill_collector", "Skill Collector", "Learn 5 different skills", 2500, 5, AchievementStats::getSkillCount));
        all.add(new Achievement("master", "Master", "Learn all available skills", 5000, 6, AchievementStats::getSkillCount));
        return all;
    }

    // Kept name for backward compatibility; mechanics inherited from base.
    public List<Achievement> getAllAchievements() {
        return getAll();
    }
}
