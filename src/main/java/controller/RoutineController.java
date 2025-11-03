package controller;

/**
 * Controller for managing user routines (daily tasks, scheduled actions, etc.).
 *
 * Follows Single Responsibility Principle by handling only routine-related flows.
 */

import services.RoutineService;

public class RoutineController {
    private final UserController userController;
    private final view.GamePanel panel;
    private final RoutineService routineService;

    public RoutineController(UserController userController, view.GamePanel panel) {
        // create routine controller with user controller and UI panel
        this.userController = userController;
        this.panel = panel;
        this.routineService = RoutineService.getInstance();
    }

    public void decreaseEnergy() {
        // apply routine energy decrease and notify view if needed
        String message = routineService.processEnergyDecrease(userController);
        if (message != null) {
            panel.addChatMessage(message);
        }
        // The view will be updated via the Observer pattern, no need to call notifyObservers() here.
    }

    public void decreaseHealth() {
        // apply routine health decrease and notify view if needed
        String message = routineService.processHealthDecrease(userController);
        if (message != null) {
            panel.addChatMessage(message);
        }
    }

    public void sicknessCheck() {
        // run sickness check and report message to the view if any
        String message = routineService.processSicknessCheck(userController);
        if (message != null) {
            panel.addChatMessage(message);
        }
    }
}
