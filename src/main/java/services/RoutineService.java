package services;

import java.util.Random;
import java.util.function.Supplier;

import model.player.UpkeepTarget;
import services.upkeep.DrainRule;
import services.upkeep.KnockoutPolicy;
import services.upkeep.SicknessRule;

// Facade for periodic upkeep. Each rule lives in services.upkeep.
// Clock/random come from outside; state changes are silent and the
// caller owns observer notifications.
public final class RoutineService {

    private final SicknessRule sickness;
    private final DrainRule drains;

    // ----- Construction -----
    public RoutineService() {
        this(new Random(), System::currentTimeMillis);
    }

    // Fixed clock + seeded random for tests.
    public RoutineService(Random random, Supplier<Long> clock) {
        utils.Require.notNull(random, "random");
        utils.Require.notNull(clock, "clock");
        this.sickness = new SicknessRule(random, clock);
        this.drains = new DrainRule(new KnockoutPolicy(clock));
    }

    // ----- Commands -----
    public String processSicknessCheck(UpkeepTarget player) {
        return sickness.applyTo(player);
    }

    public String processEnergyDecrease(UpkeepTarget player) {
        return drains.energy(player);
    }

    public String processHealthDecrease(UpkeepTarget player) {
        return drains.health(player);
    }
}
