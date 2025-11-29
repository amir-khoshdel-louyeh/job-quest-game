package controller;

// Handle for one scheduled task; cancelling it stops the repetition.
// Its own type so Scheduler stays a one-method abstraction.
public interface Cancelable {
    void cancel();
}
