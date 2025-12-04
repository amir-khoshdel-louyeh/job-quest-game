package model.player;

import model.career.Profession;
import model.career.SkillSet;

// Exactly what one job attempt needs: eligibility inputs, the payment stats,
// reward application, and the goal surfaces updated afterwards.
public interface JobPlayer extends PlayerProgress, PaymentStats {

    Profession getProfession();

    SkillSet getSkillSet();

    int getEnergy();

    boolean isBlocked();

    boolean loseEnergy(int amount);

    void loseReputation(int amount);

    void recordJobCompletion(int moneyEarned);
}
