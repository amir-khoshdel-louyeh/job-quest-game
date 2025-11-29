package controller;

import model.player.PlayerView;
import observer.Observable;

// What the game screen receives from the composition root.
// GamePanel depends on this contract instead of the concrete GameSession.
public interface GameContext {

    PlayerView playerView();

    Observable observable();

    GameCommands gameController();

    GameReads gameQueries();

    DeveloperCommands developerController();

    // The screen supplies the scheduler it wants; the context stays Swing-free.
    RoutineControl createRoutineController(MessageSink messages, Scheduler scheduler);
}
