package controller;

import services.RoutineService;

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
        // The view will be updated via the Observer pattern, no need to call notifyObservers() here.
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
