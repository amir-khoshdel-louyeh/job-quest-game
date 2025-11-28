package services;

import model.career.Job;
import model.minigame.MiniGameResult;
import model.player.User;

// Figures out job pay, XP, and reputation rewards.
// Final pay = base * (1 + level + reputation + job bonus).
public class JobService {

    // ----- Constants -----
    // Max bonuses.
    public static final double MAX_LEVEL_BONUS = 5.0;
    public static final double MAX_JOB_BONUS = 0.30;

    // ----- Queries -----
    // Level bonus: +5% per level above 1, max +500%.
    public double levelBonus(User user) {
        if (user == null) {
            return 0.0;
        }
        int level = user.getLevel();
        if (level < 1) {
            level = 1;
        }
        double bonus = (level - 1) * 0.05;
        if (bonus > MAX_LEVEL_BONUS) {
            bonus = MAX_LEVEL_BONUS;
        }
        return bonus;
    }

    // Reputation bonus: +0.5% per point, max +50%.
    public double reputationBonus(User user) {
        if (user == null) {
            return 0.0;
        }
        int rep = user.getReputation();
        if (rep < 0) {
            rep = 0;
        }
        if (rep > 100) {
            rep = 100;
        }
        return rep * 0.005;
    }

    // Job bonus: +2% per point, max +30%.
    public double jobBonus(User user) {
        if (user == null) {
            return 0.0;
        }
        int bonusLevel = user.getJobBonus();
        if (bonusLevel < 0) {
            bonusLevel = 0;
        }
        double bonus = bonusLevel * 0.02;
        if (bonus > MAX_JOB_BONUS) {
            bonus = MAX_JOB_BONUS;
        }
        return bonus;
    }

    // Adds all bonuses together.
    public double totalBonusRate(User user) {
        double total = levelBonus(user) + reputationBonus(user) + jobBonus(user);
        return total;
    }

    // Final payment after bonuses.
    public int calculateFinalPayment(Job job, User user) {
        if (job == null || user == null) {
            return 0;
        }
        double bonus = totalBonusRate(user);
        double pay = job.getPayment() * (1.0 + bonus);
        int result = (int) Math.round(pay);
        if (result < 0) {
            result = 0;
        }
        return result;
    }

    // XP reward: flat 25 + payment/8, tuned for a 10h run.
    public int calculateXpReward(Job job) {
        if (job == null) {
            return 0;
        }
        int xp = 25 + job.getPayment() / 8;
        if (xp < 10) {
            xp = 10;
        }
        return xp;
    }

    // Reputation gain: 1 base, +1 if payment >= 1000, +1 more if >= 5000.
    public int calculateReputationGain(Job job) {
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

    // ----- Mini-game scaling -----
    // Score 80+ earns +1 reputation, perfect score +2.
    public static final int SCORE_FOR_EXTRA_REPUTATION = 80;
    public static final int SCORE_FOR_PERFECT_REPUTATION = 100;
    // A played game always pays at least 1.
    private static final int MIN_SCALED_REWARD = 1;

    // Base payment scaled by the game multiplier (unchanged without a game).
    public int scalePayment(int base, MiniGameResult game) {
        if (game == null) {
            return base;
        }
        int scaled = (int) Math.round(base * game.rewardMultiplier());
        if (scaled < MIN_SCALED_REWARD) {
            scaled = MIN_SCALED_REWARD;
        }
        return scaled;
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

    // Great games earn +1 reputation, perfect games +2.
    public int extraReputation(MiniGameResult game) {
        if (game != null && game.getScore() >= SCORE_FOR_PERFECT_REPUTATION) {
            return 2;
        }
        if (game != null && game.getScore() >= SCORE_FOR_EXTRA_REPUTATION) {
            return 1;
        }
        return 0;
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
