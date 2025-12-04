package controller;

import controller.contract.ShopPurchases;
import model.player.PurchaseTarget;
import model.store.ShopItem;
import provider.contract.ShopCatalog;
import utils.Require;

// Handles money: buying services like food or hospital visits.
// Catalog is injected. Package-private: only GameSession/GameController wire it.
final class EconomyController implements ShopPurchases {
    private final PurchaseTarget player;
    private final ShopCatalog shopCatalog;

    // ----- Construction -----
    // Catalog is always supplied by the composition root; no hidden default.
    EconomyController(PurchaseTarget player, ShopCatalog shopCatalog) {
        this.player = Require.notNull(player, "player");
        this.shopCatalog = Require.notNull(shopCatalog, "shopCatalog");
    }

    // ----- Commands -----
    // Buys a service; lookup + charge + effect happen together.
    @Override
    public Outcome purchaseService(String serviceName) {
        ShopItem service = shopCatalog.findByName(serviceName);
        if (service == null) {
            return Outcome.failure("Unknown item: " + serviceName + ".");
        }
        if (!player.deductBalance(service.getPrice())) {
            return Outcome.failure("Failed to purchase " + serviceName
                    + ". Not enough balance ($" + service.getPrice() + ").");
        }
        // The target applies health/energy effects; the adapter publishes the
        // matching change notifications.
        service.apply(player);
        String effect = ShopItem.describeEffect(
                service.getEnergyEffect(), service.getHealthEffect());
        return Outcome.success("Purchased " + serviceName
                + ". Effect: " + effect + ".");
    }
}
