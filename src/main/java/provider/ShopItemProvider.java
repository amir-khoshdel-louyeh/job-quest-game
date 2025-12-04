package provider;

import provider.contract.ShopCatalog;

import java.util.ArrayList;
import java.util.List;

import model.store.ShopItem;
import utils.Lists;

// Holds all food and hospital services the player can buy.
// Extends AbstractCatalog, so name lookup comes from the base template.
// Injected into EconomyController.
public final class ShopItemProvider extends AbstractCatalog<ShopItem> implements ShopCatalog {

    // ----- Construction -----
    public ShopItemProvider() {
        super(buildDefaultCatalog());
    }

    private static List<ShopItem> buildDefaultCatalog() {
        ArrayList<ShopItem> services = new ArrayList<ShopItem>();
        services.add(new ShopItem("Water Bottle", 5, 0, 500));
        services.add(new ShopItem("Apple", 8, 0, 800));
        services.add(new ShopItem("Banana", 10, 0, 1200));
        services.add(new ShopItem("Granola Bar", 15, 0, 1800));
        services.add(new ShopItem("Coffee", 20, 0, 3000));
        services.add(new ShopItem("Energy Bar", 25, 0, 2500));
        services.add(new ShopItem("Soda Can", 30, 0, 3500));
        services.add(new ShopItem("Quick Meal", 50, 0, 5000));
        services.add(new ShopItem("Sandwich", 60, 0, 6000));
        services.add(new ShopItem("Pizza Slice", 85, 0, 8500));
        services.add(new ShopItem("Pasta Bowl", 90, 0, 10000));
        services.add(new ShopItem("Full Meal", 100, 0, 12000));
        services.add(new ShopItem("Protein Shake", 125, 0, 15000));
        services.add(new ShopItem("Basic Check-up", 50, 5, 0));
        services.add(new ShopItem("Stitches", 80, 10, 0));
        services.add(new ShopItem("Antibiotics Course", 120, 15, 0));
        services.add(new ShopItem("X-Ray & Diagnosis", 200, 20, 0));
        services.add(new ShopItem("Minor Surgery", 500, 30, 0));
        services.add(new ShopItem("Physical Therapy", 800, 40, 0));
        services.add(new ShopItem("Advanced Diagnostics", 1200, 50, 0));
        services.add(new ShopItem("Major Surgery", 2500, 65, 0));
        services.add(new ShopItem("Intensive Care", 5000, 80, 0));
        services.add(new ShopItem("Full Recovery Program", 10000, 100, 0));
        services.add(new ShopItem("Gourmet Meal", 200, 5, 25000));
        return services;
    }

    // ----- Template hook -----
    @Override
    protected String nameOf(ShopItem entry) {
        return entry.getName();
    }

    // ----- Queries -----
    // Returns only health services.
    public List<ShopItem> getHealthItems() {
        return Lists.filter(snapshot(), ShopItem::isHealthItem);
    }

    // Returns only energy services.
    public List<ShopItem> getEnergyItems() {
        return Lists.filter(snapshot(), ShopItem::isEnergyItem);
    }
}
