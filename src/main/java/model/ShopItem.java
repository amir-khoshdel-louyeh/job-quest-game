package model;

/**
 * Represents an item available for purchase in the shop.
 * This acts as a data transfer object for the shop UI.
 */
public class ShopItem {
    private final String name;
    private final String description;
    private final int price;

    public ShopItem(String name, String description, int price) {
        this.name = name;
        this.description = description;
        this.price = price;
    }

    public String getName() { return name; }
    public String getDescription() { return description; }
    public int getPrice() { return price; }
}