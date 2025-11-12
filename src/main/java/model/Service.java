package model;

/**
 * Represents a purchasable service in the game, like buying food or visiting a doctor.
 *
 * <p>OOP Step 3: implements {@link Purchasable} alongside {@link Item}.
 * Difference is lifecycle, not data: a Service applies instantly on purchase
 * (never stored), an Item goes to inventory for later use. Both share the
 * same price/effect contract, so shop UIs and {@link controller.EconomyController}
 * treat them polymorphically instead of via parallel code paths. Immutable.</p>
 */
public class Service implements Purchasable {
    private final String name;
    private final String description;
    private final int cost;
    private final int healthEffect;
    private final int energyEffect;

    public Service(String name, String description, int cost, int healthEffect, int energyEffect) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Service name must not be blank");
        }
        if (cost < 0 || healthEffect < 0 || energyEffect < 0) {
            throw new IllegalArgumentException("Service cost/effects must be >= 0");
        }
        this.name = name;
        this.description = description == null ? "" : description;
        this.cost = cost;
        this.healthEffect = healthEffect;
        this.energyEffect = energyEffect;
    }
    @Override
    public String getName() { return name; }
    @Override
    public String getDescription() { return description; }
    /** Price contract: delegates to cost (kept getCost for DB/UI compat). */
    @Override
    public int getPrice() { return cost; }
    public int getCost() { return cost; }
    public int getHealthEffect() { return healthEffect; }
    public int getEnergyEffect() { return energyEffect; }

    /**
     * Instant effect: same rule as items (restore energy/health),
     * applied immediately on purchase by EconomyController.
     */
    @Override
    public void apply(User user) {
        if (user == null) return;
        if (energyEffect > 0) user.gainEnergy(energyEffect);
        if (healthEffect > 0) user.gainHealth(healthEffect);
    }

    @Override
    public String getEffectDescription() {
        return formatEffect(energyEffect, healthEffect);
    }
}