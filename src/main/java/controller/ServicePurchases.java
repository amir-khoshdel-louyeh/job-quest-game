package controller;

import model.store.Service;

// What GameController needs from the economy module.
// GameController depends on this contract, not on EconomyController.
public interface ServicePurchases {

    Outcome<Service> purchaseService(String serviceName);
}
