package services;

import java.util.Random;
import java.util.function.Supplier;

import model.player.User;
import utils.GameBalance;

// Handles periodic sickness, energy, and health drops.
// Clock/random come from outside; state changes are silent and the
// caller owns observer notifications.
public class RoutineService {

    private final Random random;
    private final Supplier<Long> clock;

    // ----- Construction -----
    public RoutineService() {
        this(new Random(), System::currentTimeMillis);
    }

    // Fixed clock + seeded random for tests.
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

    // ----- Commands -----
    // Rolls sickness; at most once per cooldown hour.
    public String processSicknessCheck(User user) {
        long now = clock.get();
        if (now - user.getLastSicknessTime() < GameBalance.SICKNESS_COOLDOWN_MILLIS) {
            return null;
        }
        // 10% chance to get sick.
        if (random.nextInt(GameBalance.SICKNESS_ROLL_BOUND) < GameBalance.SICKNESS_ROLL_HIT_BELOW) {
            int healthLoss = GameBalance.SICKNESS_MIN_HEALTH_LOSS
                    + random.nextInt(GameBalance.SICKNESS_HEALTH_LOSS_RANGE);
            user.loseHealth(healthLoss);
            user.markSicknessChecked(now);
            return "🤒 You got sick and lost " + healthLoss + " health!";
        }
        user.markSicknessChecked(now);
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


    // ----- Private helpers -----
    // Blocks the player briefly with a small money penalty.
    private void blockUser(User user, String reason) {
        int penalty = GameBalance.KNOCKOUT_MONEY_PENALTY;
        // No partial charge when the balance is short.
        if (penalty > 0 && user.getBalance() >= penalty) {
            user.withdraw(penalty);
        }
        user.blockUntil(clock.get() + GameBalance.KNOCKOUT_BLOCK_MILLIS);
    }
}
