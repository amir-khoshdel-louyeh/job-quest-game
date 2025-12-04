package controller;

import controller.contract.GameContext;
import controller.contract.GameStarter;
import model.career.Profession;

// Concrete starter used by the application composition root.
// It is the only place that names GameSession when creating a run.
public final class GameSessionFactory implements GameStarter {

    @Override
    public GameContext start(String playerName, Profession profession) {
        return GameSession.start(playerName, profession);
    }
}
