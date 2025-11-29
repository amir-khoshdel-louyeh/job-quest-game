package services.job;

import model.career.Job;
import model.minigame.MiniGameResult;
import model.player.CareerProgress;
import model.player.JobRecord;
import model.player.PaymentStats;

// All money math for one job: bonuses, final pay, and mini-game scaling.
// This class knows nothing about XP, reputation, or challenge rewards.
public final class JobPaymentCalculator {
    // Bonus rates and caps.
    public static final double LEVEL_BONUS_PER_LEVEL = 0.05;
    public static final double REPUTATION_BONUS_PER_POINT = 0.005;
    public static final double JOB_BONUS_PER_POINT = 0.02;
    public static final double MAX_LEVEL_BONUS = 5.0;
    public static final double MAX_JOB_BONUS = JobRecord.MAX_JOB_BONUS * JOB_BONUS_PER_POINT;

    // A played game always pays at least 1.
    private static final int MIN_SCALED_REWARD = 1;

    // ----- Bonus rates -----
    // Level bonus: +5% per level above 1, max +500%.
    public double levelBonus(PaymentStats stats) {
        if (stats == null) {
            return 0.0;
        }
        int level = stats.getLevel();
        if (level < 1) {
            level = 1;
        }
        double bonus = (level - 1) * LEVEL_BONUS_PER_LEVEL;
        if (bonus > MAX_LEVEL_BONUS) {
            bonus = MAX_LEVEL_BONUS;
        }
        return bonus;
    }

    // Reputation bonus: +0.5% per point, max +50%.
    public double reputationBonus(PaymentStats stats) {
        if (stats == null) {
            return 0.0;
        }
        int reputation = stats.getReputation();
        if (reputation < 0) {
            reputation = 0;
        }
        if (reputation > CareerProgress.MAX_REPUTATION) {
            reputation = CareerProgress.MAX_REPUTATION;
        }
        return reputation * REPUTATION_BONUS_PER_POINT;
    }

    // Job bonus: +2% per point, max +30%.
    public double jobBonus(PaymentStats stats) {
        if (stats == null) {
            return 0.0;
        }
        int bonusLevel = stats.getJobBonus();
        if (bonusLevel < 0) {
            bonusLevel = 0;
        }
        double bonus = bonusLevel * JOB_BONUS_PER_POINT;
        if (bonus > MAX_JOB_BONUS) {
            bonus = MAX_JOB_BONUS;
        }
        return bonus;
    }

    public double totalBonusRate(PaymentStats stats) {
        return levelBonus(stats) + reputationBonus(stats) + jobBonus(stats);
    }

    // ----- Payment -----
    public int finalPayment(Job job, PaymentStats stats) {
        if (job == null || stats == null) {
            return 0;
        }
        double pay = job.getPayment() * (1.0 + totalBonusRate(stats));
        int result = (int) Math.round(pay);
        if (result < 0) {
            result = 0;
        }
        return result;
    }

    // Base payment scaled by the game multiplier (unchanged without a game).
    public int scaleForGame(int base, MiniGameResult game) {
        if (game == null) {
            return base;
        }
        int scaled = (int) Math.round(base * game.rewardMultiplier());
        if (scaled < MIN_SCALED_REWARD) {
            scaled = MIN_SCALED_REWARD;
        }
        return scaled;
    }

    // Re-weights pay by the game ratio.
    public int applyGameRatio(int finalPay, int scaledBase, int base, MiniGameResult game) {
        if (game == null || base <= 0) {
            return finalPay;
        }
        double ratio = scaledBase / (double) base;
        int adjusted = (int) Math.round(finalPay * ratio);
        if (adjusted < MIN_SCALED_REWARD) {
            adjusted = MIN_SCALED_REWARD;
        }
        return adjusted;
    }

    // Bonus over base, never negative.
    public int bonusAmount(int finalPay, int base) {
        int bonus = finalPay - base;
        if (bonus < 0) {
            bonus = 0;
        }
        return bonus;
    }
}
