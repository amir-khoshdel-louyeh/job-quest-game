package controller;

/**
 * Controller responsible for computing and exposing user statistics and summaries.
 *
 * Follows Single Responsibility Principle by encapsulating only stats-related logic.
 */

import model.User;

public class StatsController {
    public String getDetailedStats(User user) {
        // build and return a formatted detailed stats string for the user
        StringBuilder stats = new StringBuilder();
        stats.append("═══════════════════════════════════\n");
        stats.append(String.format(" 👤 %s - Level %d\n", user.getUsername(), user.getLevel()));
        stats.append("═══════════════════════════════════\n");
        stats.append(String.format("⭐ XP: %d/%d (%d%%)\n", 
            user.getExperience(), 
            user.getRequiredExperienceForNextLevel(),
            user.getExperiencePercentage()));
        stats.append(String.format("🏆 Reputation: %d/100 (%s)\n", 
            user.getReputation(), 
            user.getReputationTitle()));
        stats.append(String.format("🔥 Streak: %d days (Best: %d)\n", 
            user.getCurrentStreak(), 
            user.getMaxStreak()));
        stats.append(String.format("💰 Total Earned: $%,d\n", user.getTotalMoneyEarned()));
        stats.append(String.format("📊 Jobs Completed: %,d\n", user.getTotalJobsCompleted()));
        stats.append(String.format("🎖️ Achievements: %d unlocked\n", 
            user.getUnlockedAchievements().size()));
        stats.append("═══════════════════════════════════\n");
        return stats.toString();
    }
}
