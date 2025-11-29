package model.player;

import model.career.SkillSet;

// Exactly what one job attempt needs: eligibility inputs, level for payment,
// reward application, and the goal surfaces updated afterwards.
public interface JobPlayer extends GamePlayer, PaymentStats, BatchTarget {
    SkillSet getSkillSet();

    int getEnergy();

    boolean isBlocked();

    boolean loseEnergy(int amount);

    void loseReputation(int amount);

    void recordJobCompletion(int moneyEarned);
}
