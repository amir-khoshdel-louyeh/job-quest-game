package provider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import model.Item;

// This class stores all shop items in one place.
public class ShopItemProvider {

    // map name -> item
    private static HashMap<String, Item> ITEMS_BY_NAME = new HashMap<String, Item>();

    static {
        addItem(ItemFactory.createFood("Basic Food", "A simple meal. Storable — use from inventory.", 50, 4000, 0));
        addItem(ItemFactory.createFood("Energy Drink", "Quick boost. Storable — use from inventory.", 120, 10000, 0));
        addItem(ItemFactory.createTool("First-Aid Kit", "Reusable tool — stays in inventory, use any time.", 100, 0, 20));
        addItem(ItemFactory.createFood("Trail Mix", "Hiker's snack. Storable — use from inventory.", 30, 2500, 0));
        addItem(ItemFactory.createTool("Vitamin Pack", "Reusable tool — stays in inventory, use any time.", 80, 1000, 5));
    }

    // Add item to map
    private static void addItem(Item item) {
        ITEMS_BY_NAME.put(item.getName(), item);
    }

    // Return all items (copy, uses for loop, no unmodifiableList)
    public static List<Item> getAvailableItems() {
        ArrayList<Item> result = new ArrayList<Item>();
        ArrayList<Item> all = new ArrayList<Item>(ITEMS_BY_NAME.values());
        for (int i = 0; i < all.size(); i++) {
            result.add(all.get(i));
        }
        return result;
    }

    // Find item by name
    public static Item getItemByName(String name) {
        return ITEMS_BY_NAME.get(name);
    }
}
