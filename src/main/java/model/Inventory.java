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
        items.add(item);
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
