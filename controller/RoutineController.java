package controller;

import view.GamePanel;

public class RoutineController {
    private UserController userController;
    private GamePanel panel;

    public RoutineController(UserController userController, GamePanel panel) {
        this.userController = userController;
        this.panel = panel;
    }

    public void performDailyRoutines() {
        decreaseEnergy();
        sicknessCheck();
        // Add more routines as needed
    }

    public void decreaseEnergy() {
        if (userController.getEnergy() > 0) {
            userController.decreaseEnergy(1);
            panel.updateUserInfo();

            if (userController.getEnergy() <= 10_000 && userController.getEnergy() > 0) {
                panel.showEnergyWarning();
            }

            if (userController.getEnergy() == 0) {
                blockUserFor48Hours("Energy reached 0");
            }
        }
    }

    public void sicknessCheck() {
        long now = System.currentTimeMillis();
        long lastSick = userController.getUser().getLastSicknessTime();

        boolean forcedSickness = (now - lastSick) >= 72 * 60 * 60 * 1000L;
        boolean randomSickness = Math.random() < 0.2;

        if (forcedSickness || randomSickness) {
            int healthLoss = (int) (userController.getHealth() * 0.05);
            userController.decreaseHealth(healthLoss);
            userController.getUser().setLastSicknessTime(now);
            panel.addChatMessage("💀 " + userController.getUsername() + " got sick! Lost 5% health.");
            panel.updateUserInfo();
            checkHealth();
        }
    }

    private void checkHealth() {
        if (userController.getHealth() <= 20 && userController.getHealth() > 0) {
            panel.showHealthWarning();
        }

        if (userController.getHealth() == 0) {
            blockUserFor48Hours("Health reached 0");
        }
    }

    private void blockUserFor48Hours(String reason) {
        int penalty = 2000;
        userController.deductBalance(penalty);
        panel.addChatMessage("❌ " + userController.getUsername() + " is blocked for 48 hours due to: " + reason + ". Penalty: $" + penalty);
        panel.showBlockDialog(reason, penalty);

        userController.blockUser(48 * 60 * 60 * 1000L);
        panel.disableActions();
    }
}
