package model;

/**
 * Base model for items that can appear in the shop or a player's inventory.
 *
 * Follows Single Responsibility Principle by containing only item-related data.
 */

public abstract class Item {
    protected String name;
    protected String description;
    protected int price;

    public Item(String name, String description, int price) {
        // construct an item with name, description and price
        this.name = name;
        this.description = description;
        this.price = price;
    }

    // return item name
    public String getName() { return name; }
    // return item description
    public String getDescription() { return description; }
    // return item price
    public int getPrice() { return price; }
}
