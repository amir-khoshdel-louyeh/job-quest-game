package controller;

import model.Challenge;
import model.Item;
import model.Service;
import provider.ServiceProvider;
import services.AchievementService;
import services.ChallengeService;

// This class handles money: buying services and items.
public class EconomyController {
    private UserController userController;

    // Constructor
    public EconomyController(UserController userController) {
        this.userController = userController;
    }

    // Buy a service (instant effect, example: eat food)
    // Uses Polymorphism: service.apply(user) does the work.
    public boolean purchaseService(String serviceName) {
        Service service = ServiceProvider.getService(serviceName);
        if (service == null) {
            return false;
        }
        if (userController.deductBalance(service.getPrice())) {
            service.apply(userController.getUser());
            userController.refresh();
            userController.notifyObservers();
            return true;
        }
        return false;
    }

    // Buy an item (goes to inventory, use later)
    public boolean purchaseItem(Item item) {
        if (item == null) {
            return false;
        }
        if (userController.deductBalance(item.getPrice())) {
            userController.addItemToInventory(item);
            // update buy challenge + achievements
            ChallengeService.getInstance().updateChallengeProgress(
                    userController.getUser(), Challenge.ChallengeType.BUY_ITEMS, 1);
            AchievementService.getInstance().checkAndUnlockAchievements(userController.getUser());
            userController.refresh();
            userController.notifyObservers();
            return true;
        }
        return false;
    }

    // Use an item from inventory by name.
    // Returns effect text, or null if not owned.
    public String useItem(String itemName) {
        if (itemName == null) {
            return null;
        }
        String effect = userController.getUser().getInventory().useItem(userController.getUser(), itemName);
        if (effect != null) {
            userController.refresh();
            userController.notifyObservers();
        }
        return effect;
    }
}
