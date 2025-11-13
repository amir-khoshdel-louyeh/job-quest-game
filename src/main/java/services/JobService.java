package services;

import model.Job;
import model.User;

// This class calculates payment and rewards for jobs.
// Formula: Final = Base * (1 + levelBonus + repBonus + streakBonus)
public class JobService {

    // max bonuses
    public static final double MAX_LEVEL_BONUS = 5.0;
    public static final double MAX_STREAK_BONUS = 0.30;

    // Level bonus: +5% for each level above 1, max +500%.
    // Uses simple if instead of nested Math.min/max.
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

    // Streak bonus: +1% per day, max +30%.
    public double streakBonus(User user) {
        if (user == null) {
            return 0.0;
        }
        int streak = user.getCurrentStreak();
        if (streak < 0) {
            streak = 0;
        }
        double bonus = streak * 0.01;
        if (bonus > MAX_STREAK_BONUS) {
            bonus = MAX_STREAK_BONUS;
        }
        return bonus;
    }

    // Add all bonuses together
    public double totalBonusRate(User user) {
        double total = levelBonus(user) + reputationBonus(user) + streakBonus(user);
        return total;
    }

    // Final payment after bonuses, uses simple if and Math.round
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

    // XP reward: bigger jobs give more XP, minimum 10.
    public int calculateXpReward(Job job) {
        if (job == null) {
            return 0;
        }
        int xp = job.getPayment() / 10 + job.getEnergyCost() / 2000;
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
}
