package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Provides a centralized list of all items available in the shop.
 */
public class ShopItemProvider {

    private static final List<ShopItem> SHOP_ITEMS = new ArrayList<>();

    static {
        // To add a new item to the shop, just add a new line here.
        SHOP_ITEMS.add(new ShopItem("Basic Food", "Restores a small amount of energy.", 50));
        SHOP_ITEMS.add(new ShopItem("Energy Drink", "Restores a medium amount of energy.", 120));
        SHOP_ITEMS.add(new ShopItem("First-Aid Kit", "A basic kit to restore some health.", 100));
    }

    public static List<ShopItem> getAvailableItems() {
        return Collections.unmodifiableList(SHOP_ITEMS);
    }
}