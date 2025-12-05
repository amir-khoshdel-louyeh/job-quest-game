package model.player;

import java.util.function.Supplier;

import utils.Require;

// Tracks whether the player is blocked from doing jobs.
// Used by model.player.Player (sole owner); read via Player/PlayerController.
public final class BlockStatus {
    private final Supplier<Long> clock;
    private long blockedUntil = 0;
    private long lastSicknessTime = 0;

    // ----- Construction -----
    BlockStatus(Supplier<Long> clock) {
        this.clock = Require.notNull(clock, "clock");
    }

    // ----- Setter -----
    private void setBlockedUntil(long time) {
        if (time <= 0) {
            time = 0;
        }
        this.blockedUntil = time;
    }

    private void setLastSicknessTime(long time) {
        if (time <= 0) {
            return;
        }
        this.lastSicknessTime = time;
    }

    // ----- Getter -----
    public long getBlockedUntil() {
        return blockedUntil;
    }

    public long getLastSicknessTime() {
        return lastSicknessTime;
    }

    // ----- Commands -----
    public void blockUntil(long time) {
        setBlockedUntil(time);
    }

    public void markSicknessChecked(long time) {
        setLastSicknessTime(time);
    }

    // ----- Queries -----
    public boolean isBlocked() {
        return clock.get() < blockedUntil;
    }
}
