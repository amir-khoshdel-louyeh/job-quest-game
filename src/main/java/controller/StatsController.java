package controller;

import model.player.CareerProgress;
import model.player.PlayerView;
import utils.DurationText;
import utils.ReputationText;

// Builds the text block shown for player stats.
// Read-only: works with a PlayerView, never the mutable User.
// Package-private: GameController exposes only the formatted text.
final class StatsController {

    // ----- Queries -----
    public String getDetailedStats(PlayerView player) {
        StringBuilder stats = new StringBuilder();
        stats.append("===================================\n");
        stats.append(" Player: ").append(player.getUsername())
                .append(" - Level ").append(player.getLevel()).append('\n');
        stats.append("===================================\n");
        stats.append(" XP: ").append(player.getExperience()).append('/')
                .append(player.getRequiredExperienceForNextLevel())
                .append(" (").append(player.getExperiencePercentage()).append("%)\n");
        stats.append(" Reputation: ").append(player.getReputation()).append('/')
                .append(CareerProgress.MAX_REPUTATION).append(" (")
                .append(ReputationText.titleFor(player.getReputation())).append(")\n");
        stats.append(" Job Bonus: ").append(player.getJobBonus()).append('\n');
        stats.append(" Played: ").append(player.getPlayTimeMinutes()).append(" min\n");
        stats.append(" Total Earned: $").append(player.getTotalMoneyEarned()).append('\n');
        stats.append(" Jobs Completed: ").append(player.getTotalJobsCompleted()).append('\n');
        stats.append(" Achievements: ").append(player.getAchievementCount())
                .append(" unlocked\n");
        stats.append("===================================\n");
        return stats.toString();
    }
}
