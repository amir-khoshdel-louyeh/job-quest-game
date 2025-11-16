package model.store;

// Small interface so model.store never imports model.player.User.
// User implements this; Service applies only health/energy (DIP).
public interface ServiceTarget {
    void gainHealth(int amount);
    void gainEnergy(int amount);
}
