package controller;

import java.util.List;

import model.career.Job;
import model.career.LearnableSkill;
import model.goals.Achievement;
import model.goals.Challenge;
import model.player.GamePlayer;
import model.player.PlayerView;
import model.store.Service;
import provider.JobCatalog;
import provider.SkillCatalog;
import services.AchievementService;
import services.ChallengeService;

// Read-only screen queries: catalogs, progress, and session stats.
// Actions live in GameController; views receive this object only for reads.
public final class GameQueries {
    private final GamePlayer player;
    private final EconomyController economyController;
    private final JobCatalog jobCatalog;
    private final SkillCatalog skillCatalog;
    private final PlayerView playerView;
    private final StatsController statsController;
    private final ChallengeService challengeService;
    private final AchievementService achievementService;
    private final SessionTracker session;

    // ----- Construction -----
    // Wired by GameSession; every dependency is required.
    GameQueries(GamePlayer player,
                EconomyController economyController,
                JobCatalog jobCatalog,
                SkillCatalog skillCatalog,
                PlayerView playerView,
                StatsController statsController,
                ChallengeService challengeService,
                AchievementService achievementService,
                SessionTracker session) {
        this.player = utils.Require.notNull(player, "player");
        this.economyController = utils.Require.notNull(economyController, "economyController");
        this.jobCatalog = utils.Require.notNull(jobCatalog, "jobCatalog");
        this.skillCatalog = utils.Require.notNull(skillCatalog, "skillCatalog");
        this.playerView = utils.Require.notNull(playerView, "playerView");
        this.statsController = utils.Require.notNull(statsController, "statsController");
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
        return jobCatalog.getJobsForIdentity(player.getIdentity());
    }

    public List<LearnableSkill> getSkillsForCurrentIdentity() {
        return skillCatalog.getSkillsForIdentity(player.getIdentity());
    }

    // ----- Progress queries -----
    public PlayerView getPlayerView() {
        return playerView;
    }

    public String getDetailedStats() {
        return statsController.getDetailedStats(playerView);
    }

    public List<Challenge> getActiveChallenges() {
        return challengeService.getActiveIncompleteChallenges(player);
    }

    public List<Challenge> getCompletedChallenges() {
        return challengeService.getCompletedChallenges(player);
    }

    public List<Achievement> getAchievements() {
        return achievementService.getAllAchievements();
    }

    // ----- Session queries -----
    public String getSessionStats() {
        return session.summary();
    }
}
