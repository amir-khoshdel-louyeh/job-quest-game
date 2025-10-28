package controller;

import model.Item;
import model.Service;
import provider.ServiceProvider;

public class EconomyController {
    private final UserController userController;

    public EconomyController(UserController userController) {
        this.userController = userController;
    }

    public boolean purchaseService(String serviceName) {
        Service service = ServiceProvider.getService(serviceName);
        if (service == null) return false;
        if (userController.deductBalance(service.getCost())) {
            userController.increaseEnergy(service.getEnergyEffect());
            userController.increaseHealth(service.getHealthEffect());
            userController.notifyObservers();
            return true;
        }
        return false;
    }

    public boolean purchaseItem(Item item) {
        if (item == null) return false;
        if (userController.deductBalance(item.getPrice())) {
            userController.addItemToInventory(item);
            userController.notifyObservers();
            return true;
        }
        return false;
    }
}
