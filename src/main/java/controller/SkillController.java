package controller;

import model.LearnableSkill;
import model.Skill;
import provider.SkillProvider;

public class SkillController {
    private final UserController userController;

    public SkillController(UserController userController) {
        this.userController = userController;
    }

    public boolean learnSkill(String skillName) {
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
