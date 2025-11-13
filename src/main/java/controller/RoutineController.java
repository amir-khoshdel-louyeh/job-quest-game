package controller;

import services.RoutineService;

// Runs daily upkeep like energy, health, and sickness checks.
public class RoutineController {
    private final UserController userController;
    private final view.GamePanel panel;
    private final RoutineService routineService;

    public RoutineController(UserController userController, view.GamePanel panel) {
        this.userController = userController;
        this.panel = panel;
        this.routineService = RoutineService.getInstance();
    }

    public void decreaseEnergy() {
        String message = routineService.processEnergyDecrease(userController);
        if (message != null) {
            panel.addChatMessage(message);
        }
    }

    public void decreaseHealth() {
        String message = routineService.processHealthDecrease(userController);
        if (message != null) {
            panel.addChatMessage(message);
        }
    }

    public void sicknessCheck() {
        String message = routineService.processSicknessCheck(userController);
        if (message != null) {
            panel.addChatMessage(message);
        }
    }
}
