package services.upkeep;

import java.util.Random;
import java.util.function.Supplier;

import model.player.SicknessTarget;
import utils.GameBalance;

// Rolls the hourly sickness check and applies the health loss.
public final class SicknessRule {
    private final Random random;
    private final Supplier<Long> clock;

    public SicknessRule(Random random, Supplier<Long> clock) {
        this.random = utils.Require.notNull(random, "random");
        this.clock = utils.Require.notNull(clock, "clock");
    }

    // Returns a message when sickness hits; null when nothing happens.
    public String applyTo(SicknessTarget user) {
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
}
