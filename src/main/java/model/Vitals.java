package model;

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
    public void setHealth(int newHealth) {
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
    public void setEnergy(int newEnergy) {
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
}
