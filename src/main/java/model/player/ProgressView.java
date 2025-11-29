package model.player;

// Level, XP, reputation, and job-bonus numbers.
public interface ProgressView {
    int getLevel();

    int getExperience();

    int getRequiredExperienceForNextLevel();

    int getExperiencePercentage();

    int getReputation();

    int getJobBonus();
}
