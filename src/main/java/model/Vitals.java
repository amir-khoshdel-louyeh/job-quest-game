package model;

/**
 * OOP Step 6: health/energy extracted from the {@link User} god-object.
 *
 * <p>Single Responsibility: clamping and gain/lose rules for the two
 * survival stats. Health is 0–100, energy 0–100,000.</p>
 */
public class Vitals {
    public static final int MAX_HEALTH = 100;
    public static final int MAX_ENERGY = 100_000;

    private int health = MAX_HEALTH;
    private int energy = MAX_ENERGY;

    public int getHealth() { return health; }

    public void setHealth(int health) {
        this.health = Math.max(0, Math.min(MAX_HEALTH, health));
    }

    public void gainHealth(int amount) {
        if (amount > 0) setHealth(this.health + amount);
    }

    public void loseHealth(int amount) {
        if (amount > 0) setHealth(this.health - amount);
    }

    public int getEnergy() { return energy; }

    public void setEnergy(int energy) {
        this.energy = Math.max(0, Math.min(MAX_ENERGY, energy));
    }

    public void gainEnergy(int amount) {
        if (amount > 0) setEnergy(this.energy + amount);
    }

    public void loseEnergy(int amount) {
        if (amount > 0) setEnergy(this.energy - amount);
    }

    /** Full restore, e.g. on level-up. Coordinated by User, not by progress. */
    public void restoreFull() {
        setHealth(MAX_HEALTH);
        setEnergy(MAX_ENERGY);
    }
}
