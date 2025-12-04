package services.upkeep;

import java.util.Random;
import java.util.function.Supplier;

import model.player.UpkeepTarget;
import utils.GameBalance;
import utils.Require;

// Rolls the hourly sickness check and applies the health loss.
public final class SicknessRule {
    private final Random random;
    private final Supplier<Long> clock;

    public SicknessRule(Random random, Supplier<Long> clock) {
        this.random = Require.notNull(random, "random");
        this.clock = Require.notNull(clock, "clock");
    }

    // Returns a message when sickness hits; null when nothing happens.
    public String applyTo(UpkeepTarget player) {
        long now = clock.get();
        if (now - player.getLastSicknessTime() < GameBalance.SICKNESS_COOLDOWN_MILLIS) {
            return null;
        }
        // 10% chance to get sick.
        if (random.nextInt(GameBalance.SICKNESS_ROLL_BOUND) < GameBalance.SICKNESS_ROLL_HIT_BELOW) {
            int healthLoss = GameBalance.SICKNESS_MIN_HEALTH_LOSS
                    + random.nextInt(GameBalance.SICKNESS_HEALTH_LOSS_RANGE);
            player.loseHealth(healthLoss);
            player.markSicknessChecked(now);
            return "🤒 You got sick and lost " + healthLoss + " health!";
        }
        player.markSicknessChecked(now);
        return null;
    }
}
