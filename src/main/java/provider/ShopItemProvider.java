package provider;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import model.Item;

/**
 * Provides a centralized list of all items available in the shop.
 *
 * <p>Items carry real effects via {@link ItemFactory} (Factory Method) so
 * the shop is usable — previously effect-less and dead vs Services.
 * Names are stable: {@code UserRepositoryImpl} persists inventory by name.</p>
 */
public class ShopItemProvider {

    private static final Map<String, Item> ITEMS_BY_NAME = new HashMap<>();

    static {
        // To add a new item to the shop, just add a new line here.
        // Effects mirror ServiceProvider value curve (~100 Energy per $1).
        addItem(ItemFactory.createFood("Basic Food", "A simple meal. Storable — use from inventory.", 50, 4000, 0));
        addItem(ItemFactory.createFood("Energy Drink", "Quick boost. Storable — use from inventory.", 120, 10000, 0));
        addItem(ItemFactory.createTool("First-Aid Kit", "Reusable tool — stays in inventory, use any time.", 100, 0, 20));
        addItem(ItemFactory.createFood("Trail Mix", "Hiker's snack. Storable — use from inventory.", 30, 2500, 0));
        addItem(ItemFactory.createTool("Vitamin Pack", "Reusable tool — stays in inventory, use any time.", 80, 1000, 5));
    }

    private static void addItem(Item item) {
        // register an item in the shop catalog
        ITEMS_BY_NAME.put(item.getName(), item);
    }

    public static List<Item> getAvailableItems() {
        return Collections.unmodifiableList(new ArrayList<>(ITEMS_BY_NAME.values()));
    }

    /** Get an item by its name */
    public static Item getItemByName(String name) {
        // lookup item by name
        return ITEMS_BY_NAME.get(name);
    }
}
