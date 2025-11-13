package controller;

import model.Service;
import provider.ServiceProvider;

// This class handles money: buying services (Hospital/Market instant use).
public class EconomyController {
    private UserController userController;

    // Constructor
    public EconomyController(UserController userController) {
        this.userController = userController;
    }

    // Buy a service (instant effect, example: eat food)
    // Uses Polymorphism: service.apply(user) does the work.
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
