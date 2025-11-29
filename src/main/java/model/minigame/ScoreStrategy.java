package model.minigame;

import java.util.List;

// How a finished mini-game round turns into a scored result.
// Implemented by services.minigame.QuizScorer; injected into QuizRound.
public interface ScoreStrategy {

    // Par time for one round; the screen and the scorer share this constant,
    // so the view never has to name a concrete scorer.
    int ROUND_PAR_SECONDS = 90;

    MiniGameResult score(String gameName, List<QuizQuestion> questions,
                         List<Integer> picked, int elapsedSeconds);
}
