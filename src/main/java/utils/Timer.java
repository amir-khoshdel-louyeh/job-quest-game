package utils;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// Runs game tasks after a wait or on repeat.
public class Timer {

    public static javax.swing.Timer runPeriodic(int delayMillis, ActionListener task) {
        javax.swing.Timer timer = new javax.swing.Timer(delayMillis, task);
        timer.start();
        return timer;
    }

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
