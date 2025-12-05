package view.minigame;

import java.util.function.Supplier;

import utils.Require;

// Stopwatch for elapsed-time measurement.
// The quiz dialog owns one to time a round.
// The clock is injected so a round can be timed deterministically in tests.
public final class ElapsedTimer {
    private final Supplier<Long> clock;
    private long startMillis;

    ElapsedTimer(Supplier<Long> clock) {
        this.clock = Require.notNull(clock, "clock");
        start();
    }

    public void start() {
        startMillis = clock.get();
    }

    public int elapsedSeconds() {
        return (int) ((clock.get() - startMillis) / 1000);
    }
}
