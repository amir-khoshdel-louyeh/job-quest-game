package controller;

import model.career.Job;
import model.minigame.MiniGameResult;

// Every player action a screen can trigger.
// Views depend on this contract, not on the concrete GameController.
public interface GameCommands {

    Outcome purchaseService(String serviceName);

    Outcome doJob(Job job);

    Outcome doJob(Job job, MiniGameResult game);

    Outcome learnSkill(String skillName);
}
