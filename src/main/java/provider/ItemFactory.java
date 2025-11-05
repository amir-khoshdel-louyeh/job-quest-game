package provider;

import model.Food;
import model.Item;
import model.Service;
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

    /**
     * Convert an instant-use {@link Service} into a storable {@link Item}.
     * Energy-heavy services become Food, health-heavy become Tools —
     * polymorphic bridge between the two catalogs without merging hierarchies.
     */
    public static Item fromService(Service service) {
        if (service == null) {
            throw new IllegalArgumentException("service must not be null");
        }
        String desc = service.getDescription() + " (storable)";
        if (service.getHealthEffect() >= service.getEnergyEffect() / 1000) {
            return new Tool(service.getName(), desc, service.getCost(),
                    service.getEnergyEffect(), service.getHealthEffect());
        }
        return new Food(service.getName(), desc, service.getCost(),
                service.getEnergyEffect(), service.getHealthEffect());
    }
}
