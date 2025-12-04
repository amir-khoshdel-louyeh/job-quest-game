package provider.contract;

import java.util.List;

import model.minigame.QuizQuestion;

// Read-only quiz question contract used by the mini-game launcher.
public interface QuizCatalog {

    List<QuizQuestion> getQuestions();
}
