package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Inventory {
    // For now, storing item names as strings to match existing usage.
    // In a more complex system, this might store Item objects or a Map<Item, Integer> for quantities.
    private List<String> itemNames = new ArrayList<>();

    public void addItem(String itemName) {
        itemNames.add(itemName);
    }

    /**
     * Returns an unmodifiable list of item names in the inventory.
     */
    public List<String> getItemNames() {
        return Collections.unmodifiableList(itemNames);
    }
}
