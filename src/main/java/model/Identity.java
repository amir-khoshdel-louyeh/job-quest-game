package model;

/**
 * Represents the strategy for a user's professional identity.
 * Defines the behavior for daily work.
 */
public interface Identity {
    /** Result of daily work for this identity */
    WorkResult performDailyWork();
}
