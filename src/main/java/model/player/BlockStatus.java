package model.player;

// Tracks whether the player is blocked from doing jobs.
// Used by model.player.User (sole owner); read via User/UserController.

public class BlockStatus {
    private long blockedUntil = 0;
    private long lastSicknessTime = 0;

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
    public boolean isBlockedAt(long now) {
        return now < blockedUntil;
    }

    public boolean isBlocked() {
        return isBlockedAt(System.currentTimeMillis());
    }
}
