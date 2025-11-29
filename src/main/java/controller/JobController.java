package controller;

import model.career.Job;
import model.career.JobRewards;
import model.minigame.MiniGameResult;
import model.player.JobPlayer;
import model.goals.Achievement;
import model.goals.Challenge;
import services.AchievementService;
import services.ChallengeService;
import services.JobService;
import services.job.JobGate;
import java.util.ArrayList;
import java.util.List;

// Runs one job: checks energy, rolls payment, hands out XP.
// Package-private: only GameSession/GameController wire it.
final class JobController {
    private final JobPlayer player;
    private final JobService jobService;
    private final JobGate jobGate;
    private final ChallengeService challengeService;
    private final AchievementService achievementService;

    // ----- Construction -----
    // Services come from GameSession so job, skill, and challenge rules share them.
    JobController(JobPlayer player, JobService jobService, JobGate jobGate,
                  ChallengeService challengeService, AchievementService achievementService) {
        this.player = utils.Require.notNull(player, "player");
        this.jobService = utils.Require.notNull(jobService, "jobService");
        this.jobGate = utils.Require.notNull(jobGate, "jobGate");
        this.challengeService = utils.Require.notNull(challengeService, "challengeService");
        this.achievementService = utils.Require.notNull(achievementService, "achievementService");
    }

    // ----- Commands -----
    // Runs a job without a mini-game.
    public Outcome<JobRewards> doJob(Job job) {
        return execute(job, null);
    }

    // Runs a job with a mini-game result.
    public Outcome<JobRewards> doJob(Job job, MiniGameResult game) {
        return execute(job, game);
    }

    private Outcome<JobRewards> execute(Job job, MiniGameResult game) {
        JobGate.Decision gate = jobGate.check(job,
                player.getIdentity(),
                player.getSkillSet(),
                player.getEnergy(),
                player.isBlocked());
        if (!gate.isAllowed()) {
            return Outcome.failure(utils.JobResultText.failureLine(job, gate.getReason()));
        }

        // Failed mini-game: lose energy and reputation, gain nothing.
        if (game != null && !game.isPassed()) {
            int reputationLost = jobService.reputationPenalty(game);
            player.loseEnergy(job.getEnergyCost());
            player.loseReputation(reputationLost);
            return Outcome.failure(utils.JobResultText.failedAttemptLine(
                    job, game.getScore(), job.getEnergyCost(), reputationLost, game));
        }

        // All reward math lives in JobService and comes back as one value.
        JobRewards rewards = jobService.calculateRewards(job, player, game);
        int levelBefore = player.getLevel();

        player.loseEnergy(job.getEnergyCost());
        player.recordJobCompletion(rewards.getFinalPayment());
        player.addExperience(rewards.getExperience());
        player.gainReputation(rewards.getReputation());

        List<Challenge> completed = new ArrayList<Challenge>();
        completed.addAll(challengeService.updateChallengeProgress(
                player, Challenge.ChallengeType.COMPLETE_JOBS, 1));
        completed.addAll(challengeService.updateChallengeProgress(
                player, Challenge.ChallengeType.EARN_MONEY, rewards.getFinalPayment()));
        List<Achievement> unlocked =
                achievementService.checkAndUnlockAchievements(player);

        String message = utils.JobResultText.successLine(
                job, rewards, levelBefore, player.getLevel(), completed, unlocked);
        if (game != null) {
            message = utils.JobResultText.successWithGameLine(message, game);
        }
        return Outcome.success(message, rewards);
    }
}
