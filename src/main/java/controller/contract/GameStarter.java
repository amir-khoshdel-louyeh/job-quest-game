package controller.contract;

import model.career.Profession;

// Starts one run. The start screen depends on this contract, and the
// composition root supplies the concrete factory.
public interface GameStarter {

    GameContext start(String playerName, Profession profession);
}
