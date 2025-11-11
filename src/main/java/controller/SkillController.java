package controller;

/**
 * Controller that handles learning and managing user skills.
 *
 * Follows Single Responsibility Principle by containing only skill-related control flows.
 */

import model.LearnableSkill;
import model.Skill;
import provider.SkillProvider;

public class SkillController {
    private final UserController userController;

    public SkillController(UserController userController) {
        // construct skill controller with a user controller
        this.userController = userController;
    }

    public boolean learnSkill(String skillName) {
        // attempt to learn a named skill, charging the user if needed
        LearnableSkill skillToLearn = SkillProvider.getSkill(skillName);
        if (skillToLearn == null) return false;
        // A character can only learn its own profession's lessons —
        // defense in depth behind the school UI filter.
        // OOP Step 1: object-based check, no getClass().getSimpleName().
        if (!skillToLearn.isForIdentity(userController.getUser().getIdentity())) return false;
        // Prevent duplicate learning (encapsulation: skills list owned by User).
        boolean alreadyKnown = userController.getUser().hasSkill(skillToLearn.getName());
        if (alreadyKnown) return false;
        if (userController.deductBalance(skillToLearn.getCost())) {
            userController.getUser().getSkills().add(new Skill(skillToLearn.getName()));
            // Step 2: progression wiring — same Singleton services as JobController.
            // Mutations via UserController where possible; services mutate User
            // directly for rewards, so refresh() re-syncs Observers.
            services.ChallengeService.getInstance().updateChallengeProgress(
                    userController.getUser(),
                    model.Challenge.ChallengeType.LEARN_SKILLS, 1);
            services.AchievementService.getInstance()
                    .checkAndUnlockAchievements(userController.getUser());
            userController.refresh();
            userController.notifyObservers();
            return true;
        }
        return false;
    }
}
