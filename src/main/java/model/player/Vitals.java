package model.player;

import java.util.Objects;

// Tracks health and energy and keeps them within limits.
public class Vitals {
    public static final int MAX_HEALTH = 100;
    public static final int MAX_ENERGY = 100000;

    private int health = 100;
    private int energy = 100000;

    public int getHealth() {
        return health;
    }

    // Keeps the value between 0 and the max.
    // Private: game logic must use gain/lose/restore so intent is explicit.
    private void setHealth(int newHealth) {
        if (newHealth < 0) {
            newHealth = 0;
        }
        if (newHealth > MAX_HEALTH) {
            newHealth = MAX_HEALTH;
        }
        this.health = newHealth;
    }

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

    public int getEnergy() {
        return energy;
    }

    // Keeps the value between 0 and the max.
    // Private: game logic must use gain/lose/restore so intent is explicit.
    private void setEnergy(int newEnergy) {
        if (newEnergy < 0) {
            newEnergy = 0;
        }
        if (newEnergy > MAX_ENERGY) {
            newEnergy = MAX_ENERGY;
        }
        this.energy = newEnergy;
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

    // Refills both to full after a level up.
    public void restoreFull() {
        setHealth(MAX_HEALTH);
        setEnergy(MAX_ENERGY);
    }

    // Value equality: two vitals match when health and energy match.
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

    // Demo/debug only: lets the Developer panel set exact values.
    // Game logic must use gainHealth/loseHealth/gainEnergy/loseEnergy/restoreFull.
    public void debugSetHealth(int newHealth) {
        setHealth(newHealth);
    }

    public void debugSetEnergy(int newEnergy) {
        setEnergy(newEnergy);
    }
}
