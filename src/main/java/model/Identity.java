package model;

/**
 * Represents the strategy for a user's professional identity.
 * Defines the behavior for daily work.
 */
public interface Identity {
    // perform the daily work action for this identity
    WorkResult performDailyWork();
}
