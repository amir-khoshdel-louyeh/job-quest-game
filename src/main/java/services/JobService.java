package services;

import model.Job;
import model.User;

// Figures out job pay, XP, and reputation rewards.
// Final pay = base * (1 + level bonus + reputation bonus + streak bonus).
public class JobService {

    // max bonuses
    public static final double MAX_LEVEL_BONUS = 5.0;
    public static final double MAX_STREAK_BONUS = 0.30;

    // Level bonus: +5% for each level above 1, max +500%.
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

    // Streak bonus: +2% per streak point, max +30% (15 points).
    // Streak now also grows every 10 jobs (see UserController.recordJobCompletion),
    // so a single ~10h run can reach the cap without 30 daily logins.
    public double streakBonus(User user) {
        if (user == null) {
            return 0.0;
        }
        int streak = user.getCurrentStreak();
        if (streak < 0) {
            streak = 0;
        }
        double bonus = streak * 0.02;
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

    // XP reward tuned for a ~10h run: flat 25 + payment/8.
    // Starter (~150$) gives ~43 XP, mid (~1000$) ~150 XP, end (~3500$) ~460 XP.
    // Reaching L12-15 needs ~13k-21k XP total, i.e. roughly 150-200 jobs.
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
}
