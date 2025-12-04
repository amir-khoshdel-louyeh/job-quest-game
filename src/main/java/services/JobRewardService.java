package services;

import model.career.Job;
import model.career.JobRewards;
import model.minigame.MiniGameResult;
import model.player.PaymentStats;
import services.job.JobPaymentCalculator;
import services.job.JobRewardCalculator;
import services.contract.JobRewardRules;

// Composes the two job rule calculators into one JobRewards value.
// Payment math lives in JobPaymentCalculator; XP/rep in JobRewardCalculator.
public final class JobRewardService implements JobRewardRules {
    private final JobPaymentCalculator payments = new JobPaymentCalculator();
    private final JobRewardCalculator rewards = new JobRewardCalculator();

    // ----- Queries -----
    // One call produces every reward a successful job pays out.
    @Override
    public JobRewards calculateRewards(Job job, PaymentStats stats, MiniGameResult game) {
        if (job == null || stats == null) {
            return new JobRewards(0, 0, 0, 0, 0);
        }
        int base = job.getPayment();
        int scaledBase = payments.scaleForGame(base, game);
        int finalPayment = payments.applyGameRatio(
                payments.finalPayment(job, stats), scaledBase, base, game);
        int experience = rewards.scaleXp(rewards.xpReward(job), game);
        int reputation = rewards.reputationGain(job) + rewards.extraReputation(game);
        return new JobRewards(base, payments.bonusAmount(finalPayment, base),
                finalPayment, experience, reputation);
    }

    // A failed attempt loses reputation; the amount is a reward rule.
    @Override
    public int reputationPenalty(MiniGameResult game) {
        return rewards.reputationPenalty(game);
    }
}
