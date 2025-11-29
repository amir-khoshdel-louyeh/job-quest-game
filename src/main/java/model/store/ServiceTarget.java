package model.store;

// Health/energy surface a service can affect.
// Implemented by model.player.User.
public interface ServiceTarget {
    boolean gainHealth(int amount);
    boolean gainEnergy(int amount);
}
