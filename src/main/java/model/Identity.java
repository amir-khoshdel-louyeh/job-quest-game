package model;

/**
 * Represents the strategy for a user's professional identity.
 * Defines the behavior for daily work.
 */
public interface Identity {
    /**
     * Defines the outcome of performing a day's work for this identity.
     * This method should not modify any state directly.
     * @return A WorkResult object describing the outcome.
     */
    WorkResult performDailyWork();
}
