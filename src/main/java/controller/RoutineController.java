package controller;

import services.RoutineService;

// Runs daily upkeep like energy, health, and sickness checks.
public class RoutineController {
    private final UserController userController;
    private final view.GamePanel panel;

    public RoutineController(UserController userController, view.GamePanel panel) {
        this.userController = userController;
        this.panel = panel;
    }

    public void decreaseEnergy() {
        String message = RoutineService.processEnergyDecrease(userController);
        if (message != null) {
            panel.addChatMessage(message);
        }
    }

    public void decreaseHealth() {
        String message = RoutineService.processHealthDecrease(userController);
        if (message != null) {
            panel.addChatMessage(message);
        }
    }

    public void sicknessCheck() {
        String message = RoutineService.processSicknessCheck(userController);
        if (message != null) {
            panel.addChatMessage(message);
        }
    }
}
