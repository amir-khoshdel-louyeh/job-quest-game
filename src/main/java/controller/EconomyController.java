package controller;

import model.store.Service;
import provider.ServiceProvider;

// Handles money: buying services like food or hospital visits.
// Catalog is injected.
public class EconomyController {
    private UserController userController;
    private ServiceProvider serviceProvider;

    // ----- Construction -----
    public EconomyController(UserController userController) {
        this(userController, new ServiceProvider());
    }

    // Custom catalog for tests.
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

    // ----- Commands -----
    // Buys a service; lookup + charge + effect happen together.
    // Returns the service, or null when unknown/unaffordable.
    public Service purchaseService(String serviceName) {
        Service service = serviceProvider.getService(serviceName);
        if (service == null) {
            return null;
        }
        if (userController.deductBalance(service.getPrice())) {
            service.apply(userController.getUser());
            userController.refresh();
            return service;
        }
        return null;
    }

    // ----- Catalog queries -----
    public java.util.List<Service> getHealthServices() {
        return serviceProvider.getHealthServices();
    }

    public java.util.List<Service> getEnergyServices() {
        return serviceProvider.getEnergyServices();
    }
}
