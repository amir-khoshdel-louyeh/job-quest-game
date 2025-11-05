package model;

/**
 * Model representing a tool that can be used during jobs or tasks.
 *
 * <p>OOP <b>Inheritance + Polymorphism</b>: same {@link Item} contract as
 * {@link Food}, different flavor (typically health/repair focused).
 * Immutable, consumable by default.</p>
 */

public class Tool extends Item {
    private final int energyEffect;
    private final int healthEffect;

    public Tool(String name, String description, int price, int energyEffect, int healthEffect) {
        // construct a Tool item with effects (use 0 for no effect)
        super(name, description, price);
        if (energyEffect < 0 || healthEffect < 0) {
            throw new IllegalArgumentException("Tool effects must be >= 0");
        }
        this.energyEffect = energyEffect;
        this.healthEffect = healthEffect;
    }

    /** Legacy 3-arg constructor: effect-less tool (kept for compatibility). */
    public Tool(String name, String description, int price) {
        this(name, description, price, 0, 0);
    }

    public int getEnergyEffect() { return energyEffect; }
    public int getHealthEffect() { return healthEffect; }

    @Override
    public void apply(User user) {
        // polymorphic effect: tool restores health and/or energy
        if (user == null) return;
        if (energyEffect > 0) user.gainEnergy(energyEffect);
        if (healthEffect > 0) user.gainHealth(healthEffect);
    }

    @Override
    public String getEffectDescription() {
        if (energyEffect == 0 && healthEffect == 0) return "No effect";
        StringBuilder sb = new StringBuilder();
        if (healthEffect > 0) sb.append("+").append(healthEffect).append(" Health");
        if (energyEffect > 0) {
            if (sb.length() > 0) sb.append(", ");
            sb.append("+").append(String.format("%,d", energyEffect)).append(" Energy");
        }
        return sb.toString();
    }
}
