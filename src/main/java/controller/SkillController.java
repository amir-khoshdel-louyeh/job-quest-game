package controller;

import model.Challenge;
import model.LearnableSkill;
import model.Skill;
import model.User;
import provider.SkillProvider;
import services.AchievementService;
import services.ChallengeService;

// Handles learning new skills for the player's profession.
public class SkillController {
    private UserController userController;

    public SkillController(UserController userController) {
        this.userController = userController;
    }

    // Tries to learn a skill by name. Returns true if learned.
    public boolean learnSkill(String skillName) {
        LearnableSkill toLearn = SkillProvider.getSkill(skillName);
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
            userController.addSkill(new Skill(toLearn.getName()));
            // update challenges + achievements
            ChallengeService.updateChallengeProgress(user, Challenge.ChallengeType.LEARN_SKILLS, 1);
            AchievementService.checkAndUnlockAchievements(user);
            userController.refresh();
            userController.notifyObservers();
            return true;
        }
        return false;
    }
}
