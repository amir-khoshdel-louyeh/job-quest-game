package controller;

import java.util.ArrayList;
import java.util.List;

import model.player.UpkeepTarget;
import services.UpkeepRules;
import utils.GameBalance;

// Runs periodic upkeep like energy, health, sickness, and play time.
// Scheduling is injected (view.SwingScheduler in the app), so this class
// stays free of Swing. Its collaborators are abstractions: the UpkeepTarget
// port, the UpkeepRules contract, and the Scheduler handle.
public final class RoutineController implements RoutineControl {
    private static final long ENERGY_TICK_MILLIS = 30 * 1000L;
    private static final long HEALTH_TICK_MILLIS = 60 * 1000L;
    private static final long PLAY_TIME_TICK_MILLIS = 60 * 1000L;

    private final UpkeepTarget player;
    private final MessageSink messages;
    private final UpkeepRules routines;
    private final Scheduler scheduler;
    private final List<Cancelable> tasks = new ArrayList<Cancelable>();

    // ----- Construction -----
    // Package-private: GameSession.createRoutineController is the only entry.
    RoutineController(UpkeepTarget player, UpkeepRules routines, MessageSink messages,
                      Scheduler scheduler) {
        this.player = utils.Require.notNull(player, "player");
        this.routines = utils.Require.notNull(routines, "routines");
        this.messages = utils.Require.notNull(messages, "messages");
        this.scheduler = utils.Require.notNull(scheduler, "scheduler");
    }

    // ----- Commands -----
    // Tick actions stay package-private; views only start/stop the schedulers.
    void decreaseEnergy() {
        // The UpkeepTarget adapter publishes its own changes, so this only
        // reports the message.
        String message = routines.processEnergyDecrease(player);
        if (message != null) {
            messages.addChatMessage(message);
        }
    }

    void decreaseHealth() {
        String message = routines.processHealthDecrease(player);
        if (message != null) {
            messages.addChatMessage(message);
        }
    }

    void sicknessCheck() {
        String message = routines.processSicknessCheck(player);
        if (message != null) {
            messages.addChatMessage(message);
        }
    }

    // Records one minute of play time; exposed to package tests only.
    void recordPlayTime() {
        player.addPlayTime(PLAY_TIME_TICK_MILLIS);
    }

    // Starts background upkeep; restarts cleanly if called twice.
    @Override
    public void startSchedulers() {
        stopSchedulers();
        tasks.add(scheduler.every(ENERGY_TICK_MILLIS, this::decreaseEnergy));
        tasks.add(scheduler.every(HEALTH_TICK_MILLIS, this::decreaseHealth));
        tasks.add(scheduler.every(GameBalance.SICKNESS_COOLDOWN_MILLIS, this::sicknessCheck));
        tasks.add(scheduler.every(PLAY_TIME_TICK_MILLIS, this::recordPlayTime));
    }

    // Halts upkeep.
    @Override
    public void stopSchedulers() {
        for (Cancelable task : tasks) {
            try {
                task.cancel();
            } catch (RuntimeException e) {
                // One bad task must not leave the rest of the schedulers running.
                utils.GameLog.error("stopping scheduler", e);
            }
        }
        tasks.clear();
    }
}
