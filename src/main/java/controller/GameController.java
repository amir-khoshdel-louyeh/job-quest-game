package controller;

import model.User;

public class GameController {
    private UserController userController;

    public GameController(User user) {
        this.userController = new UserController(user);
    }

    public ActionResult buyFood() {
        if (userController.deductBalance(50)) {
            userController.increaseEnergy(5000);
            return new ActionResult(true, userController.getUsername() + " bought food (+Energy, -$50)");
        } else {
            return new ActionResult(false, "Not enough money to buy food!");
        }
    }

    public ActionResult visitDoctor() {
        if (userController.deductBalance(100)) {
            userController.increaseHealth(20);
            return new ActionResult(true, userController.getUsername() + " visited doctor (+Health, -$100)");
        } else {
            return new ActionResult(false, "Not enough money to visit doctor!");
        }
    }

    public ActionResult doWork() {
        User user = userController.getUser();
        if (user.getIdentity().getClass().getSimpleName().equals("Freelancer")) {
            return new ActionResult(false, "FREELANCER_TASK_DIALOG");
        } else {
            user.getIdentity().performDailyWork(user);
            return new ActionResult(true, userController.getUsername() + " did daily work. Current balance: $" + userController.getBalance());
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
