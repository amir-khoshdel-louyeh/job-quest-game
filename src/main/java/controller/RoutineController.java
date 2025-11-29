package controller;

import java.util.ArrayList;
import java.util.List;

import services.RoutineService;
import utils.GameBalance;

// Runs periodic upkeep like energy, health, sickness, and play time.
// Scheduling is injected (view.SwingScheduler in the app), so this class
// stays free of Swing; UserController is the UpkeepTarget and publishes the
// matching changes itself.
public final class RoutineController {
    private static final long ENERGY_TICK_MILLIS = 30 * 1000L;
    private static final long HEALTH_TICK_MILLIS = 60 * 1000L;
    private static final long PLAY_TIME_TICK_MILLIS = 60 * 1000L;

    private final UserController userController;
    private final MessageSink messages;
    private final RoutineService routines;
    private final Scheduler scheduler;
    private final List<Scheduler.Cancelable> tasks = new ArrayList<Scheduler.Cancelable>();

    // ----- Construction -----
    // Package-private: GameSession.createRoutineController is the only entry.
    RoutineController(UserController userController, MessageSink messages,
                      Scheduler scheduler) {
        this.userController = utils.Require.notNull(userController, "userController");
        this.messages = utils.Require.notNull(messages, "messages");
        this.routines = new RoutineService();
        this.scheduler = utils.Require.notNull(scheduler, "scheduler");
    }

    // ----- Commands -----
    // Tick actions stay package-private; views only start/stop the schedulers.
    void decreaseEnergy() {
        // UserController is the UpkeepTarget: drains and the knockout fine
        // notify observers themselves, so this only reports the message.
        String message = routines.processEnergyDecrease(userController);
        if (message != null) {
            messages.addChatMessage(message);
        }
    }

    void decreaseHealth() {
        String message = routines.processHealthDecrease(userController);
        if (message != null) {
            messages.addChatMessage(message);
        }
    }

    void sicknessCheck() {
        String message = routines.processSicknessCheck(userController);
        if (message != null) {
            messages.addChatMessage(message);
        }
    }

    // Records one minute of play time; exposed to package tests only.
    void recordPlayTime() {
        userController.addPlayTime(PLAY_TIME_TICK_MILLIS);
    }

    // Starts background upkeep; restarts cleanly if called twice.
    public void startSchedulers() {
        stopSchedulers();
        tasks.add(scheduler.every(ENERGY_TICK_MILLIS, this::decreaseEnergy));
        tasks.add(scheduler.every(HEALTH_TICK_MILLIS, this::decreaseHealth));
        tasks.add(scheduler.every(GameBalance.SICKNESS_COOLDOWN_MILLIS, this::sicknessCheck));
        tasks.add(scheduler.every(PLAY_TIME_TICK_MILLIS, this::recordPlayTime));
    }

    // Halts upkeep.
    public void stopSchedulers() {
        for (Scheduler.Cancelable task : tasks) {
            try {
                task.cancel();
            } catch (Exception e) {
                utils.GameLog.error("stopping scheduler", e);
            }
        }
        tasks.clear();
    }
}
