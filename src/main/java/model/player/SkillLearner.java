package model.player;

import model.Identity;
import model.career.SkillSet;

// Exactly what one learn attempt needs: eligibility, purchase, and the goal
// surfaces updated afterwards.
public interface SkillLearner extends ChallengePlayer, AchievementPlayer {
    Identity getIdentity();

    SkillSet getSkillSet();

    long getPlayTimeMinutes();

    boolean deductBalance(int amount);

    boolean addSkill(String skillName);
}
