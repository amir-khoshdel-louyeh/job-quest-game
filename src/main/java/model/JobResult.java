package model;

/**
 * Immutable value object describing the outcome of performing a {@link Job}.
 *
 * <p>Demonstrates OOP <b>Encapsulation</b> (all fields final, getters only)
 * and <b>Abstraction</b> (callers depend on this DTO instead of
 * primitive booleans). Created via static factories
 * {@link #success} / {@link #failure} (Factory Method pattern).</p>
 */
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

    private JobResult(boolean success, String message, Job job,
                      int basePayment, int bonusAmount, int finalPayment,
                      int xpGained, int reputationGained,
                      int levelBefore, int levelAfter) {
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
    }

    /** Factory for successful job completion. */
    public static JobResult success(Job job, int basePayment, int bonusAmount, int finalPayment,
                                    int xpGained, int reputationGained,
                                    int levelBefore, int levelAfter) {
        String leveledUp = levelAfter > levelBefore
                ? String.format(" 🎉 Level up! %d → %d (HP & Energy refilled)", levelBefore, levelAfter)
                : "";
        String message = String.format("✅ %s completed! +$%d (base $%d + bonus $%d) +%d XP +%d rep%s",
                job.getName(), finalPayment, basePayment, bonusAmount,
                xpGained, reputationGained, leveledUp);
        return new JobResult(true, message, job,
                basePayment, bonusAmount, finalPayment,
                xpGained, reputationGained, levelBefore, levelAfter);
    }

    /** Factory for failed job attempts (no state mutated). */
    public static JobResult failure(Job job, String reason) {
        String jobName = (job == null) ? "Job" : job.getName();
        return new JobResult(false, "❌ " + jobName + ": " + reason, job,
                0, 0, 0, 0, 0, 0, 0);
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
}
