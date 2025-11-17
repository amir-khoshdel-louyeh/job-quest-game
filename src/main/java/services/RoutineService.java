package services;

import controller.UserController;
import utils.RandomUtil;

// Handles daily sickness, energy, and health drops.
public class RoutineService {
    private RoutineService() {}

    public static String processSicknessCheck(UserController userController) {
        long now = System.currentTimeMillis();
        // 10-hour run tuning: allow sickness once per hour (was 24h),
        // lighter damage so one event can't ruin a run.
        if (now - userController.getLastSicknessTime() < 60 * 60 * 1000L) {
            return null;
        }
        // 10% chance to get sick.
        if (RandomUtil.getRandomInt(100) < 10) {
            int healthLoss = 5 + RandomUtil.getRandomInt(6); // Lose 5-10 health
            userController.decreaseHealth(healthLoss);
            userController.markSicknessChecked(now);
            return "🤒 You got sick and lost " + healthLoss + " health!";
        }
        // No sickness occurred
        userController.markSicknessChecked(now); // Mark checked for today
        return null;
    }
    public static String processEnergyDecrease(UserController userController) {
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
    public static String processHealthDecrease(UserController userController) {
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

    
    // Blocks the player briefly with a small money penalty.
    // 10-hour run tuning: 5 minutes (was 48 hours), penalty 200 (was 2000),
    // so hitting 0 energy/health is a setback, not a run-ender.
    private static void blockUser(UserController userController, String reason) {
        int penalty = 200;
        userController.deductBalance(penalty);
        userController.blockUser(5 * 60 * 1000L);
        // The service does not show dialogs. It just changes the state.
    }
}