package controller;

import controller.contract.SkillOperations;
import model.career.Skill;
import model.player.SkillLearner;
import model.goals.ChallengeType;
import provider.contract.SkillCatalog;
import services.contract.AchievementRules;
import services.contract.ChallengeRules;
import services.contract.SkillEligibility;
import services.GateDecision;
import utils.Require;

// Handles learning new skills for the player's profession.
// Package-private: only GameSession/GameController wire it.
final class SkillController implements SkillOperations {
    private final SkillLearner player;
    private final ChallengeRules challengeRules;
    private final AchievementRules achievementRules;
    private final SkillCatalog skillCatalog;
    private final SkillEligibility skillGate;

    // ----- Construction -----
    // Abstractions and catalog come from GameSession; every dependency is required.
    SkillController(SkillLearner player,
                    ChallengeRules challengeRules,
                    AchievementRules achievementRules,
                    SkillCatalog skillCatalog,
                    SkillEligibility skillGate) {
        this.player = Require.notNull(player, "player");
        this.challengeRules = Require.notNull(challengeRules, "challengeRules");
        this.achievementRules = Require.notNull(achievementRules, "achievementRules");
        this.skillCatalog = Require.notNull(skillCatalog, "skillCatalog");
        this.skillGate = Require.notNull(skillGate, "skillGate");
    }

    // ----- Commands -----
    // Learns a skill only when the profession, playtime, and money all allow it.
    @Override
    public Outcome learnSkill(String skillName) {
        Skill toLearn = skillCatalog.findByName(skillName);
        GateDecision gate = skillGate.check(toLearn,
                player.getProfession(),
                player.getSkillSet(),
                player.getPlayTimeMinutes(),
                player.getBalance());
        if (!gate.isAllowed()) {
            return Outcome.failure(gate.getReason());
        }

        if (!player.purchaseSkill(toLearn.getName(), toLearn.getCost())) {
            return Outcome.failure("You do not have enough money for that skill.");
        }
        challengeRules.updateChallengeProgress(
                player, ChallengeType.LEARN_SKILLS, 1);
        achievementRules.checkAndUnlockAchievements(player);
        return Outcome.success("Skill learned successfully.");
    }
}
