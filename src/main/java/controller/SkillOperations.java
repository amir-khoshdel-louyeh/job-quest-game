package controller;

import model.career.LearnableSkill;

// What GameController needs from the skill module.
// GameController depends on this contract, not on SkillController.
public interface SkillOperations {

    Outcome<LearnableSkill> learnSkill(String skillName);
}
