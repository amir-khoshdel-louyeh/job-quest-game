package controller;

import model.Identity;
import model.career.SkillSet;
import model.player.PlayerView;
import model.player.User;

// Read-only player data for screens.
// It deliberately implements nothing else, so it cannot be cast to a command
// port or to the mutable User aggregate.
final class ReadOnlyPlayerView implements PlayerView {
    private final User user;

    ReadOnlyPlayerView(User user) {
        this.user = utils.Require.notNull(user, "user");
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public Identity getIdentity() {
        return user.getIdentity();
    }

    @Override
    public String getIdentityName() {
        return user.getIdentityName();
    }

    @Override
    public SkillSet getSkillSet() {
        return user.getSkillSet();
    }

    @Override
    public int getSkillCount() {
        return user.getSkillCount();
    }

    @Override
    public int getLevel() {
        return user.getLevel();
    }

    @Override
    public int getExperience() {
        return user.getExperience();
    }

    @Override
    public int getRequiredExperienceForNextLevel() {
        return user.getRequiredExperienceForNextLevel();
    }

    @Override
    public int getExperiencePercentage() {
        return user.getExperiencePercentage();
    }

    @Override
    public int getReputation() {
        return user.getReputation();
    }

    @Override
    public int getJobBonus() {
        return user.getJobBonus();
    }

    @Override
    public long getPlayTimeMinutes() {
        return user.getPlayTimeMinutes();
    }

    @Override
    public int getHealth() {
        return user.getHealth();
    }

    @Override
    public int getHealthPercentage() {
        return user.getHealthPercentage();
    }

    @Override
    public int getEnergy() {
        return user.getEnergy();
    }

    @Override
    public int getEnergyPercentage() {
        return user.getEnergyPercentage();
    }

    @Override
    public int getBalance() {
        return user.getBalance();
    }

    @Override
    public boolean isBlocked() {
        return user.isBlocked();
    }

    @Override
    public long getBlockedUntil() {
        return user.getBlockedUntil();
    }

    @Override
    public int getAchievementCount() {
        return user.getAchievementCount();
    }

    @Override
    public int getTotalJobsCompleted() {
        return user.getTotalJobsCompleted();
    }

    @Override
    public int getTotalMoneyEarned() {
        return user.getTotalMoneyEarned();
    }

    @Override
    public boolean hasAchievement(String id) {
        return user.hasAchievement(id);
    }
}
