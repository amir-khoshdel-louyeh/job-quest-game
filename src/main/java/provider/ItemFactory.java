package provider;

import model.Food;
import model.Item;
import model.Tool;

// This is a simple factory that creates Food and Tool objects.
// It shows Factory idea: all creation is in one place.
public final class ItemFactory {

    // private constructor, nobody creates this class
    private ItemFactory() {
    }

    // Create a food item (removed from inventory after use)
    public static Food createFood(String name, String description, int price,
                                  int energyEffect, int healthEffect) {
        return new Food(name, description, price, energyEffect, healthEffect);
    }

    // Create a tool item (stays in inventory after use)
    public static Tool createTool(String name, String description, int price,
                                   int energyEffect, int healthEffect) {
        return new Tool(name, description, price, energyEffect, healthEffect);
    }
}
