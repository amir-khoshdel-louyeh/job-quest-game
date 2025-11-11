package model;

/**
 * Immutable value object describing the outcome of performing a {@link Job}.
 *
 * <p>Demonstrates OOP <b>Encapsulation</b> (all fields final, getters only)
 * and <b>Abstraction</b> (callers depend on this DTO instead of
 * primitive booleans). Created via static factories
 * {@link #success} / {@link #failure} (Factory Method pattern).</p>
 */
import java.util.Collections;
import java.util.List;

public final class JobResult {
    private final boolean success;
    private final String message;
    private final Job job;
    private final int basePayment;
    private final int bonusAmount;
    private final int finalPayment;
    private final int xpGained;
    private final int reputationGained;
    private final int levelBefore;
    private final int levelAfter;
    private final List<Challenge> completedChallenges;
    private final List<Achievement> newAchievements;

    private JobResult(boolean success, String message, Job job,
                      int basePayment, int bonusAmount, int finalPayment,
                      int xpGained, int reputationGained,
                      int levelBefore, int levelAfter,
                      List<Challenge> completedChallenges,
                      List<Achievement> newAchievements) {
        this.success = success;
        this.message = message;
        this.job = job;
        this.basePayment = basePayment;
        this.bonusAmount = bonusAmount;
        this.finalPayment = finalPayment;
        this.xpGained = xpGained;
        this.reputationGained = reputationGained;
        this.levelBefore = levelBefore;
        this.levelAfter = levelAfter;
        // Defensive copies keep the DTO immutable (Encapsulation).
        this.completedChallenges = completedChallenges == null
                ? Collections.emptyList()
                : Collections.unmodifiableList(new java.util.ArrayList<>(completedChallenges));
        this.newAchievements = newAchievements == null
                ? Collections.emptyList()
                : Collections.unmodifiableList(new java.util.ArrayList<>(newAchievements));
    }

    /** Factory for successful job completion (no progression extras). */
    public static JobResult success(Job job, int basePayment, int bonusAmount, int finalPayment,
                                    int xpGained, int reputationGained,
                                    int levelBefore, int levelAfter) {
        return success(job, basePayment, bonusAmount, finalPayment,
                xpGained, reputationGained, levelBefore, levelAfter,
                Collections.emptyList(), Collections.emptyList());
    }

    /**
     * Factory for successful job completion with progression extras.
     * Message construction stays encapsulated here so controllers stay thin.
     */
    public static JobResult success(Job job, int basePayment, int bonusAmount, int finalPayment,
                                    int xpGained, int reputationGained,
                                    int levelBefore, int levelAfter,
                                    List<Challenge> completedChallenges,
                                    List<Achievement> newAchievements) {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("✅ %s completed! +$%d (base $%d + bonus $%d) +%d XP +%d rep",
                job.getName(), finalPayment, basePayment, bonusAmount,
                xpGained, reputationGained));
        if (levelAfter > levelBefore) {
            sb.append(String.format(" 🎉 Level up! %d → %d (HP & Energy refilled)", levelBefore, levelAfter));
        }
        if (completedChallenges != null) {
            for (Challenge c : completedChallenges) {
                sb.append(String.format("%n🏆 Challenge complete: %s (+$%d +%d XP)",
                        c.getName(), c.getRewardMoney(), c.getRewardExperience()));
            }
        }
        if (newAchievements != null) {
            for (Achievement a : newAchievements) {
                sb.append(String.format("%n🏅 Achievement unlocked: %s (+$%d)",
                        a.getName(), a.getRewardMoney()));
            }
        }
        return new JobResult(true, sb.toString(), job,
                basePayment, bonusAmount, finalPayment,
                xpGained, reputationGained, levelBefore, levelAfter,
                completedChallenges, newAchievements);
    }

    /**
     * Mini-game overload (Open/Closed): same as above plus the game summary
     * line from {@link model.minigame.MiniGameResult#summaryLine()}.
     */
    public static JobResult success(Job job, int basePayment, int bonusAmount, int finalPayment,
                                    int xpGained, int reputationGained,
                                    int levelBefore, int levelAfter,
                                    List<Challenge> completedChallenges,
                                    List<Achievement> newAchievements,
                                    model.minigame.MiniGameResult game) {
        JobResult base = success(job, basePayment, bonusAmount, finalPayment,
                xpGained, reputationGained, levelBefore, levelAfter,
                completedChallenges, newAchievements);
        if (game == null) return base;
        String withGame = base.getMessage() + String.format("%n%s", game.summaryLine());
        return new JobResult(true, withGame, job,
                basePayment, bonusAmount, finalPayment,
                xpGained, reputationGained, levelBefore, levelAfter,
                completedChallenges, newAchievements);
    }

    /** Factory for failed job attempts (no state mutated). */
    public static JobResult failure(Job job, String reason) {
        String jobName = (job == null) ? "Job" : job.getName();
        return new JobResult(false, "❌ " + jobName + ": " + reason, job,
                0, 0, 0, 0, 0, 0, 0, Collections.emptyList(), Collections.emptyList());
    }

    /**
     * Factory for a started-but-failed attempt (Fail state): unlike
     * {@link #failure}, energy WAS spent doing the work, so this DTO reports
     * an already-applied penalty. No pay, XP, rep gain or progression.
     */
    public static JobResult failedAttempt(Job job, int score, int energyLost, int repLost,
                                          model.minigame.MiniGameResult game) {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("❌ %s failed! Score %d/100 (need 50+). Energy -%d",
                job.getName(), score, energyLost));
        if (repLost > 0) {
            sb.append(String.format(", Reputation -%d (botched job)", repLost));
        }
        sb.append(". No pay, XP or progress. Rest up and retry — Cancel stays free.");
        if (game != null) {
            sb.append(String.format("%n%s", game.summaryLine()));
        }
        return new JobResult(false, sb.toString(), job,
                0, 0, 0, 0, 0, 0, 0, Collections.emptyList(), Collections.emptyList());
    }

    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
    public Job getJob() { return job; }
    public int getBasePayment() { return basePayment; }
    public int getBonusAmount() { return bonusAmount; }
    public int getFinalPayment() { return finalPayment; }
    public int getXpGained() { return xpGained; }
    public int getReputationGained() { return reputationGained; }
    public int getLevelBefore() { return levelBefore; }
    public int getLevelAfter() { return levelAfter; }
    /** True when this job caused a level-up. */
    public boolean didLevelUp() { return levelAfter > levelBefore; }
    /** Challenges completed by this job (immutable, never null). */
    public List<Challenge> getCompletedChallenges() { return completedChallenges; }
    /** Achievements unlocked by this job (immutable, never null). */
    public List<Achievement> getNewAchievements() { return newAchievements; }
}
