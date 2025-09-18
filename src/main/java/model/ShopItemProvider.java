package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

/**
 * Provides a centralized list of all items available in the shop.
 */
public class ShopItemProvider {

    private static final Map<String, Item> ITEMS_BY_NAME = new HashMap<>();

    static {
        // To add a new item to the shop, just add a new line here.
        addItem(new Food("Basic Food", "Restores a small amount of energy.", 50));
        addItem(new Food("Energy Drink", "Restores a medium amount of energy.", 120));
        addItem(new Tool("First-Aid Kit", "A basic kit to restore some health.", 100));
    }

    private static void addItem(Item item) {
        ITEMS_BY_NAME.put(item.getName(), item);
    }

    public static List<Item> getAvailableItems() {
        return Collections.unmodifiableList(new ArrayList<>(ITEMS_BY_NAME.values()));
    }

    /**
     * Retrieves a fully-formed Item object by its unique name.
     * @param name The name of the item.
     * @return The Item object, or null if not found.
     */
    public static Item getItemByName(String name) {
        return ITEMS_BY_NAME.get(name);
    }
}