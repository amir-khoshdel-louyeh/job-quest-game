package services;

/**
 * Business logic for executing and evaluating jobs (work units) in the game.
 *
 * <p>Follows Single Responsibility Principle by containing job-related domain
 * operations only (no UI, no persistence, no controller coordination).
 * Stateless and injectable so controllers depend on this abstraction
 * (Dependency Inversion) rather than inline math.</p>
 *
 * <p>Payment formula (matches README):<br>
 * {@code Final = Base × (1 + ReputationBonus + LevelBonus + StreakBonus)}}</p>
 * <ul>
 *   <li>LevelBonus = (level - 1) × 5%, capped at +500% (level 100+)</li>
 *   <li>ReputationBonus = reputation × 0.5%, max +50%</li>
 *   <li>StreakBonus = streak days × 1%, max +30%</li>
 * </ul>
 */

import model.Job;
import model.User;

public class JobService {

    /** Max level bonus (+500%). */
    public static final double MAX_LEVEL_BONUS = 5.0;
    /** Max streak bonus (+30%). */
    public static final double MAX_STREAK_BONUS = 0.30;

    /** Level bonus: +5% per level above 1. OOP: pure function, no side effects. */
    public double levelBonus(User user) {
        if (user == null) return 0.0;
        return Math.min((Math.max(1, user.getLevel()) - 1) * 0.05, MAX_LEVEL_BONUS);
    }

    /** Reputation bonus: +0.5% per reputation point, max +50%. */
    public double reputationBonus(User user) {
        if (user == null) return 0.0;
        return Math.max(0, Math.min(100, user.getReputation())) * 0.005;
    }

    /** Streak bonus: +1% per streak day, max +30%. */
    public double streakBonus(User user) {
        if (user == null) return 0.0;
        return Math.min(Math.max(0, user.getCurrentStreak()) * 0.01, MAX_STREAK_BONUS);
    }

    /** Combined bonus rate (e.g. 0.75 = +75%). */
    public double totalBonusRate(User user) {
        return levelBonus(user) + reputationBonus(user) + streakBonus(user);
    }

    /**
     * Final payment after bonuses, rounded to whole dollars.
     * Encapsulates balance math so controllers stay thin.
     */
    public int calculateFinalPayment(Job job, User user) {
        if (job == null || user == null) return 0;
        double finalPay = job.getPayment() * (1.0 + totalBonusRate(user));
        return Math.max(0, (int) Math.round(finalPay));
    }

    /**
     * XP reward scales with pay and effort so early jobs still progress.
     * Polymorphism-friendly: depends only on {@link Job} abstraction.
     */
    public int calculateXpReward(Job job) {
        if (job == null) return 0;
        return Math.max(10, job.getPayment() / 10 + job.getEnergyCost() / 2000);
    }

    /**
     * Reputation gain: 1 base, +1 for $1000+ jobs, +1 more for $5000+ jobs.
     * Rewards risk without inflating early game.
     */
    public int calculateReputationGain(Job job) {
        if (job == null) return 0;
        int gain = 1;
        if (job.getPayment() >= 1000) gain++;
        if (job.getPayment() >= 5000) gain++;
        return gain;
    }
}
