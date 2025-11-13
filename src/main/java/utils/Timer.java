package utils;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// Simple timer helper for the game.
// It runs tasks after some time using Swing Timer.
public class Timer {

    // Run a task every X milliseconds
    public static javax.swing.Timer runPeriodic(int delayMillis, ActionListener task) {
        javax.swing.Timer timer = new javax.swing.Timer(delayMillis, task);
        timer.start();
        return timer;
    }

    // Run a task once after delay.
    // Uses simple ActionListener class (no lambda).
    public static void runOnce(int delayMillis, final Runnable task) {
        javax.swing.Timer timer = new javax.swing.Timer(delayMillis, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                task.run();
            }
        });
        timer.setRepeats(false);
        timer.start();
    }
}
