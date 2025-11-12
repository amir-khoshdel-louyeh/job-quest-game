package controller;

/**
 * Controller responsible for economic operations (purchases, balance updates, marketplace interactions).
 *
 * Follows Single Responsibility Principle by isolating economic logic from presentation and persistence.
 */

import model.Item;
import model.Service;
import provider.ServiceProvider;

public class EconomyController {
    private final UserController userController;

    public EconomyController(UserController userController) {
        // create controller with a user controller dependency
        this.userController = userController;
    }

    public boolean purchaseService(String serviceName) {
        // attempt to buy a named service for the user.
        // OOP Step 3: polymorphic apply — Service implements Purchasable,
        // same contract as Item, so effect logic lives in the model, not here.
        Service service = ServiceProvider.getService(serviceName);
        if (service == null) return false;
        if (userController.deductBalance(service.getPrice())) {
            service.apply(userController.getUser());
            userController.refresh();
            userController.notifyObservers();
            return true;
        }
        return false;
    }

    public boolean purchaseItem(Item item) {
        // attempt to buy an item and add it to inventory
        if (item == null) return false;
        if (userController.deductBalance(item.getPrice())) {
            userController.addItemToInventory(item);
            // Step 3: progression wiring — BUY_ITEMS was previously never updated.
            services.ChallengeService.getInstance().updateChallengeProgress(
                    userController.getUser(),
                    model.Challenge.ChallengeType.BUY_ITEMS, 1);
            services.AchievementService.getInstance()
                    .checkAndUnlockAchievements(userController.getUser());
            userController.refresh();
            userController.notifyObservers();
            return true;
        }
        return false;
    }

    /**
     * Use one inventory item by name. Polymorphism does the work:
     * {@link model.Inventory#useItem(model.User, String)} calls
     * {@link Item#apply(model.User)} without instanceof checks.
     *
     * @return effect description, or null when the item is not owned
     */
    public String useItem(String itemName) {
        // consume an owned item and notify observers (Observer pattern)
        if (itemName == null) return null;
        String effect = userController.getUser().getInventory()
                .useItem(userController.getUser(), itemName);
        if (effect != null) {
            userController.refresh();
            userController.notifyObservers();
        }
        return effect;
    }
}
