package controller.contract;

import java.util.List;

import model.career.Job;
import model.career.Skill;
import model.goals.Achievement;
import model.goals.Challenge;
import model.player.PlayerView;
import model.store.ShopItem;

// Every read a screen needs. Views depend on this contract, not on GameQueries.
public interface GameReads {

    List<ShopItem> getHealthItems();

    List<ShopItem> getEnergyItems();

    List<Job> getJobsForCurrentIdentity();

    List<Skill> getSkillsForCurrentIdentity();

    PlayerView getPlayerView();

    String getDetailedStats();

    List<Challenge> getActiveChallenges();

    List<Challenge> getCompletedChallenges();

    List<Achievement> getAchievements();

    String getSessionStats();
}
