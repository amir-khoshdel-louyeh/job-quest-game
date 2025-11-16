package model.goals;

// Small interface so model.goals never imports model.player.User.
// User implements this; Achievement reads only what it needs (DIP).
public interface AchievementStats {
    int getTotalJobsCompleted();
    int getTotalMoneyEarned();
    int getBalance();
    int getSkillCount();
    boolean hasAchievement(String id);
}
