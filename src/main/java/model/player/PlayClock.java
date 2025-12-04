package model.player;

import utils.DurationText;

// Tracks how long the player has played.
// Used by model.player.User (sole owner); read via User/UserController.
public final class PlayClock {
    private long totalMillis;

    // ----- Construction -----
    PlayClock() {
    }

    // ----- Getter -----
    public long getTotalMinutes() {
        return DurationText.minutesOf(totalMillis);
    }

    // ----- Commands -----
    public void addMillis(long deltaMillis) {
        if (deltaMillis > 0) {
            totalMillis = totalMillis + deltaMillis;
        }
    }

    // ----- Object overrides -----
    @Override
    public String toString() {
        return "PlayClock [" + getTotalMinutes() + " min]";
    }
}
