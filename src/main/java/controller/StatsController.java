package controller;

import model.player.CareerProgress;
import model.player.User;

// Builds the text block shown for player stats.
public class StatsController {

    public String getDetailedStats(User user) {
        String stats = "";
        stats = stats + "===================================\n";
        stats = stats + " Player: " + user.getUsername() + " - Level " + user.getLevel() + "\n";
        stats = stats + "===================================\n";
        stats = stats + " XP: " + user.getExperience() + "/" + user.getRequiredExperienceForNextLevel();
        stats = stats + " (" + user.getExperiencePercentage() + "%)\n";
        stats = stats + " Reputation: " + user.getReputation() + "/" + CareerProgress.MAX_REPUTATION + " (" + utils.ReputationText.titleFor(user.getReputation()) + ")\n";
        stats = stats + " Job Bonus: " + user.getJobBonus() + "\n";
        stats = stats + " Played: " + (user.getTotalPlayTime() / (1000 * 60)) + " min\n";
        stats = stats + " Total Earned: $" + user.getTotalMoneyEarned() + "\n";
        stats = stats + " Jobs Completed: " + user.getTotalJobsCompleted() + "\n";
        stats = stats + " Achievements: " + user.getUnlockedAchievements().size() + " unlocked\n";
        stats = stats + "===================================\n";
        return stats;
    }
}
