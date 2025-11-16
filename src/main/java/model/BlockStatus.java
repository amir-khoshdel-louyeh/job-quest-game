package model;

// Owns wall-clock blocking state. Extracted from User for SRP.
// Time is passed in so the rule is testable without System clock.
public class BlockStatus {
    private long blockedUntil = 0;
    private long lastSicknessTime = 0;

    public void setBlockedUntil(long time) {
        this.blockedUntil = time;
    }

    public long getBlockedUntil() {
        return blockedUntil;
    }

    public void setLastSicknessTime(long time) {
        this.lastSicknessTime = time;
    }

    public long getLastSicknessTime() {
        return lastSicknessTime;
    }

    public boolean isBlockedAt(long now) {
        return now < blockedUntil;
    }

    public boolean isBlocked() {
        return isBlockedAt(System.currentTimeMillis());
    }
}
