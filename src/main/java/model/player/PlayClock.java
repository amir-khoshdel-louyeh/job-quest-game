package model.player;

import utils.DurationText;
import utils.Require;

// Tracks how long the player has played.
// Used by model.player.Player (sole owner); read via Player/PlayerController.
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
        Require.notNegative(deltaMillis, "deltaMillis");
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
