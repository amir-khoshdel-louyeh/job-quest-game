package controller;

import controller.contract.GameReads;
import controller.contract.SessionStats;

import java.util.List;

import model.career.Job;
import model.career.LearnableSkill;
import model.goals.Achievement;
import model.goals.Challenge;
import model.player.CareerProgress;
import model.player.PlayerProgress;
import model.player.PlayerView;
import model.store.Service;
import provider.contract.JobCatalog;
import provider.contract.ServiceCatalog;
import provider.contract.SkillCatalog;
import services.AchievementRules;
import services.ChallengeRules;
import utils.ReputationText;
import utils.Require;

// Read-only screen queries: catalogs, progress text, and session stats.
// Actions live in GameController; views receive this object only for reads.
public final class GameQueries implements GameReads {
    // The goal port: only its read half is used from here.
    private final PlayerProgress goalPlayer;
    private final ServiceCatalog serviceCatalog;
    private final JobCatalog jobCatalog;
    private final SkillCatalog skillCatalog;
    private final PlayerView playerView;
    private final ChallengeRules challengeRules;
    private final AchievementRules achievementRules;
    private final SessionStats session;

    // ----- Construction -----
    // Wired by GameSession; every dependency is an abstraction.
    GameQueries(PlayerProgress goalPlayer,
                ServiceCatalog serviceCatalog,
                JobCatalog jobCatalog,
                SkillCatalog skillCatalog,
                PlayerView playerView,
                ChallengeRules challengeRules,
                AchievementRules achievementRules,
                SessionStats session) {
        this.goalPlayer = Require.notNull(goalPlayer, "goalPlayer");
        this.serviceCatalog = Require.notNull(serviceCatalog, "serviceCatalog");
        this.jobCatalog = Require.notNull(jobCatalog, "jobCatalog");
        this.skillCatalog = Require.notNull(skillCatalog, "skillCatalog");
        this.playerView = Require.notNull(playerView, "playerView");
        this.challengeRules = Require.notNull(challengeRules, "challengeRules");
        this.achievementRules = Require.notNull(achievementRules, "achievementRules");
        this.session = Require.notNull(session, "session");
    }

    // ----- Catalog queries -----
    @Override
    public List<Service> getHealthServices() {
        return serviceCatalog.getHealthServices();
    }

    @Override
    public List<Service> getEnergyServices() {
        return serviceCatalog.getEnergyServices();
    }

    @Override
    public List<Job> getJobsForCurrentIdentity() {
        return jobCatalog.getJobsForIdentity(playerView.getIdentity());
    }

    @Override
    public List<LearnableSkill> getSkillsForCurrentIdentity() {
        return skillCatalog.getSkillsForIdentity(playerView.getIdentity());
    }

    // ----- Progress queries -----
    @Override
    public PlayerView getPlayerView() {
        return playerView;
    }

    // Builds the text block shown in the Progress > Stats tab.
    // Read-only: works with a PlayerView, never the mutable aggregate.
    @Override
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

    @Override
    public List<Challenge> getActiveChallenges() {
        return challengeRules.getActiveIncompleteChallenges(goalPlayer);
    }

    @Override
    public List<Challenge> getCompletedChallenges() {
        return challengeRules.getCompletedChallenges(goalPlayer);
    }

    @Override
    public List<Achievement> getAchievements() {
        return achievementRules.getAllAchievements();
    }

    // ----- Session queries -----
    @Override
    public String getSessionStats() {
        return session.summary();
    }
}
