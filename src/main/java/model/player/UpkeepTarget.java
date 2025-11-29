package model.player;

// The surface periodic upkeep needs: vitals, the money for a knockout fine,
// and the sickness/block bookkeeping.
// One port for all three upkeep rules keeps their dependencies explicit.
public interface UpkeepTarget {

    int getHealth();

    int getEnergy();

    int getBalance();

    boolean loseHealth(int amount);

    boolean loseEnergy(int amount);

    boolean withdraw(int amount);

    void blockUntil(long time);

    void markSicknessChecked(long time);

    long getLastSicknessTime();

    // Adds elapsed play time; the upkeep scheduler calls this once per minute.
    void addPlayTime(long millis);
}
