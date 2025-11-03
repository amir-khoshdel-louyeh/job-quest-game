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
        if (userController.deductBalance(skillToLearn.getCost())) {
            userController.getUser().getSkills().add(new Skill(skillToLearn.getName()));
            userController.notifyObservers();
            return true;
        }
        return false;
    }
}
