package utils;

import java.awt.event.ActionListener;
import javax.swing.Timer;

// Runs game tasks after a wait or on repeat.
public class TimerUtil {

    public static Timer runPeriodic(int delayMillis, ActionListener task) {
        Timer timer = new Timer(delayMillis, task);
        timer.start();
        return timer;
    }

    public static void runOnce(int delayMillis, Runnable task) {
        Timer timer = new Timer(delayMillis, actionEvent -> task.run());
        timer.setRepeats(false);
        timer.start();
    }
}
