package services;

import java.util.Random;
import java.util.function.Supplier;

import controller.UserController;

// Handles daily sickness, energy, and health drops.
// Instance (not static): the clock/random come from outside, so sickness
// rolls are deterministic in tests (same pattern as EventService).
public class RoutineService {

    private final Random random;
    private final Supplier<Long> clock;

    public RoutineService() {
        this(new Random(), System::currentTimeMillis);
    }

    // Test seam: fixed clock + seeded random make every run reproducible.
    public RoutineService(Random random, Supplier<Long> clock) {
        if (random == null) {
            throw new IllegalArgumentException("random must not be null");
        }
        if (clock == null) {
            throw new IllegalArgumentException("clock must not be null");
        }
        this.random = random;
        this.clock = clock;
    }

    public String processSicknessCheck(UserController userController) {
        long now = clock.get();
        // 10-hour run tuning: allow sickness once per hour (was 24h),
        // lighter damage so one event can't ruin a run.
        if (now - userController.getLastSicknessTime() < 60 * 60 * 1000L) {
            return null;
        }
        // 10% chance to get sick.
        if (random.nextInt(100) < 10) {
            int healthLoss = 5 + random.nextInt(6); // Lose 5-10 health
            userController.decreaseHealth(healthLoss);
            userController.markSicknessChecked(now);
            return "🤒 You got sick and lost " + healthLoss + " health!";
        }
        // No sickness occurred
        userController.markSicknessChecked(now); // Mark checked for today
        return null;
    }
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


    // Blocks the player briefly with a small money penalty.
    // 10-hour run tuning: 5 minutes (was 48 hours), penalty 200 (was 2000),
    // so hitting 0 energy/health is a setback, not a run-ender.
    private void blockUser(UserController userController, String reason) {
        int penalty = 200;
        userController.deductBalance(penalty);
        userController.blockUser(5 * 60 * 1000L);
        // The service does not show dialogs. It just changes the state.
    }
}
