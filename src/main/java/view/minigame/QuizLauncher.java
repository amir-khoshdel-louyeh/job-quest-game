package view.minigame;

import java.util.function.Supplier;
import javax.swing.JFrame;

import model.minigame.MiniGameResult;
import model.minigame.ScoreStrategy;
import provider.contract.QuizCatalog;
import view.contract.MiniGameLauncher;
import utils.Require;

// The single quiz game every profession plays.
// Its question bank and scorer are abstractions supplied by the screen's
// composition root, so this class names no concrete provider or service.
public final class QuizLauncher implements MiniGameLauncher {
    private final QuizCatalog quizzes;
    private final ScoreStrategy scorer;
    private final Supplier<Long> clock;

    public QuizLauncher(QuizCatalog quizzes, ScoreStrategy scorer, Supplier<Long> clock) {
        this.quizzes = Require.notNull(quizzes, "quizzes");
        this.scorer = Require.notNull(scorer, "scorer");
        this.clock = Require.notNull(clock, "clock");
    }

    @Override
    public MiniGameResult launch(JFrame parent, String jobName) {
        QuizGameDialog dialog = new QuizGameDialog(
                parent, jobName + " — Quiz Round", quizzes.getAll(), scorer, clock);
        return dialog.showAndPlay();
    }
}
