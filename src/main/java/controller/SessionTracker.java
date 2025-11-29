package controller;

import java.util.function.Supplier;

import utils.DurationText;

// Tracks one play session: elapsed minutes and successful jobs.
// GameController only asks for the summary line.
public final class SessionTracker {
    private final long startTime;
    private final Supplier<Long> clock;
    private int jobsCompleted;

    // ----- Construction -----
    public SessionTracker(long startTime) {
        this(startTime, System::currentTimeMillis);
    }

    // Fixed clock for tests.
    public SessionTracker(long startTime, Supplier<Long> clock) {
        if (clock == null) {
            throw new IllegalArgumentException("clock must not be null");
        }
        this.startTime = startTime;
        this.clock = clock;
    }

    // ----- Commands -----
    public void recordCompletedJob() {
        jobsCompleted = jobsCompleted + 1;
    }

    // ----- Queries -----
    public int getJobsCompleted() {
        return jobsCompleted;
    }

    public long getMinutes() {
        return DurationText.minutesOf(clock.get() - startTime);
    }

    public String summary() {
        return "Session: " + getMinutes() + " minutes | Jobs: " + jobsCompleted;
    }
}
