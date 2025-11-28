package controller;

import model.career.LearnableSkill;
import model.player.User;
import model.goals.Challenge;
import provider.SkillProvider;
import services.AchievementService;
import services.ChallengeService;

// Handles learning new skills for the player's profession.
public class SkillController {
    private UserController userController;
    private ChallengeService challengeService;
    private AchievementService achievementService;
    private SkillProvider skillProvider;

    // ----- Construction -----
    public SkillController(UserController userController) {
        this(userController, new ChallengeService(), new AchievementService(), new SkillProvider());
    }

    // Services come from outside.
    public SkillController(UserController userController,
                           ChallengeService challengeService,
                           AchievementService achievementService) {
        this(userController, challengeService, achievementService, new SkillProvider());
    }

    // Skill catalog comes from outside.
    public SkillController(UserController userController,
                           ChallengeService challengeService,
                           AchievementService achievementService,
                           SkillProvider skillProvider) {
        if (userController == null) {
            throw new IllegalArgumentException("userController must not be null");
        }
        this.userController = userController;
        if (challengeService != null) {
            this.challengeService = challengeService;
        } else {
            this.challengeService = new ChallengeService();
        }
        if (achievementService != null) {
            this.achievementService = achievementService;
        } else {
            this.achievementService = new AchievementService();
        }
        if (skillProvider != null) {
            this.skillProvider = skillProvider;
        } else {
            this.skillProvider = new SkillProvider();
        }
    }

    // ----- Commands -----
    // Learns a skill; returns true if learned.
    public boolean learnSkill(String skillName) {
        LearnableSkill toLearn = skillProvider.getSkill(skillName);
        if (toLearn == null) {
            return false;
        }
        User user = userController.getUser();
        if (!toLearn.isForIdentity(user.getIdentity())) {
            return false;
        }
        if (user.hasSkill(toLearn.getName())) {
            return false;
        }
        if (userController.deductBalance(toLearn.getCost())) {
            userController.addSkill(toLearn.getName());
            challengeService.updateChallengeProgress(user, Challenge.ChallengeType.LEARN_SKILLS, 1);
            achievementService.checkAndUnlockAchievements(user);
            userController.refresh();
            return true;
        }
        return false;
    }
}
