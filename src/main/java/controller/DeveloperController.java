package controller;

import controller.contract.DeveloperCommands;
import model.player.DevControls;
import model.player.PlayerView;
import observer.ChangeType;
import observer.Subject;
import utils.Require;

// Lets developers set stats by hand for demos and testing.
// It depends only on abstractions: a read-only PlayerView for the current
// values, the narrow DevControls port for mutations, and Subject for the
// change notifications. No concrete aggregate or controller is referenced.
public final class DeveloperController implements DeveloperCommands {
    private final PlayerView player;
    private final DevControls devControls;
    private final Subject subject;

    // ----- Construction -----
    // Package-private: GameSession creates the one developer facade per run.
    DeveloperController(PlayerView player, DevControls devControls, Subject subject) {
        this.player = Require.notNull(player, "player");
        this.devControls = Require.notNull(devControls, "devControls");
        this.subject = Require.notNull(subject, "subject");
    }

    // ----- Read delegates -----
    @Override
    public int getHealth() {
        return player.getHealth();
    }

    @Override
    public int getEnergy() {
        return player.getEnergy();
    }

    @Override
    public int getLevel() {
        return player.getLevel();
    }

    @Override
    public int getReputation() {
        return player.getReputation();
    }

    @Override
    public long getPlayTimeMinutes() {
        return player.getPlayTimeMinutes();
    }

    @Override
    public int getJobBonus() {
        return player.getJobBonus();
    }

    @Override
    public int getBalance() {
        return player.getBalance();
    }

    // ----- Commands -----
    // Sets balance. Demo only.
    @Override
    public void setBalance(int amount) {
        devControls.setBalance(amount);
        subject.notifyObservers(ChangeType.BALANCE);
    }

    // Sets health. Demo only.
    @Override
    public void setHealth(int health) {
        devControls.setHealth(health);
        subject.notifyObservers(ChangeType.HEALTH);
    }

    // Sets energy. Demo only.
    @Override
    public void setEnergy(int energy) {
        devControls.setEnergy(energy);
        subject.notifyObservers(ChangeType.ENERGY);
    }

    // Adds playtime in minutes; non-positive values are ignored.
    @Override
    public void addPlaytimeMinutes(long minutes) {
        if (minutes <= 0) {
            return;
        }
        devControls.addPlayTimeMinutes(minutes);
        subject.notifyObservers();
    }

    // Sets playtime to an exact number of minutes. Demo only.
    @Override
    public void setPlaytimeMinutes(long minutes) {
        devControls.setPlayTimeMinutes(minutes);
        subject.notifyObservers();
    }

    // Sets level. Demo only.
    @Override
    public void setLevel(int level) {
        devControls.setLevel(level);
        subject.notifyObservers(ChangeType.XP);
    }

    // Sets reputation. Demo only.
    @Override
    public void setReputation(int reputation) {
        devControls.setReputation(reputation);
        subject.notifyObservers(ChangeType.REPUTATION);
    }

    // Sets job bonus. Demo only.
    @Override
    public void setJobBonus(int jobBonus) {
        devControls.setJobBonus(jobBonus);
        subject.notifyObservers();
    }

    // Lifts any active block. Demo only.
    @Override
    public void unblock() {
        devControls.unblock();
        subject.notifyObservers();
    }
}
