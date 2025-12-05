package model.store;

import java.util.Locale;
import java.util.Objects;

import utils.Require;

// A buyable service that restores health or energy right away.
// Applied to a ShopItemTarget; read via ShopCatalog/controller.
public final class ShopItem {
    private final String name;
    private final String description;
    private final int cost;
    private final int healthEffect;
    private final int energyEffect;

    // ----- Construction -----
    // Description is derived from the effects so the catalog cannot drift.
    public ShopItem(String name, int cost, int healthEffect, int energyEffect) {
        this(name, describeEffect(energyEffect, healthEffect),
                cost, healthEffect, energyEffect);
    }

    public ShopItem(String name, String description, int cost, int healthEffect, int energyEffect) {
        if (name == null || name.trim().equals("")) {
            throw new IllegalArgumentException("ShopItem name must not be blank");
        }
        if (cost < 0 || healthEffect < 0 || energyEffect < 0) {
            throw new IllegalArgumentException("ShopItem cost/effects must be >= 0");
        }
        this.name = name;
        if (description == null) {
            this.description = "";
        } else {
            this.description = description;
        }
        this.cost = cost;
        this.healthEffect = healthEffect;
        this.energyEffect = energyEffect;
    }

    // ----- Getter -----
    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getPrice() {
        return cost;
    }

    public int getHealthEffect() {
        return healthEffect;
    }

    public int getEnergyEffect() {
        return energyEffect;
    }

    // ----- Queries -----
    // "+25,000 Energy, +5 Health", or "No effect" for a service with no bars.
    public static String describeEffect(int energyEffect, int healthEffect) {
        if (energyEffect == 0 && healthEffect == 0) {
            return "No effect";
        }
        String result = "";
        if (energyEffect > 0) {
            result = result + "+" + formatAmount(energyEffect) + " Energy";
        }
        if (healthEffect > 0) {
            if (!result.equals("")) {
                result = result + ", ";
            }
            result = result + "+" + formatAmount(healthEffect) + " Health";
        }
        return result;
    }

    private static String formatAmount(int amount) {
        return String.format(Locale.US, "%,d", amount);
    }

    public boolean isHealthItem() {
        return healthEffect > 0 && energyEffect == 0;
    }

    public boolean isEnergyItem() {
        return energyEffect > 0 && healthEffect == 0;
    }

    // ----- Commands -----
    // Applies the stored health and energy amounts.
    public void apply(ShopItemTarget target) {
        Require.notNull(target, "target");
        if (energyEffect > 0) {
            target.gainEnergy(energyEffect);
        }
        if (healthEffect > 0) {
            target.gainHealth(healthEffect);
        }
    }

    // ----- Object overrides -----
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        ShopItem other = (ShopItem) obj;
        return cost == other.cost
                && healthEffect == other.healthEffect
                && energyEffect == other.energyEffect
                && Objects.equals(name, other.name)
                && Objects.equals(description, other.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, cost, healthEffect, energyEffect);
    }

    @Override
    public String toString() {
        return name + " [price $" + cost + "]";
    }
}
