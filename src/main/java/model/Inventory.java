package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Inventory {
    private final List<Item> items = new ArrayList<>();

    public void addItem(Item item) {
        items.add(item);
    }

    public void setItems(List<Item> items) {
        this.items.clear();
        this.items.addAll(items);
    }

    /**
     * Returns an unmodifiable list of items in the inventory.
     */
    public List<Item> getItems() {
        return Collections.unmodifiableList(items);
    }

    public List<String> getItemNames() { // Keep for compatibility if needed, e.g., for DB saving
        return items.stream().map(Item::getName).collect(Collectors.toList());
    }
}
