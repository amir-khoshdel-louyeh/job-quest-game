package controller;

import services.RoutineService;

// Runs daily upkeep like energy, health, and sickness checks.
// Depends on MessageSink, not on the Swing panel (DIP).
public class RoutineController {
    private final UserController userController;
    private final MessageSink messages;
    private final RoutineService routines;

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
        String message = routines.processEnergyDecrease(userController);
        if (message != null) {
            messages.addChatMessage(message);
        }
    }

    public void decreaseHealth() {
        String message = routines.processHealthDecrease(userController);
        if (message != null) {
            messages.addChatMessage(message);
        }
    }

    public void sicknessCheck() {
        String message = routines.processSicknessCheck(userController);
        if (message != null) {
            messages.addChatMessage(message);
        }
    }

    // Starts the background upkeep timers (energy/health drains, sickness
    // rolls, block expiry). Owned here — not in the view — so scheduling
    // lives next to the logic it triggers, scheduled exactly once.
    public void startSchedulers() {
        final RoutineController self = this;
        utils.TimerUtil.runPeriodic(30000, new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                self.decreaseEnergy();
            }
        });
        utils.TimerUtil.runPeriodic(60000, new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                self.decreaseHealth();
            }
        });
        utils.TimerUtil.runPeriodic(24 * 60 * 60 * 1000, new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                self.sicknessCheck();
            }
        });

        // Unblock after 5 minutes (10h-run tuning, was 48 hours).
        final UserController users = userController;
        utils.TimerUtil.runOnce(5 * 60 * 1000, new Runnable() {
            public void run() {
                users.unblockUser();
            }
        });
    }
}
