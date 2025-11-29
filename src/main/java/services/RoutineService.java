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
public final class RoutineService implements UpkeepRules {

    private final SicknessRule sickness;
    private final DrainRule drains;

    // ----- Construction -----
    public RoutineService() {
        Supplier<Long> clock = System::currentTimeMillis;
        this.sickness = new SicknessRule(new Random(), clock);
        this.drains = new DrainRule(new KnockoutPolicy(clock));
    }

    // ----- Commands -----
    @Override
    public String processSicknessCheck(UpkeepTarget player) {
        return sickness.applyTo(player);
    }

    @Override
    public String processEnergyDecrease(UpkeepTarget player) {
        return drains.energy(player);
    }

    @Override
    public String processHealthDecrease(UpkeepTarget player) {
        return drains.health(player);
    }
}
