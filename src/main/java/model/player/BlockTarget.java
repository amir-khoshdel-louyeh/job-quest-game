package model.player;

// Blocking/sickness bookkeeping an upkeep rule may use.
public interface BlockTarget {
    void blockUntil(long time);

    void markSicknessChecked(long time);

    long getLastSicknessTime();
}
