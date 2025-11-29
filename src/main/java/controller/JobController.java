package controller;

import model.career.Job;
import model.career.JobRewards;
import model.minigame.MiniGameResult;
import model.player.JobPlayer;
import model.goals.Achievement;
import model.goals.Challenge;
import model.goals.ChallengeType;
import services.AchievementRules;
import services.ChallengeRules;
import services.job.JobEligibility;
import services.job.JobGateDecision;
import services.job.JobRewardRules;
import java.util.ArrayList;
import java.util.List;

// Runs one job: checks energy, rolls payment, hands out XP.
// Package-private: only GameSession/GameController wire it.
final class JobController implements JobOperations {
    private final JobPlayer player;
    private final JobRewardRules rewards;
    private final JobEligibility jobGate;
    private final ChallengeRules challengeRules;
    private final AchievementRules achievementRules;

    // ----- Construction -----
    // Abstractions come from GameSession, so this class never names a concrete rule.
    JobController(JobPlayer player, JobRewardRules rewards, JobEligibility jobGate,
                  ChallengeRules challengeRules, AchievementRules achievementRules) {
        this.player = utils.Require.notNull(player, "player");
        this.rewards = utils.Require.notNull(rewards, "rewards");
        this.jobGate = utils.Require.notNull(jobGate, "jobGate");
        this.challengeRules = utils.Require.notNull(challengeRules, "challengeRules");
        this.achievementRules = utils.Require.notNull(achievementRules, "achievementRules");
    }

    // ----- Commands -----
    // Runs a job without a mini-game.
    @Override
    public Outcome doJob(Job job) {
        return execute(job, null);
    }

    // Runs a job with a mini-game result.
    @Override
    public Outcome doJob(Job job, MiniGameResult game) {
        return execute(job, game);
    }

    private Outcome execute(Job job, MiniGameResult game) {
        JobGateDecision gate = jobGate.check(job,
                player.getIdentity(),
                player.getSkillSet(),
                player.getEnergy(),
                player.isBlocked());
        if (!gate.isAllowed()) {
            return Outcome.failure(utils.JobResultText.failureLine(job, gate.getReason()));
        }

        // Failed mini-game: lose energy and reputation, gain nothing.
        if (game != null && !game.isPassed()) {
            int reputationLost = rewards.reputationPenalty(game);
            player.loseEnergy(job.getEnergyCost());
            player.loseReputation(reputationLost);
            return Outcome.failure(utils.JobResultText.failedAttemptLine(
                    job, game.getScore(), job.getEnergyCost(), reputationLost, game));
        }

        // All reward math lives in JobService and comes back as one value.
        JobRewards payout = rewards.calculateRewards(job, player, game);
        int levelBefore = player.getLevel();

        player.loseEnergy(job.getEnergyCost());
        player.recordJobCompletion(payout.getFinalPayment());
        player.addExperience(payout.getExperience());
        player.gainReputation(payout.getReputation());

        List<Challenge> completed = new ArrayList<Challenge>();
        completed.addAll(challengeRules.updateChallengeProgress(
                player, ChallengeType.COMPLETE_JOBS, 1));
        completed.addAll(challengeRules.updateChallengeProgress(
                player, ChallengeType.EARN_MONEY, payout.getFinalPayment()));
        List<Achievement> unlocked =
                achievementRules.checkAndUnlockAchievements(player);

        String message = utils.JobResultText.successLine(
                job, payout, levelBefore, player.getLevel(), completed, unlocked);
        if (game != null) {
            message = utils.JobResultText.successWithGameLine(message, game);
        }
        return Outcome.success(message);
    }
}
