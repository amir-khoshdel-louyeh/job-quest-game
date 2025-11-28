package utils;

import java.util.function.Supplier;

// Stopwatch for elapsed-time measurement.
// Clock is injectable for tests.
public final class ElapsedTimer {
    private final Supplier<Long> clock;
    private long startMillis;

    public ElapsedTimer() {
        this(System::currentTimeMillis);
    }

    // Custom clock for tests.
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
