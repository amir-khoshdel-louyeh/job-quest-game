package controller;

import model.player.DevControls;
import observer.ChangeType;

// Lets developers set stats by hand for demos and testing.
// Every mutation goes through the narrow DevControls port and then publishes
// the matching change, so views stay in sync with the cheated aggregate.
public final class DeveloperController {
    private final UserController userController;

    // ----- Construction -----
    // Package-private: GameSession creates the one developer facade per run.
    DeveloperController(UserController userController) {
        this.userController = utils.Require.notNull(userController, "userController");
    }

    // ----- Read delegates -----
    public int getHealth() {
        return userController.getHealth();
    }

    public int getEnergy() {
        return userController.getEnergy();
    }

    public int getLevel() {
        return userController.getLevel();
    }

    public int getReputation() {
        return userController.getReputation();
    }

    public long getPlayTimeMinutes() {
        return userController.getPlayTimeMinutes();
    }

    public int getJobBonus() {
        return userController.getJobBonus();
    }

    public int getBalance() {
        return userController.getBalance();
    }

    // ----- Commands -----
    // Sets balance. Demo only.
    public void setBalance(int amount) {
        dev().setBalance(amount);
        userController.notifyObservers(ChangeType.BALANCE);
    }

    // Sets health. Demo only.
    public void setHealth(int hp) {
        dev().setHealth(hp);
        userController.notifyObservers(ChangeType.HEALTH);
    }

    // Sets energy. Demo only.
    public void setEnergy(int energy) {
        dev().setEnergy(energy);
        userController.notifyObservers(ChangeType.ENERGY);
    }

    // Adds playtime in minutes; non-positive values are ignored.
    public void addPlaytimeMinutes(long minutes) {
        if (minutes <= 0) {
            return;
        }
        dev().addPlayTimeMinutes(minutes);
        userController.publishAll();
    }

    // Sets playtime to an exact number of minutes. Demo only.
    public void setPlaytimeMinutes(long minutes) {
        dev().setPlayTimeMinutes(minutes);
        userController.publishAll();
    }

    // Sets level. Demo only.
    public void setLevel(int level) {
        dev().setLevel(level);
        userController.notifyObservers(ChangeType.XP);
    }

    // Sets reputation. Demo only.
    public void setReputation(int reputation) {
        dev().setReputation(reputation);
        userController.notifyObservers(ChangeType.REPUTATION);
    }

    // Sets job bonus. Demo only.
    public void setJobBonus(int jobBonus) {
        dev().setJobBonus(jobBonus);
        userController.notifyObservers();
    }

    // Lifts any active block. Demo only.
    public void unblock() {
        userController.unblockUser();
        userController.publishAll();
    }

    // ----- Private helpers -----
    // The aggregate's demo port; User itself never leaves the controller layer.
    private DevControls dev() {
        return userController.devControls();
    }
}
