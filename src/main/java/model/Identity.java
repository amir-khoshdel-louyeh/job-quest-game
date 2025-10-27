package model;

/**
 * Represents the strategy for a user's professional identity.
 * Defines the behavior for daily work.
 */
public interface Identity {
    /** نتیجه کار روزانه برای این هویت */
    WorkResult performDailyWork();
}
