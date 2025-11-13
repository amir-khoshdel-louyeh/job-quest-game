package controller;

import model.Challenge;
import model.LearnableSkill;
import model.Skill;
import model.User;
import provider.SkillProvider;
import services.AchievementService;
import services.ChallengeService;

// This class handles learning new skills.
public class SkillController {
    private UserController userController;

    // Constructor
    public SkillController(UserController userController) {
        this.userController = userController;
    }

    // Try to learn a skill by name. Returns true if learned.
    // Uses simple if, no advanced code.
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
            ChallengeService.getInstance().updateChallengeProgress(user, Challenge.ChallengeType.LEARN_SKILLS, 1);
            AchievementService.getInstance().checkAndUnlockAchievements(user);
            userController.refresh();
            userController.notifyObservers();
            return true;
        }
        return false;
    }
}
