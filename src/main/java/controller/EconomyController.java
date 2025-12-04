package controller;

import controller.contract.ServicePurchases;
import model.player.PurchaseTarget;
import model.store.Service;
import provider.ServiceCatalog;
import utils.Require;

// Handles money: buying services like food or hospital visits.
// Catalog is injected. Package-private: only GameSession/GameController wire it.
final class EconomyController implements ServicePurchases {
    private final PurchaseTarget player;
    private final ServiceCatalog serviceProvider;

    // ----- Construction -----
    // Catalog is always supplied by the composition root; no hidden default.
    EconomyController(PurchaseTarget player, ServiceCatalog serviceProvider) {
        this.player = Require.notNull(player, "player");
        this.serviceProvider = Require.notNull(serviceProvider, "serviceProvider");
    }

    // ----- Commands -----
    // Buys a service; lookup + charge + effect happen together.
    @Override
    public Outcome purchaseService(String serviceName) {
        Service service = serviceProvider.getService(serviceName);
        if (service == null) {
            return Outcome.failure("Unknown service: " + serviceName + ".");
        }
        if (!player.deductBalance(service.getPrice())) {
            return Outcome.failure("Failed to purchase " + serviceName
                    + ". Not enough balance ($" + service.getPrice() + ").");
        }
        // The target applies health/energy effects; the adapter publishes the
        // matching change notifications.
        service.apply(player);
        String effect = Service.describeEffect(
                service.getEnergyEffect(), service.getHealthEffect());
        return Outcome.success("Service " + serviceName
                + " purchased. Effect: " + effect + ".");
    }
}
