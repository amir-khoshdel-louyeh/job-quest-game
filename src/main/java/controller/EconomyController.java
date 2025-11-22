package controller;

import model.store.Service;
import provider.ServiceProvider;

// Handles money: buying services like food or hospital visits.
// Owns its ServiceProvider (injected, DIP): no static catalog lookup.
public class EconomyController {
    private UserController userController;
    private ServiceProvider serviceProvider;

    public EconomyController(UserController userController) {
        this(userController, new ServiceProvider());
    }

    // Injection seam: catalog comes from outside (testable, swappable).
    public EconomyController(UserController userController, ServiceProvider serviceProvider) {
        if (userController == null) {
            throw new IllegalArgumentException("userController must not be null");
        }
        this.userController = userController;
        if (serviceProvider != null) {
            this.serviceProvider = serviceProvider;
        } else {
            this.serviceProvider = new ServiceProvider();
        }
    }

    // Buys a service with an instant effect (for example: eat food).
    // One atomic transaction: lookup + charge + effect happen here together,
    // so callers can never deduct without applying (or vice versa).
    // Returns the purchased service, or null when unknown/unaffordable.
    public Service purchaseService(String serviceName) {
        Service service = serviceProvider.getService(serviceName);
        if (service == null) {
            return null;
        }
        if (userController.deductBalance(service.getPrice())) {
            service.apply(userController.getUser());
            // Single coherent notification (refresh blasts once).
            userController.refresh();
            return service;
        }
        return null;
    }

    // Catalog queries for views: NavBar asks here via GameController,
    // never the provider directly (Demeter).
    public java.util.List<Service> getHealthServices() {
        return serviceProvider.getHealthServices();
    }

    public java.util.List<Service> getEnergyServices() {
        return serviceProvider.getEnergyServices();
    }
}
