package model;

// This class stores health and energy of the player.
// Health is 0 to 100, energy is 0 to 100000.
public class Vitals {
    // max values, public so other classes can read them
    public static final int MAX_HEALTH = 100;
    public static final int MAX_ENERGY = 100000;

    private int health = 100;
    private int energy = 100000;

    // return health
    public int getHealth() {
        return health;
    }

    // set health, keep it between 0 and 100
    public void setHealth(int newHealth) {
        if (newHealth < 0) {
            newHealth = 0;
        }
        if (newHealth > MAX_HEALTH) {
            newHealth = MAX_HEALTH;
        }
        this.health = newHealth;
    }

    // increase health
    public void gainHealth(int amount) {
        if (amount > 0) {
            setHealth(health + amount);
        }
    }

    // decrease health
    public void loseHealth(int amount) {
        if (amount > 0) {
            setHealth(health - amount);
        }
    }

    // return energy
    public int getEnergy() {
        return energy;
    }

    // set energy, keep it between 0 and max
    public void setEnergy(int newEnergy) {
        if (newEnergy < 0) {
            newEnergy = 0;
        }
        if (newEnergy > MAX_ENERGY) {
            newEnergy = MAX_ENERGY;
        }
        this.energy = newEnergy;
    }

    // increase energy
    public void gainEnergy(int amount) {
        if (amount > 0) {
            setEnergy(energy + amount);
        }
    }

    // decrease energy
    public void loseEnergy(int amount) {
        if (amount > 0) {
            setEnergy(energy - amount);
        }
    }

    // refill both to max, used after level up
    public void restoreFull() {
        setHealth(MAX_HEALTH);
        setEnergy(MAX_ENERGY);
    }
}
