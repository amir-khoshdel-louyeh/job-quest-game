package model.store;

// Health/energy surface a service can affect.
// Implemented by model.player.Player.
public interface ShopItemTarget {
    boolean gainHealth(int amount);
    boolean gainEnergy(int amount);
}
