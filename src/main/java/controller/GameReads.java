package controller;

import java.util.List;

import model.career.Job;
import model.career.LearnableSkill;
import model.goals.Achievement;
import model.goals.Challenge;
import model.player.PlayerView;
import model.store.Service;

// Every read a screen needs. Views depend on this contract, not on GameQueries.
public interface GameReads {

    List<Service> getHealthServices();

    List<Service> getEnergyServices();

    List<Job> getJobsForCurrentIdentity();

    List<LearnableSkill> getSkillsForCurrentIdentity();

    PlayerView getPlayerView();

    String getDetailedStats();

    List<Challenge> getActiveChallenges();

    List<Challenge> getCompletedChallenges();

    List<Achievement> getAchievements();

    String getSessionStats();
}
