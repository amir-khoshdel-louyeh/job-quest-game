package controller;

import database.DatabaseUtil;
import model.WorkResult;
import model.Service;
import model.ServiceProvider;
import model.User;

public class GameController {
    private UserController userController;

    public GameController(User user) {
        this.userController = new UserController(user);
    }

    public ActionResult purchaseService(String serviceName) {
        Service service = ServiceProvider.getService(serviceName);
        if (service == null) {
            return new ActionResult(false, "Service '" + serviceName + "' not found.");
        }

        if (userController.deductBalance(service.getCost())) {
            userController.increaseEnergy(service.getEnergyEffect());
            userController.increaseHealth(service.getHealthEffect());
            userController.notifyObservers(); // Notify after all changes are done
            String message = String.format("%s used %s. Cost: $%d.",
                    userController.getUsername(),
                    service.getName(),
                    service.getCost());
            return new ActionResult(true, message);
        } else {
            return new ActionResult(false, "Not enough money for " + service.getName() + "!");
        }
    }

    public ActionResult doWork() {
        WorkResult workResult = userController.getUser().getIdentity().performDailyWork();

        if (workResult.getType() == WorkResult.Type.REQUIRES_DIALOG) {
            return new ActionResult(false, "FREELANCER_TASK_DIALOG"); // Special message for the view
        }

        if (userController.getEnergy() >= workResult.getEnergyCost()) {
            userController.addBalance(workResult.getMoneyEarned());
            userController.decreaseEnergy(workResult.getEnergyCost());
            userController.notifyObservers(); // Notify after all changes are done
            String message = String.format("%s did daily work. Earned $%d, used %d energy. Current balance: $%d",
                    userController.getUsername(), workResult.getMoneyEarned(), workResult.getEnergyCost(), userController.getBalance());
            return new ActionResult(true, message);
        } else {
            return new ActionResult(false, "Not enough energy to work!");
        }
    }

    public ActionResult saveGame() {
        boolean success = DatabaseUtil.updateUser(userController.getUser());
        if (success) {
            return new ActionResult(true, "Game saved successfully!");
        } else {
            return new ActionResult(false, "Error: Could not save game data.");
        }
    }

    public UserController getUserController() {
        return userController;
    }
    
    // Routine actions can be handled similarly, returning results or status objects
    // ...
    
    // Helper class for action results
    public static class ActionResult {
        public final boolean success;
        public final String message;

        public ActionResult(boolean success, String message) {
            this.success = success;
            this.message = message;
        }
    }
}
