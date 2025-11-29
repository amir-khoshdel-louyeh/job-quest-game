package controller;

import java.util.List;

import model.career.Job;
import model.career.LearnableSkill;
import model.goals.Achievement;
import model.goals.Challenge;
import model.player.CareerProgress;
import model.player.PlayerProgress;
import model.player.PlayerView;
import model.store.Service;
import provider.JobCatalog;
import provider.SkillCatalog;
import services.AchievementService;
import services.ChallengeService;
import utils.ReputationText;

// Read-only screen queries: catalogs, progress text, and session stats.
// Actions live in GameController; views receive this object only for reads.
public final class GameQueries {
    // The goal port: only its read half is used from here.
    private final PlayerProgress goalPlayer;
    private final EconomyController economyController;
    private final JobCatalog jobCatalog;
    private final SkillCatalog skillCatalog;
    private final PlayerView playerView;
    private final ChallengeService challengeService;
    private final AchievementService achievementService;
    private final SessionTracker session;

    // ----- Construction -----
    // Wired by GameSession; every dependency is required.
    GameQueries(PlayerProgress goalPlayer,
                EconomyController economyController,
                JobCatalog jobCatalog,
                SkillCatalog skillCatalog,
                PlayerView playerView,
                ChallengeService challengeService,
                AchievementService achievementService,
                SessionTracker session) {
        this.goalPlayer = utils.Require.notNull(goalPlayer, "goalPlayer");
        this.economyController = utils.Require.notNull(economyController, "economyController");
        this.jobCatalog = utils.Require.notNull(jobCatalog, "jobCatalog");
        this.skillCatalog = utils.Require.notNull(skillCatalog, "skillCatalog");
        this.playerView = utils.Require.notNull(playerView, "playerView");
        this.challengeService = utils.Require.notNull(challengeService, "challengeService");
        this.achievementService = utils.Require.notNull(achievementService, "achievementService");
        this.session = utils.Require.notNull(session, "session");
    }

    // ----- Catalog queries -----
    public List<Service> getHealthServices() {
        return economyController.getHealthServices();
    }

    public List<Service> getEnergyServices() {
        return economyController.getEnergyServices();
    }

    public List<Job> getJobsForCurrentIdentity() {
        return jobCatalog.getJobsForIdentity(playerView.getIdentity());
    }

    public List<LearnableSkill> getSkillsForCurrentIdentity() {
        return skillCatalog.getSkillsForIdentity(playerView.getIdentity());
    }

    // ----- Progress queries -----
    public PlayerView getPlayerView() {
        return playerView;
    }

    // Builds the text block shown in the Progress > Stats tab.
    // Read-only: works with a PlayerView, never the mutable aggregate.
    public String getDetailedStats() {
        StringBuilder stats = new StringBuilder();
        stats.append("===================================\n");
        stats.append(" Player: ").append(playerView.getUsername())
                .append(" - Level ").append(playerView.getLevel()).append('\n');
        stats.append("===================================\n");
        stats.append(" XP: ").append(playerView.getExperience()).append('/')
                .append(playerView.getRequiredExperienceForNextLevel())
                .append(" (").append(playerView.getExperiencePercentage()).append("%)\n");
        stats.append(" Reputation: ").append(playerView.getReputation()).append('/')
                .append(CareerProgress.MAX_REPUTATION).append(" (")
                .append(ReputationText.titleFor(playerView.getReputation())).append(")\n");
        stats.append(" Job Bonus: ").append(playerView.getJobBonus()).append('\n');
        stats.append(" Played: ").append(playerView.getPlayTimeMinutes()).append(" min\n");
        stats.append(" Total Earned: $").append(playerView.getTotalMoneyEarned()).append('\n');
        stats.append(" Jobs Completed: ").append(playerView.getTotalJobsCompleted()).append('\n');
        stats.append(" Achievements: ").append(playerView.getAchievementCount())
                .append(" unlocked\n");
        stats.append("===================================\n");
        return stats.toString();
    }

    public List<Challenge> getActiveChallenges() {
        return challengeService.getActiveIncompleteChallenges(goalPlayer);
    }

    public List<Challenge> getCompletedChallenges() {
        return challengeService.getCompletedChallenges(goalPlayer);
    }

    public List<Achievement> getAchievements() {
        return achievementService.getAllAchievements();
    }

    // ----- Session queries -----
    public String getSessionStats() {
        return session.summary();
    }
}
