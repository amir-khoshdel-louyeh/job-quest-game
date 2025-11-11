package model;

/**
 * Model representing a durable, reusable tool (e.g. First-Aid Kit).
 *
 * <p>OOP Step 2: real behavioral difference via <b>Polymorphism</b>.
 * {@link Food} is consumed on use; a {@code Tool} stays in inventory
 * ({@link #isConsumable()} returns {@code false}), so
 * {@link Inventory#useItem(User, String)} applies the effect without removing it.
 * Same {@link ConsumableItem} effect state, opposite lifecycle. Immutable.</p>
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

    /**
     * Tools are durable: using one applies its effect but keeps it.
     * This single override is what makes Food vs Tool a true subclass
     * distinction instead of two identical labels.
     */
    @Override
    public boolean isConsumable() { return false; }
}
