package controller;

import observer.ChangeType;
import services.RoutineService;

// Runs periodic upkeep like energy, health, and sickness checks.
// Owns the Swing timers; the service mutates silently and this notifies.
public class RoutineController {
    private final UserController userController;
    private final MessageSink messages;
    private final RoutineService routines;
    private final java.util.List<javax.swing.Timer> timers = new java.util.ArrayList<>();

    // ----- Construction -----
    public RoutineController(UserController userController, MessageSink messages) {
        this(userController, messages, new RoutineService());
    }

    // Custom service for tests.
    public RoutineController(UserController userController, MessageSink messages, RoutineService routines) {
        if (userController == null) {
            throw new IllegalArgumentException("userController must not be null");
        }
        if (messages == null) {
            throw new IllegalArgumentException("messages must not be null");
        }
        if (routines == null) {
            throw new IllegalArgumentException("routines must not be null");
        }
        this.userController = userController;
        this.messages = messages;
        this.routines = routines;
    }

    // ----- Commands -----
    public void decreaseEnergy() {
        if (userController.getEnergy() <= 0) {
            return;
        }
        String message = routines.processEnergyDecrease(userController.getUser());
        userController.notifyObservers(ChangeType.ENERGY);
        if (message != null) {
            messages.addChatMessage(message);
        }
    }

    public void decreaseHealth() {
        String message = routines.processHealthDecrease(userController.getUser());
        userController.notifyObservers(ChangeType.HEALTH);
        if (message != null) {
            messages.addChatMessage(message);
        }
    }

    public void sicknessCheck() {
        String message = routines.processSicknessCheck(userController.getUser());
        if (message != null) {
            userController.notifyObservers(ChangeType.HEALTH);
            messages.addChatMessage(message);
        }
    }

    // Starts background upkeep timers; restarts cleanly if called twice.
    public void startSchedulers() {
        stopSchedulers();
        final RoutineController self = this;
        timers.add(utils.TimerUtil.runPeriodic(30000, new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                self.decreaseEnergy();
            }
        }));
        timers.add(utils.TimerUtil.runPeriodic(60000, new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                self.decreaseHealth();
            }
        }));
        timers.add(utils.TimerUtil.runPeriodic((int) utils.GameBalance.SICKNESS_COOLDOWN_MILLIS, new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                self.sicknessCheck();
            }
        }));

        // Unblocks after 5 minutes.
        final UserController users = userController;
        utils.TimerUtil.runOnce(5 * 60 * 1000, new Runnable() {
            public void run() {
                users.unblockUser();
            }
        });
    }

    // Halts upkeep timers.
    public void stopSchedulers() {
        for (int i = 0; i < timers.size(); i++) {
            try {
                timers.get(i).stop();
            } catch (Exception e) {
                utils.GameLog.error("stopping scheduler", e);
            }
        }
        timers.clear();
    }
}
