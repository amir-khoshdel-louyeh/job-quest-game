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

    public SkillController(UserController userController) {
        this(userController, new ChallengeService(), new AchievementService(), new SkillProvider());
    }

    // Injection seam: services come from outside (testable, swappable).
    public SkillController(UserController userController,
                           ChallengeService challengeService,
                           AchievementService achievementService) {
        this(userController, challengeService, achievementService, new SkillProvider());
    }

    // Full injection seam: skill catalog comes from outside (testable).
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

    // Tries to learn a skill by name. Returns true if learned.
    public boolean learnSkill(String skillName) {
        LearnableSkill toLearn = skillProvider.getSkill(skillName);
        if (toLearn == null) {
            return false;
        }
        User user = userController.getUser();
        // only own profession can learn it
        if (!toLearn.isForIdentity(user.getIdentity())) {
            return false;
        }
        // already known?
        if (user.hasSkill(toLearn.getName())) {
            return false;
        }
        // try to pay
        if (userController.deductBalance(toLearn.getCost())) {
            userController.addSkill(toLearn.getName());
            // update challenges + achievements
            challengeService.updateChallengeProgress(user, Challenge.ChallengeType.LEARN_SKILLS, 1);
            achievementService.checkAndUnlockAchievements(user);
            // Single coherent notification: addSkill already sent SKILLS;
            // refresh() blasts once so balance/cost change repaints too.
            // (Previously refresh()+notifyObservers() fired two identical blasts.)
            userController.refresh();
            return true;
        }
        return false;
    }
}
