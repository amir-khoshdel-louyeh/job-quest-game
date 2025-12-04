package services.upkeep;

import java.util.function.Supplier;

import model.player.UpkeepTarget;
import utils.GameBalance;
import utils.Require;

// Applies the knockout penalty: a small money fine and a temporary job block.
public final class KnockoutPolicy {
    private final Supplier<Long> clock;

    public KnockoutPolicy(Supplier<Long> clock) {
        this.clock = Require.notNull(clock, "clock");
    }

    public void applyTo(UpkeepTarget user) {
        int penalty = GameBalance.KNOCKOUT_MONEY_PENALTY;
        // No partial charge when the balance is short.
        if (penalty > 0 && user.getBalance() >= penalty) {
            user.withdraw(penalty);
        }
        user.blockUntil(clock.get() + GameBalance.KNOCKOUT_BLOCK_MILLIS);
    }
}
