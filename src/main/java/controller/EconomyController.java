package controller;

import model.store.Service;
import provider.ServiceProvider;

// Handles money: buying services like food or hospital visits.
public class EconomyController {
    private UserController userController;

    public EconomyController(UserController userController) {
        this.userController = userController;
    }

    // Buys a service with an instant effect (for example: eat food).
    public boolean purchaseService(String serviceName) {
        Service service = ServiceProvider.getService(serviceName);
        if (service == null) {
            return false;
        }
        if (userController.deductBalance(service.getPrice())) {
            service.apply(userController.getUser());
            userController.refresh();
            userController.notifyObservers();
            return true;
        }
        return false;
    }
}
