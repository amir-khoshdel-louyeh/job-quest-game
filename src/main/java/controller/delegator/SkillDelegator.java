package controller.delegator;

import controller.GameController;
import controller.SkillController;

/**
 * Delegates skill acquisition requests to the skill handler.
 */
public class SkillDelegator implements Delegator<SkillController> {
    private final SkillController handler;

    public SkillDelegator(SkillController handler) {
        this.handler = handler;
    }

    public GameController.ActionResult learnSkill(String skillName) {
        boolean success = handler.learnSkill(skillName);
        return success
            ? new GameController.ActionResult(true, "Skill learned successfully.")
            : new GameController.ActionResult(false, "Failed to learn skill.");
    }

    public SkillController getHandler() {
        return handler;
    }
}
