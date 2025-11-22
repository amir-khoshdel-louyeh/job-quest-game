package controller;

import observer.ChangeType;
import services.RoutineService;

// Runs daily upkeep like energy, health, and sickness checks.
// Depends on MessageSink, not on the Swing panel (DIP).
// Owns all observer notifications for upkeep: the service mutates the
// User silently, so exactly one place decides what the UI hears.
// Owns its Swing Timers so the panel can stop them on detach (no leak
// when MainFrame swaps panels): startSchedulers is idempotent, and
// stopSchedulers halts all periodic tasks.
public class RoutineController {
    private final UserController userController;
    private final MessageSink messages;
    private final RoutineService routines;
    private final java.util.List<javax.swing.Timer> timers = new java.util.ArrayList<>();

    public RoutineController(UserController userController, MessageSink messages) {
        this(userController, messages, new RoutineService());
    }

    // Test seam: inject a service with fixed clock/random.
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

    public void decreaseEnergy() {
        if (userController.getEnergy() <= 0) {
            return; // matches the service guard: nothing changes, nothing notifies
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
            // A message means sickness struck (the only state change that notifies).
            userController.notifyObservers(ChangeType.HEALTH);
            messages.addChatMessage(message);
        }
    }

    // Starts the background upkeep timers (energy/health drains, sickness
    // rolls, block expiry). Owned here — not in the view — so scheduling
    // lives next to the logic it triggers. Idempotent: calling twice
    // restarts cleanly instead of piling up duplicate timers.
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
        timers.add(utils.TimerUtil.runPeriodic(24 * 60 * 60 * 1000, new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                self.sicknessCheck();
            }
        }));

        // Unblock after 5 minutes (10h-run tuning, was 48 hours).
        final UserController users = userController;
        utils.TimerUtil.runOnce(5 * 60 * 1000, new Runnable() {
            public void run() {
                users.unblockUser();
            }
        });
    }

    // Halts all periodic upkeep (called when the panel detaches).
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
