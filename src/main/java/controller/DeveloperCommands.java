package controller;

// Demo controls a screen can request.
// The developer dialog depends on this contract, not on DeveloperController.
public interface DeveloperCommands {

    int getHealth();

    int getEnergy();

    int getLevel();

    int getReputation();

    long getPlayTimeMinutes();

    int getJobBonus();

    int getBalance();

    void setBalance(int amount);

    void setHealth(int health);

    void setEnergy(int energy);

    void addPlaytimeMinutes(long minutes);

    void setPlaytimeMinutes(long minutes);

    void setLevel(int level);

    void setReputation(int reputation);

    void setJobBonus(int jobBonus);

    void unblock();
}
