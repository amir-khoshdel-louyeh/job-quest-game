package controller;

import services.RoutineService;

// Runs daily upkeep like energy, health, and sickness checks.
// Depends on MessageSink, not on the Swing panel (DIP).
public class RoutineController {
    private final UserController userController;
    private final MessageSink messages;

    public RoutineController(UserController userController, MessageSink messages) {
        if (userController == null) {
            throw new IllegalArgumentException("userController must not be null");
        }
        if (messages == null) {
            throw new IllegalArgumentException("messages must not be null");
        }
        this.userController = userController;
        this.messages = messages;
    }

    public void decreaseEnergy() {
        String message = RoutineService.processEnergyDecrease(userController);
        if (message != null) {
            messages.addChatMessage(message);
        }
    }

    public void decreaseHealth() {
        String message = RoutineService.processHealthDecrease(userController);
        if (message != null) {
            messages.addChatMessage(message);
        }
    }

    public void sicknessCheck() {
        String message = RoutineService.processSicknessCheck(userController);
        if (message != null) {
            messages.addChatMessage(message);
        }
    }
}
