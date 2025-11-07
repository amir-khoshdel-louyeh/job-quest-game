package provider;

import model.Food;
import model.Item;
import model.Tool;

/**
 * Factory for shop/inventory {@link Item}s (Factory Method pattern).
 *
 * <p>Centralizes creation so game balance lives in one place and callers
 * depend on the {@link Item} abstraction (Dependency Inversion), never on
 * {@code new Food/...} details scattered across the codebase.</p>
 */
public final class ItemFactory {

    private ItemFactory() {
        // static factory only
    }

    /** Create a food item with energy/health effects. */
    public static Food createFood(String name, String description, int price,
                                  int energyEffect, int healthEffect) {
        return new Food(name, description, price, energyEffect, healthEffect);
    }

    /** Create a tool item with energy/health effects. */
    public static Tool createTool(String name, String description, int price,
                                   int energyEffect, int healthEffect) {
        return new Tool(name, description, price, energyEffect, healthEffect);
    }
}
