package model.minigame;

import java.util.List;

// How a finished mini-game round turns into a scored result.
// Implemented by services.minigame.QuizScorer; injected into QuizRound.
public interface ScoreStrategy {
    MiniGameResult score(String gameName, List<QuizQuestion> questions,
                         List<Integer> picked, int elapsedSeconds);
}
