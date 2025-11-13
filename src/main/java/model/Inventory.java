package model;

/**
 * Inventory model holding a user's items and inventory management logic.
 *
 * Follows Single Responsibility Principle by managing only inventory data and helper operations.
 */

import java.util.ArrayList;
import java.util.List;

// This class stores all items of the player.
// It shows Encapsulation: items list is private.
public class Inventory {
    // list of items in inventory
    private ArrayList<Item> items = new ArrayList<Item>();

    // Add one item to inventory
    public void addItem(Item item) {
        if (item == null) {
            return;
        }
        items.add(item);
    }

    // Remove one item from inventory.
    // Returns true if item was removed.
    public boolean removeItem(Item item) {
        return items.remove(item);
    }

    // Use one item by name.
    // This method shows Polymorphism: item.apply(user) calls
    // the correct apply method of Food or Tool.
    public String useItem(User user, String itemName) {
        if (user == null || itemName == null) {
            return null;
        }
        // make a copy to avoid problems while removing
        ArrayList<Item> copy = new ArrayList<Item>(items);
        for (int i = 0; i < copy.size(); i++) {
            Item item = copy.get(i);
            if (item.getName().equals(itemName)) {
                // apply effect of item on user
                item.apply(user);
                String effect = item.getEffectDescription();
                // if item is food (consumable), remove it after use
                // if it is a tool, keep it
                if (item.isConsumable()) {
                    items.remove(item);
                }
                return effect;
            }
        }
        return null;
    }

    // Count how many items with this name we have.
    // Uses simple for loop instead of stream.
    public int countByName(String itemName) {
        if (itemName == null) {
            return 0;
        }
        int count = 0;
        for (int i = 0; i < items.size(); i++) {
            Item item = items.get(i);
            if (item.getName().equals(itemName)) {
                count++;
            }
        }
        return count;
    }

    // Replace all items in inventory
    // Uses simple for loop, no addAll
    public void setItems(List<Item> newItems) {
        items.clear();
        if (newItems != null) {
            for (int i = 0; i < newItems.size(); i++) {
                items.add(newItems.get(i));
            }
        }
    }

    // Return all items (a copy so outside code cannot break our list)
    public List<Item> getItems() {
        ArrayList<Item> copy = new ArrayList<Item>();
        for (int i = 0; i < items.size(); i++) {
            copy.add(items.get(i));
        }
        return copy;
    }

    // Return only names of items. Useful for saving to database.
    // Uses simple for loop.
    public List<String> getItemNames() {
        ArrayList<String> names = new ArrayList<String>();
        for (int i = 0; i < items.size(); i++) {
            names.add(items.get(i).getName());
        }
        return names;
    }
}
