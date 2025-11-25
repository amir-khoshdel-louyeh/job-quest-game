package model.player;

// Tracks jobs completed and the job bonus (+1 per 10 jobs, max +30%).
// Used by model.player.User (sole owner); read via User/UserController/JobService.
public class JobRecord {
    public static final int MAX_JOB_BONUS = 15;

    private int totalJobsCompleted = 0;
    private int jobBonus = 0;

    // ----- Getter -----
    public int getTotalJobsCompleted() {
        return totalJobsCompleted;
    }

    public int getJobBonus() {
        return jobBonus;
    }

    // ----- Setter -----
    private void setTotalJobsCompleted(int total) {
        if (total < 0) {
            total = 0;
        }
        this.totalJobsCompleted = total;
    }

    private void setJobBonus(int jobBonus) {
        if (jobBonus < 0) {
            jobBonus = 0;
        }
        if (jobBonus > MAX_JOB_BONUS) {
            jobBonus = MAX_JOB_BONUS;
        }
        this.jobBonus = jobBonus;
    }

    // ----- Commands -----
    public void addCompletedJob() {
        setTotalJobsCompleted(this.totalJobsCompleted + 1);
        if (this.totalJobsCompleted % 10 == 0) {
            grantJobBonusIncrement();
        }
    }

    public void grantStartingBonus() {
        if (this.jobBonus == 0) {
            setJobBonus(1);
        }
    }

    // ----- Private helpers -----
    private void grantJobBonusIncrement() {
        setJobBonus(this.jobBonus + 1);
    }

    // ----- Cheat harness -----
    // Package-private: only User/CheatService (same package) may call this.
    void setJobBonusForCheat(int jobBonus) {
        setJobBonus(jobBonus);
    }
}
