package services.upkeep;

import model.player.UpkeepTarget;
import utils.GameBalance;
import utils.Require;

// Passive energy and health drain, including the knockout check.
public final class DrainRule {
    private final KnockoutPolicy knockout;

    public DrainRule(KnockoutPolicy knockout) {
        this.knockout = Require.notNull(knockout, "knockout");
    }

    // Returns a warning message when poor health increases the drain.
    public String energy(UpkeepTarget player) {
        if (player.getEnergy() <= 0) {
            return null;
        }

        int energyLoss = GameBalance.BASE_ENERGY_LOSS;
        String message = null;

        if (player.getHealth() < GameBalance.LOW_HEALTH_THRESHOLD) {
            energyLoss = GameBalance.SICKLY_ENERGY_LOSS;
            message = "⚡ Energy decreased by " + energyLoss + " due to poor health.";
        }

        player.loseEnergy(energyLoss);

        if (player.getEnergy() == 0) {
            knockout.applyTo(player);
        }
        return message;
    }

    // Returns a warning message when exhaustion increases the drain.
    public String health(UpkeepTarget player) {
        int healthLoss = GameBalance.BASE_HEALTH_LOSS;
        String message = null;

        if (player.getEnergy() < GameBalance.LOW_ENERGY_THRESHOLD) {
            healthLoss = GameBalance.EXHAUSTED_HEALTH_LOSS;
            message = "❤️‍🩹 Health decreased by " + healthLoss + " due to extreme fatigue!";
        }

        player.loseHealth(healthLoss);

        if (player.getHealth() == 0) {
            knockout.applyTo(player);
        }
        return message;
    }
}
