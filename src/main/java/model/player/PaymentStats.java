package model.player;

// The progress values a job payment depends on.
// Implemented by User; read by services.JobService.
public interface PaymentStats {
    int getLevel();

    int getReputation();

    int getJobBonus();
}
