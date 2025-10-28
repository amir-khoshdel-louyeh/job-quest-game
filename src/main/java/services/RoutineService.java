package services;

import controller.UserController;

/**
 * Handles the business logic for periodic game routines like health/energy decay and sickness.
 * This service modifies the user's state but is completely decoupled from the UI.
 */
public class RoutineService {
    private static RoutineService instance;

    private RoutineService() {}

    public static RoutineService getInstance() {
        if (instance == null) {
            instance = new RoutineService();
        }
        return instance;
    }

    /**
     * Checks if the user gets sick and applies effects if so.
     *  userController The controller for the user.
     *  A message describing the sickness event, or null if nothing happened.
     */

    public String processSicknessCheck(UserController userController) {
        long now = System.currentTimeMillis();
        // Only allow sickness once per 24 hours
        if (now - userController.getUser().getLastSicknessTime() < 24 * 60 * 60 * 1000L) {
            return null;
        }
        // 10% chance to get sick
        if (utils.RandomUtil.nextInt(100) < 10) {
            int healthLoss = 10 + utils.RandomUtil.nextInt(11); // Lose 10-20 health
            userController.decreaseHealth(healthLoss);
            userController.getUser().setLastSicknessTime(now);
            return "🤒 You got sick and lost " + healthLoss + " health!";
        }
        // No sickness occurred
        userController.getUser().setLastSicknessTime(now); // Mark checked for today
        return null;
    }

    /**
     * Decreases the user's energy based on their current health.
     *  userController The controller for the user.
     *  A message describing the event, or null if no special event occurred.
     */
    public String processEnergyDecrease(UserController userController) {
        if (userController.getEnergy() <= 0) return null;

        int energyLoss = 1; // Base energy loss
        String message = null;

        if (userController.getHealth() < 30) {
            energyLoss = 2;
            message = "⚡ Energy decreased by " + energyLoss + " due to poor health.";
        }

        userController.decreaseEnergy(energyLoss);

        if (userController.getEnergy() == 0) {
            blockUser(userController, "Energy reached 0");
        }
        return message;
    }

    /**
     * Decreases the user's health based on their current energy.
     *  userController The controller for the user.
     *  A message describing the event, or null if no special event occurred.
     */
    public String processHealthDecrease(UserController userController) {
        int healthLoss = 1;
        String message = null;

        if (userController.getEnergy() < 25_000) {
            healthLoss = 3;
            message = "❤️‍🩹 Health decreased by " + healthLoss + " due to extreme fatigue!";
        }

        userController.decreaseHealth(healthLoss);

        if (userController.getHealth() == 0) {
            blockUser(userController, "Health reached 0");
        }
        return message;
    }

    /**
     * Blocks the user and applies a penalty.
     *  userController The controller for the user.
     *  reason The reason for blocking.
     */
    private void blockUser(UserController userController, String reason) {
        int penalty = 2000;
        userController.deductBalance(penalty);
        userController.blockUser(48 * 60 * 60 * 1000L);
        // The service does not show dialogs. It just changes the state.
    }
}