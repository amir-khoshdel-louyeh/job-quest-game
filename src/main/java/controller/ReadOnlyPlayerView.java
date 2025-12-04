package controller;

import model.career.Profession;
import model.career.SkillSet;
import model.player.PlayerView;
import model.player.Player;
import utils.Require;

// Read-only player data for screens.
// It deliberately implements nothing else, so it cannot be cast to a command
// port or to the mutable Player aggregate.
final class ReadOnlyPlayerView implements PlayerView {
    private final Player player;

    ReadOnlyPlayerView(Player player) {
        this.player = Require.notNull(player, "player");
    }

    @Override
    public String getPlayerName() {
        return player.getPlayerName();
    }

    @Override
    public Profession getProfession() {
        return player.getProfession();
    }

    @Override
    public String getProfessionDisplayName() {
        return player.getProfessionDisplayName();
    }

    @Override
    public SkillSet getSkillSet() {
        return player.getSkillSet();
    }

    @Override
    public int getSkillCount() {
        return player.getSkillCount();
    }

    @Override
    public int getLevel() {
        return player.getLevel();
    }

    @Override
    public int getExperience() {
        return player.getExperience();
    }

    @Override
    public int getRequiredExperienceForNextLevel() {
        return player.getRequiredExperienceForNextLevel();
    }

    @Override
    public int getExperiencePercentage() {
        return player.getExperiencePercentage();
    }

    @Override
    public int getReputation() {
        return player.getReputation();
    }

    @Override
    public int getJobBonus() {
        return player.getJobBonus();
    }

    @Override
    public long getPlayTimeMinutes() {
        return player.getPlayTimeMinutes();
    }

    @Override
    public int getHealth() {
        return player.getHealth();
    }

    @Override
    public int getHealthPercentage() {
        return player.getHealthPercentage();
    }

    @Override
    public int getEnergy() {
        return player.getEnergy();
    }

    @Override
    public int getEnergyPercentage() {
        return player.getEnergyPercentage();
    }

    @Override
    public int getBalance() {
        return player.getBalance();
    }

    @Override
    public boolean isBlocked() {
        return player.isBlocked();
    }

    @Override
    public long getBlockedUntil() {
        return player.getBlockedUntil();
    }

    @Override
    public int getAchievementCount() {
        return player.getAchievementCount();
    }

    @Override
    public int getTotalJobsCompleted() {
        return player.getTotalJobsCompleted();
    }

    @Override
    public int getTotalMoneyEarned() {
        return player.getTotalMoneyEarned();
    }

    @Override
    public boolean hasAchievement(String id) {
        return player.hasAchievement(id);
    }
}
