package controller.contract;

import controller.Outcome;

// What GameController needs from the economy module.
// GameController depends on this contract, not on EconomyController.
public interface ShopPurchases {

    Outcome purchaseService(String serviceName);
}
