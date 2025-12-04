package controller;

import controller.contract.SessionStats;
import utils.DurationText;

// Tracks one play session: elapsed minutes and successful jobs.
// GameController only asks for the summary line.
public final class SessionTracker implements SessionStats {
    private final long startTime;
    private int jobsCompleted;

    // ----- Construction -----
    SessionTracker(long startTime) {
        this.startTime = startTime;
    }

    // ----- Commands -----
    @Override
    public void recordCompletedJob() {
        jobsCompleted = jobsCompleted + 1;
    }

    // ----- Queries -----
    // Package-private: only summary() needs the elapsed minutes.
    long getMinutes() {
        return DurationText.minutesOf(System.currentTimeMillis() - startTime);
    }

    @Override
    public String summary() {
        return "Session: " + getMinutes() + " minutes | Jobs: " + jobsCompleted;
    }
}
