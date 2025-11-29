package model.player;

// Health/energy operations an upkeep rule may use.
public interface VitalsTarget {
    int getHealth();

    int getEnergy();

    boolean loseHealth(int amount);

    boolean loseEnergy(int amount);
}
