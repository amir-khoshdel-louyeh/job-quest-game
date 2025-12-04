package provider.contract;

import java.util.List;

import model.store.ShopItem;

// Read-only shop catalog contract: name lookup plus the two filtered views.
public interface ShopCatalog extends NamedCatalog<ShopItem> {
    List<ShopItem> getHealthItems();

    List<ShopItem> getEnergyItems();
}
