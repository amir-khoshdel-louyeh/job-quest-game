package model;

/**
 * Model representing a food item that a player can consume to gain effects.
 *
 * <p>OOP <b>Inheritance + Polymorphism</b>: encapsulates energy/health
 * effects and applies them itself. Immutable.</p>
 */

public class Food extends Item {
    private final int energyEffect;
    private final int healthEffect;

    public Food(String name, String description, int price, int energyEffect, int healthEffect) {
        // construct a Food item with effects (use 0 for no effect)
        super(name, description, price);
        if (energyEffect < 0 || healthEffect < 0) {
            throw new IllegalArgumentException("Food effects must be >= 0");
        }
        this.energyEffect = energyEffect;
        this.healthEffect = healthEffect;
    }

    /** Legacy 3-arg constructor: effect-less food (kept for compatibility, Open/Closed). */
    public Food(String name, String description, int price) {
        this(name, description, price, 0, 0);
    }

    public int getEnergyEffect() { return energyEffect; }
    public int getHealthEffect() { return healthEffect; }

    @Override
    public void apply(User user) {
        // polymorphic effect: food restores energy and/or health
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
