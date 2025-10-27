package utils;

import java.awt.event.ActionListener;

public class Timer {

    /** Run a task periodically on the Swing event thread */
    public static javax.swing.Timer runPeriodic(int delayMillis, ActionListener task) {
        javax.swing.Timer timer = new javax.swing.Timer(delayMillis, task);
        timer.start();
        return timer;
    }

    /** Run a task once after a delay on the Swing event thread */
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
