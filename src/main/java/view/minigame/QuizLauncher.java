package view.minigame;

import javax.swing.JFrame;

import model.minigame.MiniGameResult;
import model.minigame.ScoreStrategy;
import provider.QuizCatalog;
import utils.Require;

// The single quiz game every profession plays.
// Its question bank and scorer are abstractions supplied by the screen's
// composition root, so this class names no concrete provider or service.
public final class QuizLauncher implements MiniGameLauncher {
    private final QuizCatalog quizzes;
    private final ScoreStrategy scorer;

    public QuizLauncher(QuizCatalog quizzes, ScoreStrategy scorer) {
        this.quizzes = Require.notNull(quizzes, "quizzes");
        this.scorer = Require.notNull(scorer, "scorer");
    }

    @Override
    public MiniGameResult launch(JFrame parent, String jobName) {
        QuizGameDialog dialog = new QuizGameDialog(
                parent, jobName + " — Quiz Round", quizzes.getQuestions(), scorer);
        return dialog.showAndPlay();
    }
}
