package model.career;

// Immutable result of one successful job: money, XP, and reputation.
// Built by services.JobRewardService; read by JobController and the result text.
public final class JobRewards {
    private final int basePayment;
    private final int bonusPayment;
    private final int finalPayment;
    private final int experience;
    private final int reputation;

    // ----- Construction -----
    public JobRewards(int basePayment, int bonusPayment, int finalPayment,
                      int experience, int reputation) {
        if (basePayment < 0 || bonusPayment < 0 || finalPayment < 0
                || experience < 0 || reputation < 0) {
            throw new IllegalArgumentException("job rewards must be >= 0");
        }
        this.basePayment = basePayment;
        this.bonusPayment = bonusPayment;
        this.finalPayment = finalPayment;
        this.experience = experience;
        this.reputation = reputation;
    }

    // ----- Getter -----
    public int getBasePayment() {
        return basePayment;
    }

    public int getBonusPayment() {
        return bonusPayment;
    }

    public int getFinalPayment() {
        return finalPayment;
    }

    public int getExperience() {
        return experience;
    }

    public int getReputation() {
        return reputation;
    }

    // ----- Object overrides -----
    @Override
    public String toString() {
        return "JobRewards [pay $" + finalPayment + " (base $" + basePayment
                + " + $" + bonusPayment + "), xp " + experience
                + ", rep " + reputation + "]";
    }
}
