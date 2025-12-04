package model.goals;

// Stats needed to check achievements.
// Implemented by model.player.Player.
public interface AchievementStats {
    int getTotalJobsCompleted();
    int getTotalMoneyEarned();
    int getBalance();
    int getSkillCount();
    boolean hasAchievement(String id);
}
