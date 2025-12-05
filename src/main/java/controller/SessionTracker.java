package controller;

import controller.contract.SessionStats;
import java.util.function.Supplier;
import utils.DurationText;
import utils.Require;

// Tracks one play session: elapsed minutes and successful jobs.
// GameController only asks for the summary line.
// The clock is injected so session length can be tested without waiting.
public final class SessionTracker implements SessionStats {
    private final Supplier<Long> clock;
    private final long startTime;
    private int jobsCompleted;

    // ----- Construction -----
    SessionTracker(Supplier<Long> clock) {
        this.clock = Require.notNull(clock, "clock");
        this.startTime = clock.get();
    }

    // ----- Commands -----
    @Override
    public void recordCompletedJob() {
        jobsCompleted = jobsCompleted + 1;
    }

    // ----- Queries -----
    // Package-private: only summary() needs the elapsed minutes.
    long getMinutes() {
        return DurationText.minutesOf(clock.get() - startTime);
    }

    @Override
    public String summary() {
        return "Session: " + getMinutes() + " minutes | Jobs: " + jobsCompleted;
    }
}
