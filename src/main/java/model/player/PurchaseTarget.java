package model.player;

import model.store.ServiceTarget;

// Exactly what a purchase needs: charge the wallet, then apply the effect.
public interface PurchaseTarget extends ServiceTarget {
    boolean deductBalance(int amount);
}
