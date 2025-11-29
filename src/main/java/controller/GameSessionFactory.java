package controller;

import model.Identity;

// Concrete starter used by the application composition root.
// It is the only place that names GameSession when creating a run.
public final class GameSessionFactory implements GameStarter {

    @Override
    public GameContext start(String username, Identity identity) {
        return GameSession.start(username, identity);
    }
}
