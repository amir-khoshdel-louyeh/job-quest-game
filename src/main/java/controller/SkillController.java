package controller;

import model.career.LearnableSkill;
import model.career.SkillAvailability;
import model.player.SkillLearner;
import model.goals.ChallengeType;
import provider.SkillCatalog;
import services.AchievementRules;
import services.ChallengeRules;

// Handles learning new skills for the player's profession.
// Package-private: only GameSession/GameController wire it.
final class SkillController implements SkillOperations {
    private final SkillLearner player;
    private final ChallengeRules challengeRules;
    private final AchievementRules achievementRules;
    private final SkillCatalog skillProvider;

    // ----- Construction -----
    // Abstractions and catalog come from GameSession; every dependency is required.
    SkillController(SkillLearner player,
                    ChallengeRules challengeRules,
                    AchievementRules achievementRules,
                    SkillCatalog skillProvider) {
        this.player = utils.Require.notNull(player, "player");
        this.challengeRules = utils.Require.notNull(challengeRules, "challengeRules");
        this.achievementRules = utils.Require.notNull(achievementRules, "achievementRules");
        this.skillProvider = utils.Require.notNull(skillProvider, "skillProvider");
    }

    // ----- Commands -----
    // Learns a skill only when the profession, playtime, and money all allow it.
    @Override
    public Outcome learnSkill(String skillName) {
        LearnableSkill toLearn = skillProvider.getSkill(skillName);
        if (toLearn == null) {
            return Outcome.failure("That skill does not exist.");
        }
        if (!toLearn.isForIdentity(player.getIdentity())) {
            return Outcome.failure("That skill is not available for your profession.");
        }

        SkillAvailability availability = toLearn.availabilityFor(
                player.getSkillSet(), player.getPlayTimeMinutes());
        if (availability == SkillAvailability.LEARNED) {
            return Outcome.failure("You already learned that skill.");
        }
        if (availability == SkillAvailability.LOCKED) {
            return Outcome.failure("You need more playtime before that skill unlocks.");
        }

        if (!player.deductBalance(toLearn.getCost())) {
            return Outcome.failure("You do not have enough money for that skill.");
        }
        player.addSkill(toLearn.getName());
        challengeRules.updateChallengeProgress(
                player, ChallengeType.LEARN_SKILLS, 1);
        achievementRules.checkAndUnlockAchievements(player);
        return Outcome.success("Skill learned successfully.");
    }
}
