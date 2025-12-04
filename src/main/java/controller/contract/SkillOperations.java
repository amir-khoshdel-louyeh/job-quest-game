package controller.contract;

import controller.Outcome;

// What GameController needs from the skill module.
// GameController depends on this contract, not on SkillController.
public interface SkillOperations {

    Outcome learnSkill(String skillName);
}
