package controller.delegator;

import controller.GameController;
import controller.SaveController;
import controller.UserController;

/**
 * Delegates save operations while translating results into action responses.
 */
public class SaveDelegator implements Delegator<SaveController> {
    private final SaveController handler;
    private final UserController userController;

    public SaveDelegator(SaveController handler, UserController userController) {
        this.handler = handler;
        this.userController = userController;
    }

    public GameController.ActionResult saveGame(long sessionStartTime) {
        boolean success = handler.saveGame(userController.getUser(), sessionStartTime);
        return success
            ? new GameController.ActionResult(true, "Game saved successfully!")
            : new GameController.ActionResult(false, "Error: Could not save game data.");
    }

    public SaveController getHandler() {
        return handler;
    }
}
