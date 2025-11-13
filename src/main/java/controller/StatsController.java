package controller;

import model.User;

// This class builds text with player stats.
public class StatsController {

    // Build a simple stats string, uses + concatenation (no String.format).
    public String getDetailedStats(User user) {
        String stats = "";
        stats = stats + "===================================\n";
        stats = stats + " Player: " + user.getUsername() + " - Level " + user.getLevel() + "\n";
        stats = stats + "===================================\n";
        stats = stats + " XP: " + user.getExperience() + "/" + user.getRequiredExperienceForNextLevel();
        stats = stats + " (" + user.getExperiencePercentage() + "%)\n";
        stats = stats + " Reputation: " + user.getReputation() + "/100 (" + user.getReputationTitle() + ")\n";
        stats = stats + " Streak: " + user.getCurrentStreak() + " days (Best: " + user.getMaxStreak() + ")\n";
        stats = stats + " Total Earned: $" + user.getTotalMoneyEarned() + "\n";
        stats = stats + " Jobs Completed: " + user.getTotalJobsCompleted() + "\n";
        stats = stats + " Achievements: " + user.getUnlockedAchievements().size() + " unlocked\n";
        stats = stats + "===================================\n";
        return stats;
    }
}
