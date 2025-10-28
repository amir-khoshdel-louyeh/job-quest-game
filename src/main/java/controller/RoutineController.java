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
        String message = routineService.processEnergyDecrease(userController.getUser());
        if (message != null) {
            panel.addChatMessage(message);
        }
        // The view will be updated via the Observer pattern, no need to call notifyObservers() here.
    }

    public void decreaseHealth() {
        String message = routineService.processHealthDecrease(userController);
        String message = routineService.processHealthDecrease(userController.getUser());
        if (message != null) {
            panel.addChatMessage(message);
        }
    }

    public void sicknessCheck() {
        String message = routineService.processSicknessCheck(userController);
        String message = routineService.processSicknessCheck(userController.getUser());
        if (message != null) {
            panel.addChatMessage(message);
        }
    }
}
