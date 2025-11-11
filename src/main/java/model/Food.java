package model;

/**
 * Model representing a consumable food item (e.g. Energy Drink).
 *
 * <p>OOP Step 2: the counterpart to {@link Tool}. Same effect state inherited
 * from {@link ConsumableItem}, but {@link #isConsumable()} stays {@code true},
 * so {@link Inventory#useItem(User, String)} removes one unit per use.
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

    /** Food is single-use: explicit so the Food/Tool contract reads clearly. */
    @Override
    public boolean isConsumable() { return true; }
}
