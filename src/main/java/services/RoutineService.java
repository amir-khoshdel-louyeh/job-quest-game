package services;

import java.util.Random;
import java.util.function.Supplier;

import model.player.User;
import utils.GameBalance;

// Handles periodic sickness, energy, and health drops.
// Instance (not static): the clock/random come from outside, so sickness
// rolls are deterministic in tests.
// Works on the User model directly — never on UserController (layering:
// service -> model, not service -> controller). State changes are silent;
// the caller (RoutineController) owns all observer notifications.
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

    public String processSicknessCheck(User user) {
        long now = clock.get();
        // 10-hour run tuning: allow sickness once per hour (was 24h),
        // lighter damage so one event can't ruin a run.
        if (now - user.getLastSicknessTime() < GameBalance.SICKNESS_COOLDOWN_MILLIS) {
            return null;
        }
        // 10% chance to get sick.
        if (random.nextInt(GameBalance.SICKNESS_ROLL_BOUND) < GameBalance.SICKNESS_ROLL_HIT_BELOW) {
            int healthLoss = GameBalance.SICKNESS_MIN_HEALTH_LOSS
                    + random.nextInt(GameBalance.SICKNESS_HEALTH_LOSS_RANGE); // Lose 5-10 health
            user.loseHealth(healthLoss);
            user.markSicknessChecked(now);
            return "🤒 You got sick and lost " + healthLoss + " health!";
        }
        // No sickness occurred
        user.markSicknessChecked(now); // Mark checked for today
        return null;
    }
    public String processEnergyDecrease(User user) {
        if (user.getEnergy() <= 0) return null;

        int energyLoss = GameBalance.BASE_ENERGY_LOSS;
        String message = null;

        if (user.getHealth() < GameBalance.LOW_HEALTH_THRESHOLD) {
            energyLoss = GameBalance.SICKLY_ENERGY_LOSS;
            message = "⚡ Energy decreased by " + energyLoss + " due to poor health.";
        }

        user.loseEnergy(energyLoss);

        if (user.getEnergy() == 0) {
            blockUser(user, "Energy reached 0");
        }
        return message;
    }
    public String processHealthDecrease(User user) {
        int healthLoss = GameBalance.BASE_HEALTH_LOSS;
        String message = null;

        if (user.getEnergy() < GameBalance.LOW_ENERGY_THRESHOLD) {
            healthLoss = GameBalance.EXHAUSTED_HEALTH_LOSS;
            message = "❤️‍🩹 Health decreased by " + healthLoss + " due to extreme fatigue!";
        }

        user.loseHealth(healthLoss);

        if (user.getHealth() == 0) {
            blockUser(user, "Health reached 0");
        }
        return message;
    }


    // Blocks the player briefly with a small money penalty.
    // 10-hour run tuning: 5 minutes (was 48 hours), penalty 200 (was 2000),
    // so hitting 0 energy/health is a setback, not a run-ender.
    private void blockUser(User user, String reason) {
        int penalty = GameBalance.KNOCKOUT_MONEY_PENALTY;
        // Same refusal rule as UserController.deductBalance: no partial
        // charge when the balance is short (withdraw alone would clamp).
        if (penalty > 0 && user.getBalance() >= penalty) {
            user.withdraw(penalty);
        }
        user.blockUntil(clock.get() + GameBalance.KNOCKOUT_BLOCK_MILLIS);
        // The service does not show dialogs. It just changes the state.
    }
}
