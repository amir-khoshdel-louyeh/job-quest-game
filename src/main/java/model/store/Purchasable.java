package model.store;

// Small interface so model.store never imports model.player.User.

// Something the player can buy in the shop.
// Depends on ServiceTarget, not on player.User (DIP).
public interface Purchasable {
    String getName();
    String getDescription();
    int getPrice();
    void apply(ServiceTarget target);
}
