package model;

// A buyable service that restores health or energy right away.
public class Service implements Purchasable {
    private String name;
    private String description;
    private int cost;
    private int healthEffect;
    private int energyEffect;

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

    // Adds the stored health and energy amounts to the player.
    @Override
    public void apply(User user) {
        if (user == null) {
            return;
        }
        if (energyEffect > 0) {
            user.gainEnergy(energyEffect);
        }
        if (healthEffect > 0) {
            user.gainHealth(healthEffect);
        }
    }

    @Override
    public String getEffectDescription() {
        return utils.EffectText.formatServiceEffect(energyEffect, healthEffect);
    }
}
