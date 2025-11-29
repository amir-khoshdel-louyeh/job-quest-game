package controller;

import model.career.Job;
import model.career.JobRewards;
import model.career.LearnableSkill;
import model.minigame.MiniGameResult;
import model.store.Service;

// Every player action a screen can trigger.
// Views depend on this contract, not on the concrete GameController.
public interface GameCommands {

    Outcome<Service> purchaseService(String serviceName);

    Outcome<JobRewards> doJob(Job job);

    Outcome<JobRewards> doJob(Job job, MiniGameResult game);

    Outcome<LearnableSkill> learnSkill(String skillName);
}
