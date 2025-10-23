package controller;

public class RoutineController {
    private UserController userController;
    private view.GamePanel panel; // Keep for dialogs, but not for UI updates

    public RoutineController(UserController userController, view.GamePanel panel) {
        this.userController = userController;
        this.panel = panel;
    }

    public void performDailyRoutines() {
        decreaseEnergy();
        decreaseHealth();
        sicknessCheck();
        // Add more routines as needed
    }

    public void decreaseEnergy() {
        if (userController.getEnergy() > 0) {
            int energyLoss = 1; // Base energy loss
            // If health is low (e.g., below 30), you lose energy faster
            if (userController.getHealth() < 30) {
                energyLoss = 2;
            }

            userController.decreaseEnergy(energyLoss);
            if (energyLoss > 1) {
                panel.addChatMessage("⚡ Energy decreased by " + energyLoss + " due to poor health.");
            }

            if (userController.getEnergy() <= 10_000 && userController.getEnergy() > 0) {
                panel.showEnergyWarning();
            }

            if (userController.getEnergy() == 0) {
                blockUserFor48Hours("Energy reached 0");
            }
            userController.notifyObservers(); // Notify the UI to update
        }
    }

    public void decreaseHealth() {
        int healthLoss = 1; // Base health loss

        // If energy is critically low (e.g., below 25,000), health deteriorates faster.
        if (userController.getEnergy() < 25_000) {
            healthLoss = 3; // Increased health loss
            panel.addChatMessage("❤️‍🩹 Health decreased by " + healthLoss + " due to extreme fatigue!");
        } else {
            // We can keep the regular message for the base case, or remove it to reduce chat spam
            // panel.addChatMessage("❤️‍🩹 Health decreased by " + healthLoss + " due to fatigue.");
        }

        userController.decreaseHealth(healthLoss);
        userController.notifyObservers(); // Notify the UI to update
        checkHealth();
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
            userController.notifyObservers(); // Notify the UI to update
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
