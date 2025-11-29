package view;

import javax.swing.Timer;

import controller.Cancelable;
import controller.Scheduler;

// Swing-backed scheduler: the only place where upkeep meets Swing.
public final class SwingScheduler implements Scheduler {

    @Override
    public Cancelable every(long periodMillis, Runnable task) {
        if (task == null) {
            throw new IllegalArgumentException("task must not be null");
        }
        if (periodMillis <= 0) {
            throw new IllegalArgumentException("periodMillis must be > 0");
        }
        Timer timer = new Timer((int) Math.min(periodMillis, Integer.MAX_VALUE),
                event -> task.run());
        timer.start();
        return timer::stop;
    }
}
