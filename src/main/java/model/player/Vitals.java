package model.player;

import java.util.Objects;

// Tracks health and energy and keeps them within limits.
// Used by model.player.User (sole owner); read via User/UserController.

public class Vitals {
    public static final int MAX_HEALTH = 100;
    public static final int MAX_ENERGY = 100000;

    private int health = 100;
    private int energy = 100000;

    // ----- Getter -----
    public int getHealth() {
        return health;
    }

    public int getEnergy() {
        return energy;
    }

    // ----- Setter -----
    private void setHealth(int newHealth) {
        if (newHealth < 0) {
            newHealth = 0;
        }
        if (newHealth > MAX_HEALTH) {
            newHealth = MAX_HEALTH;
        }
        this.health = newHealth;
    }

    private void setEnergy(int newEnergy) {
        if (newEnergy < 0) {
            newEnergy = 0;
        }
        if (newEnergy > MAX_ENERGY) {
            newEnergy = MAX_ENERGY;
        }
        this.energy = newEnergy;
    }

    // ----- Commands -----
    public void gainHealth(int amount) {
        if (amount > 0) {
            setHealth(health + amount);
        }
    }

    public void loseHealth(int amount) {
        if (amount > 0) {
            setHealth(health - amount);
        }
    }

    public void gainEnergy(int amount) {
        if (amount > 0) {
            setEnergy(energy + amount);
        }
    }

    public void loseEnergy(int amount) {
        if (amount > 0) {
            setEnergy(energy - amount);
        }
    }

    public void restoreFull() {
        setHealth(MAX_HEALTH);
        setEnergy(MAX_ENERGY);
    }

    // ----- Cheat harness -----
    // Package-private: only User/CheatService (same package) may call these.
    void setHealthForCheat(int newHealth) {
        setHealth(newHealth);
    }

    void setEnergyForCheat(int newEnergy) {
        setEnergy(newEnergy);
    }

    // ----- Object overrides -----
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Vitals other = (Vitals) obj;
        return health == other.health && energy == other.energy;
    }

    @Override
    public int hashCode() {
        return Objects.hash(health, energy);
    }

    @Override
    public String toString() {
        return "Vitals [health " + health + "/" + MAX_HEALTH
                + ", energy " + energy + "/" + MAX_ENERGY + "]";
    }
}
