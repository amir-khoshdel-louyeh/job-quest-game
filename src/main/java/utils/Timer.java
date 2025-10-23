package utils;

import java.awt.event.ActionListener;

public class Timer {

    /**
     * Runs a task periodically on the Swing event thread.
     * @param delayMillis interval in milliseconds
     * @param task the task to run
     * @return the javax.swing.Timer instance (can be stopped with .stop())
     */
    public static javax.swing.Timer runPeriodic(int delayMillis, ActionListener task) {
        javax.swing.Timer timer = new javax.swing.Timer(delayMillis, task);
        timer.start();
        return timer;
    }

    /**
     * Runs a task once after a delay on the Swing event thread.
     * @param delayMillis delay in milliseconds
     * @param task the task to run
     */
    public static void runOnce(int delayMillis, Runnable task) {
        new javax.swing.Timer(delayMillis, e -> {
            task.run();
        }) {{
            setRepeats(false);
            start();
        }};
    }

    // Usage examples (do NOT put these in the class body):
    // utils.Timer.runPeriodic(60_000, e -> routineController.decreaseEnergy());
    // utils.Timer.runOnce(48 * 60 * 60 * 1000, () -> userController.unblockUser());
}
