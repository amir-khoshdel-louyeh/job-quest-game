package model.player;

import model.career.Profession;
import model.career.SkillSet;

// Exactly what one learn attempt needs: eligibility, purchase, and the goal
// surfaces updated afterwards.
public interface SkillLearner extends PlayerProgress {

    Profession getProfession();

    SkillSet getSkillSet();

    long getPlayTimeMinutes();

    boolean deductBalance(int amount);

    boolean addSkill(String skillName);
}
