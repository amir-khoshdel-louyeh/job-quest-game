package controller;

// Runs a task on a fixed period. The view layer supplies the real
// implementation (Swing timer), so controllers never import Swing.
public interface Scheduler {

    // Starts the task and returns a handle that stops it.
    Cancelable every(long periodMillis, Runnable task);
}
