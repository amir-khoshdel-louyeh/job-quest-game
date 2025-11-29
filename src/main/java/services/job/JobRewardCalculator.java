package services.job;

import model.career.Job;
import model.minigame.MiniGameResult;

// XP and reputation earned or lost from a job attempt.
// Money math lives in JobPaymentCalculator.
public final class JobRewardCalculator {
    // Score thresholds and penalties.
    public static final int SCORE_FOR_EXTRA_REPUTATION = 80;
    public static final int SCORE_FOR_PERFECT_REPUTATION = 100;
    public static final int FAILED_ATTEMPT_REPUTATION_PENALTY = 1;
    public static final int BOTCHED_ATTEMPT_REPUTATION_PENALTY = 2;

    private static final int BASE_XP = 25;
    private static final int PAYMENT_PER_XP = 8;
    private static final int MIN_XP = 10;
    private static final int MIN_SCALED_REWARD = 1;

    // ----- XP -----
    // XP reward: flat 25 + payment/8, tuned for a 10h run.
    public int xpReward(Job job) {
        if (job == null) {
            return 0;
        }
        int xp = BASE_XP + job.getPayment() / PAYMENT_PER_XP;
        if (xp < MIN_XP) {
            xp = MIN_XP;
        }
        return xp;
    }

    // XP scaled by the game multiplier (unchanged without a game).
    public int scaleXp(int xp, MiniGameResult game) {
        if (game == null) {
            return xp;
        }
        int scaled = (int) Math.round(xp * game.rewardMultiplier());
        if (scaled < MIN_SCALED_REWARD) {
            scaled = MIN_SCALED_REWARD;
        }
        return scaled;
    }

    // ----- Reputation -----
    // Reputation gain: 1 base, +1 if payment >= 1000, +1 more if >= 5000.
    public int reputationGain(Job job) {
        if (job == null) {
            return 0;
        }
        int gain = 1;
        if (job.getPayment() >= 1000) {
            gain = gain + 1;
        }
        if (job.getPayment() >= 5000) {
            gain = gain + 1;
        }
        return gain;
    }

    // Great games earn +1 reputation, perfect games +2.
    public int extraReputation(MiniGameResult game) {
        if (game == null) {
            return 0;
        }
        if (game.getScore() >= SCORE_FOR_PERFECT_REPUTATION) {
            return 2;
        }
        if (game.getScore() >= SCORE_FOR_EXTRA_REPUTATION) {
            return 1;
        }
        return 0;
    }

    // A failed game costs 2 reputation at zero score, otherwise 1.
    public int reputationPenalty(MiniGameResult game) {
        if (game == null) {
            return 0;
        }
        if (game.getScore() == 0) {
            return BOTCHED_ATTEMPT_REPUTATION_PENALTY;
        }
        return FAILED_ATTEMPT_REPUTATION_PENALTY;
    }
}
