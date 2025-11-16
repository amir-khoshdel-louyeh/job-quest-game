package model.store;

import model.player.User;

// Something the player can buy in the shop.
public interface Purchasable {
    String getName();
    String getDescription();
    int getPrice();
    void apply(User user);
    String getEffectDescription();
}
