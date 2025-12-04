package model.player;

import model.store.ShopItemTarget;

// Exactly what a purchase needs: charge the wallet, then apply the effect.
public interface PurchaseTarget extends ShopItemTarget {
    boolean deductBalance(int amount);
}
