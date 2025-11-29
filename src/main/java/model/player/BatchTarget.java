package model.player;

// Runs one action as a unit: observer notifications are held until it ends.
public interface BatchTarget {
    void runBatch(Runnable action);
}
