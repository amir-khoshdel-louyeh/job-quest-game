package model.player;

import utils.DurationText;

// Tracks how long the player has played.
// Used by model.player.User (sole owner); read via User/UserController.
public final class PlayClock {
    private long totalMillis;

    // ----- Getter -----
    public long getTotalMillis() {
        return totalMillis;
    }

    public long getTotalMinutes() {
        return DurationText.minutesOf(totalMillis);
    }

    // ----- Commands -----
    public void addMillis(long deltaMillis) {
        if (deltaMillis > 0) {
            totalMillis = totalMillis + deltaMillis;
        }
    }

    // ----- DevControls support -----
    // Package-private: only User may call this; the demo port is on User.
    void setMinutesForCheat(long minutes) {
        if (minutes < 0) {
            minutes = 0;
        }
        totalMillis = minutes * DurationText.MILLIS_PER_MINUTE;
    }

    // ----- Object overrides -----
    @Override
    public String toString() {
        return "PlayClock [" + getTotalMinutes() + " min]";
    }
}
