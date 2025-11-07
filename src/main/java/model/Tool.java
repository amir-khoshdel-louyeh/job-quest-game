package model;

/**
 * Model representing a tool that can be used during jobs or tasks.
 *
 * <p>OOP <b>Inheritance</b>: effect state and behavior live in
 * {@link ConsumableItem}; this class is the tool-flavored extension point
 * (typically health/repair focused, durable tools may override
 * {@link #isConsumable()} later). Immutable.</p>
 */

public class Tool extends ConsumableItem {

    public Tool(String name, String description, int price, int energyEffect, int healthEffect) {
        // construct a Tool item with effects (use 0 for no effect)
        super(name, description, price, energyEffect, healthEffect);
    }

    /** Legacy 3-arg constructor: effect-less tool (kept for compatibility). */
    public Tool(String name, String description, int price) {
        this(name, description, price, 0, 0);
    }
}
