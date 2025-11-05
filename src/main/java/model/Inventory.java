package model;

/**
 * Inventory model holding a user's items and inventory management logic.
 *
 * Follows Single Responsibility Principle by managing only inventory data and helper operations.
 */

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Inventory {
    private final List<Item> items = new ArrayList<>();

    public void addItem(Item item) {
        // add an item to inventory
        if (item == null) return;
        items.add(item);
    }

    /**
     * Remove one item instance from inventory.
     * @return true if an item was removed
     */
    public boolean removeItem(Item item) {
        return items.remove(item);
    }

    /**
     * Use one item by name: polymorphic {@link Item#apply(User)} does the
     * work, no {@code instanceof} needed. Consumables are removed after use.
     *
     * @param user owner receiving the effect
     * @param itemName exact item name
     * @return effect description, or null when not found
     */
    public String useItem(User user, String itemName) {
        if (user == null || itemName == null) return null;
        for (Item item : new java.util.ArrayList<>(items)) {
            if (item.getName().equals(itemName)) {
                item.apply(user);
                String effect = item.getEffectDescription();
                if (item.isConsumable()) {
                    items.remove(item);
                }
                return effect;
            }
        }
        return null;
    }

    /** Count items with the given name. */
    public int countByName(String itemName) {
        if (itemName == null) return 0;
        return (int) items.stream().filter(i -> i.getName().equals(itemName)).count();
    }

    public void setItems(List<Item> items) {
        // replace inventory contents
        this.items.clear();
        this.items.addAll(items);
    }

    /**
     * Returns an unmodifiable list of items in the inventory.
     */
    public List<Item> getItems() {
        // return an unmodifiable view of items
        return Collections.unmodifiableList(items);
    }

    // return list of item names (CSV helper)
    public List<String> getItemNames() { // Keep for compatibility if needed, e.g., for DB saving
        return items.stream().map(Item::getName).collect(Collectors.toList());
    }
}
