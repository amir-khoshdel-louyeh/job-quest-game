package model.player;

import java.util.function.Supplier;

// Tracks whether the player is blocked from doing jobs.
// Used by model.player.User (sole owner); read via User/UserController.
// The clock is injected so block checks are testable instead of reading
// System.currentTimeMillis() from inside the model.
public final class BlockStatus {
    private final Supplier<Long> clock;
    private long blockedUntil = 0;
    private long lastSicknessTime = 0;

    // ----- Construction -----
    public BlockStatus() {
        this(System::currentTimeMillis);
    }

    // Fixed clock for tests.
    public BlockStatus(Supplier<Long> clock) {
        this.clock = utils.Require.notNull(clock, "clock");
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

    public void unblock() {
        setBlockedUntil(0);
    }

    public void markSicknessChecked(long time) {
        setLastSicknessTime(time);
    }

    // ----- Queries -----
    public boolean isBlocked() {
        return clock.get() < blockedUntil;
    }
}
