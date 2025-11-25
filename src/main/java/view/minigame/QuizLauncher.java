package view.minigame;

import javax.swing.JFrame;

import model.minigame.MiniGameResult;
import provider.QuizProvider;

// The single quiz game every profession plays.
public final class QuizLauncher implements MiniGameLauncher {
    private final QuizProvider quizzes;

    public QuizLauncher() {
        this(new QuizProvider());
    }

    // Test seam: fixed question bank makes every run reproducible.
    public QuizLauncher(QuizProvider quizzes) {
        if (quizzes == null) {
            throw new IllegalArgumentException("quizzes must not be null");
        }
        this.quizzes = quizzes;
    }

    @Override
    public MiniGameResult launch(JFrame parent, String jobName) {
        QuizGameDialog dialog = new QuizGameDialog(
                parent, jobName + " — Quiz Round", quizzes.getQuestions(), null);
        return dialog.showAndPlay();
    }
}
