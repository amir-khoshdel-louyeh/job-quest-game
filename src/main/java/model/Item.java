package model;

/**
 * Base model for items that can appear in the shop or a player's inventory.
 *
 * <p>OOP <b>Abstraction + Inheritance + Polymorphism</b>: concrete items
 * ({@link Food}, {@link Tool}) encapsulate their own effects and apply
 * themselves via {@link #apply(User)} — callers never use
 * {@code instanceof} checks. Immutable (all fields final) so shared
 * catalog instances are safe.</p>
 *
 * <p>Follows Single Responsibility Principle by containing only item data
 * and its own use-behavior.</p>
 */

public abstract class Item {
    private final String name;
    private final String description;
    private final int price;

    public Item(String name, String description, int price) {
        // construct an item with name, description and price
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Item name must not be blank");
        }
        if (price < 0) {
            throw new IllegalArgumentException("Item price must be >= 0");
        }
        this.name = name;
        this.description = description == null ? "" : description;
        this.price = price;
    }

    // return item name
    public String getName() { return name; }
    // return item description
    public String getDescription() { return description; }
    // return item price
    public int getPrice() { return price; }

    /**
     * Apply this item's effect to the user.
     * Polymorphic: each subclass implements its own effect.
     * @param user target, must not be null
     */
    public abstract void apply(User user);

    /** Human-readable effect line for shop tooltips and messages. */
    public abstract String getEffectDescription();

    /**
     * Whether the item is removed from inventory after use.
     * Default consumable; durable tools can override to false (Open/Closed).
     */
    public boolean isConsumable() { return true; }
}
