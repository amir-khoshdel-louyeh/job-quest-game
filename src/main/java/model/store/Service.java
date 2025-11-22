package model.store;

import java.util.Objects;

// A buyable service that restores health or energy right away.
// Applies to ServiceTarget, not to User directly (DIP).
// Immutable value object: all fields final.
public final class Service implements Purchasable {
    private final String name;
    private final String description;
    private final int cost;
    private final int healthEffect;
    private final int energyEffect;

    public Service(String name, String description, int cost, int healthEffect, int energyEffect) {
        if (name == null || name.trim().equals("")) {
            throw new IllegalArgumentException("Service name must not be blank");
        }
        if (cost < 0 || healthEffect < 0 || energyEffect < 0) {
            throw new IllegalArgumentException("Service cost/effects must be >= 0");
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

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public int getPrice() {
        return cost;
    }

    public int getHealthEffect() {
        return healthEffect;
    }

    public int getEnergyEffect() {
        return energyEffect;
    }

    // Catalog predicates live here (Information Expert): the provider asks,
    // it never branches on raw effect numbers (no external if-chain).
    public boolean isHealthService() {
        return healthEffect > 0 && energyEffect == 0;
    }

    public boolean isEnergyService() {
        return energyEffect > 0 && healthEffect == 0;
    }

    // Adds the stored health and energy amounts to the player.
    @Override
    public void apply(ServiceTarget target) {
        if (target == null) {
            return;
        }
        if (energyEffect > 0) {
            target.gainEnergy(energyEffect);
        }
        if (healthEffect > 0) {
            target.gainHealth(healthEffect);
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Service other = (Service) obj;
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
