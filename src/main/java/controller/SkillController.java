package controller;

import model.career.LearnableSkill;
import model.player.SkillLearner;
import model.goals.Challenge;
import provider.SkillCatalog;
import services.AchievementService;
import services.ChallengeService;

// Handles learning new skills for the player's profession.
// Package-private: only GameSession/GameController wire it.
final class SkillController {
    private final SkillLearner player;
    private final ChallengeService challengeService;
    private final AchievementService achievementService;
    private final SkillCatalog skillProvider;

    // ----- Construction -----
    // Services and catalog come from GameSession; every dependency is required.
    SkillController(SkillLearner player,
                    ChallengeService challengeService,
                    AchievementService achievementService,
                    SkillCatalog skillProvider) {
        this.player = utils.Require.notNull(player, "player");
        this.challengeService = utils.Require.notNull(challengeService, "challengeService");
        this.achievementService = utils.Require.notNull(achievementService, "achievementService");
        this.skillProvider = utils.Require.notNull(skillProvider, "skillProvider");
    }

    // ----- Commands -----
    // Learns a skill only when the profession, playtime, and money all allow it.
    public Outcome<LearnableSkill> learnSkill(String skillName) {
        LearnableSkill toLearn = skillProvider.getSkill(skillName);
        if (toLearn == null) {
            return Outcome.failure("That skill does not exist.");
        }
        if (!toLearn.isForIdentity(player.getIdentity())) {
            return Outcome.failure("That skill is not available for your profession.");
        }

        LearnableSkill.Availability availability = toLearn.availabilityFor(
                player.getSkillSet(), player.getPlayTimeMinutes());
        if (availability == LearnableSkill.Availability.LEARNED) {
            return Outcome.failure("You already learned that skill.");
        }
        if (availability == LearnableSkill.Availability.LOCKED) {
            return Outcome.failure("You need more playtime before that skill unlocks.");
        }

        if (!player.deductBalance(toLearn.getCost())) {
            return Outcome.failure("You do not have enough money for that skill.");
        }
        player.addSkill(toLearn.getName());
        challengeService.updateChallengeProgress(
                player, Challenge.ChallengeType.LEARN_SKILLS, 1);
        achievementService.checkAndUnlockAchievements(player);
        return Outcome.success("Skill learned successfully.", toLearn);
    }
}
