package controller;

// What GameController needs from the economy module.
// GameController depends on this contract, not on EconomyController.
public interface ServicePurchases {

    Outcome purchaseService(String serviceName);
}
