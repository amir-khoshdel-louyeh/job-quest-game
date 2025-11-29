package model.player;

import model.Identity;
import model.career.SkillSet;
import model.goals.AchievementStats;

// Complete read-only player surface.
// Screens receive only this interface, never the mutable User aggregate:
// GameSession hands out the package-private UserController as a PlayerView.
public interface PlayerView extends AchievementStats {

    // ----- Identity -----
    String getUsername();

    Identity getIdentity();

    String getIdentityName();

    // ----- Skills -----
    // The query-only SkillSet is enough for every screen: job and skill
    // availability ask it directly, so no separate hasSkill read is needed.
    SkillSet getSkillSet();

    // ----- Progress -----
    int getLevel();

    int getExperience();

    int getRequiredExperienceForNextLevel();

    int getExperiencePercentage();

    int getReputation();

    int getJobBonus();

    long getPlayTimeMinutes();

    // ----- Vitals / wallet -----
    int getHealth();

    int getHealthPercentage();

    int getEnergy();

    int getEnergyPercentage();

    // ----- Block -----
    boolean isBlocked();

    long getBlockedUntil();

    // ----- Achievement summary -----
    int getAchievementCount();
}
