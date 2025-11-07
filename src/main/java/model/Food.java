package model;

/**
 * Model representing a food item that a player can consume to gain effects.
 *
 * <p>OOP <b>Inheritance</b>: effect state and behavior live in
 * {@link ConsumableItem}; this class is the food-flavored extension point.
 * Immutable.</p>
 */

public class Food extends ConsumableItem {

    public Food(String name, String description, int price, int energyEffect, int healthEffect) {
        // construct a Food item with effects (use 0 for no effect)
        super(name, description, price, energyEffect, healthEffect);
    }

    /** Legacy 3-arg constructor: effect-less food (kept for compatibility, Open/Closed). */
    public Food(String name, String description, int price) {
        this(name, description, price, 0, 0);
    }
}
