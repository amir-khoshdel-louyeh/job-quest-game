package utils;

import java.util.function.Supplier;

// Stopwatch for elapsed-time measurement (mini-game rounds).
// Extracted from the duplicated startMillis/elapsedSeconds code in the
// game dialogs (DRY). The clock is injectable, so timing is testable.
public final class ElapsedTimer {
    private final Supplier<Long> clock;
    private long startMillis;

    public ElapsedTimer() {
        this(System::currentTimeMillis);
    }

    public ElapsedTimer(Supplier<Long> clock) {
        if (clock == null) {
            throw new IllegalArgumentException("clock must not be null");
        }
        this.clock = clock;
        start();
    }

    public void start() {
        startMillis = clock.get();
    }

    public int elapsedSeconds() {
        return (int) ((clock.get() - startMillis) / 1000);
    }
}
