package model.store;

// Health/energy surface a service can affect.
// Implemented by model.player.User.
public interface ServiceTarget {
    void gainHealth(int amount);
    void gainEnergy(int amount);
}
