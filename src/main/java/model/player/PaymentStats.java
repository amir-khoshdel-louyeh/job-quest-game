package model.player;

// The progress values a job payment depends on.
// Implemented by Player; read by services.JobRewardService.
public interface PaymentStats {
    int getLevel();

    int getReputation();

    int getJobBonus();
}
