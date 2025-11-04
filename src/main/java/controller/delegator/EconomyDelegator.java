package controller.delegator;

import controller.EconomyController;
import controller.GameController;
import model.Item;

/**
 * Delegates economic actions to the underlying handler while normalizing responses.
 */
public class EconomyDelegator implements Delegator<EconomyController> {
    private final EconomyController handler;

    public EconomyDelegator(EconomyController handler) {
        this.handler = handler;
    }

    public GameController.ActionResult purchaseService(String serviceName) {
        return wrap(handler.purchaseService(serviceName),
            "Service purchased successfully.",
            "Failed to purchase service.");
    }

    public GameController.ActionResult purchaseItem(Item item) {
        return wrap(handler.purchaseItem(item),
            "Item purchased successfully.",
            "Failed to purchase item.");
    }

    public EconomyController getHandler() {
        return handler;
    }

    private GameController.ActionResult wrap(boolean success, String successMessage, String failureMessage) {
        return success
            ? new GameController.ActionResult(true, successMessage)
            : new GameController.ActionResult(false, failureMessage);
    }
}
