package model;

/**
 * Shared base for inventory items with energy/health effects.
 *
 * <p>OOP Step 2: holds the effect state + {@link #apply} implementation once
 * (DRY). Lifecycle differs by subclass via polymorphic
 * {@link Item#isConsumable()}: {@link Food} returns {@code true} (one use),
 * {@link Tool} returns {@code false} (reusable). The name is historical —
 * think "effect item".</p>
 */
public abstract class ConsumableItem extends Item {
    private final int energyEffect;
    private final int healthEffect;

    protected ConsumableItem(String name, String description, int price,
                             int energyEffect, int healthEffect) {
        super(name, description, price);
        if (energyEffect < 0 || healthEffect < 0) {
            throw new IllegalArgumentException("Item effects must be >= 0");
        }
        this.energyEffect = energyEffect;
        this.healthEffect = healthEffect;
    }

    public int getEnergyEffect() { return energyEffect; }
    public int getHealthEffect() { return healthEffect; }

    @Override
    public void apply(User user) {
        // polymorphic effect: restore energy and/or health
        if (user == null) return;
        if (energyEffect > 0) user.gainEnergy(energyEffect);
        if (healthEffect > 0) user.gainHealth(healthEffect);
    }

    @Override
    public String getEffectDescription() {
        if (energyEffect == 0 && healthEffect == 0) return "No effect";
        StringBuilder sb = new StringBuilder();
        if (energyEffect > 0) sb.append("+").append(String.format("%,d", energyEffect)).append(" Energy");
        if (healthEffect > 0) {
            if (sb.length() > 0) sb.append(", ");
            sb.append("+").append(healthEffect).append(" Health");
        }
        return sb.toString();
    }
}
