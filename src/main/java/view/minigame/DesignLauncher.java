package view.minigame;

import javax.swing.JFrame;

import model.minigame.MiniGameResult;
import provider.QuizProvider;

// Client-brief quiz for designers.
public final class DesignLauncher implements MiniGameLauncher {
    private final QuizProvider quizzes;

    public DesignLauncher() {
        this(new QuizProvider());
    }

    // Test seam: fixed question bank makes every run reproducible.
    public DesignLauncher(QuizProvider quizzes) {
        if (quizzes == null) {
            throw new IllegalArgumentException("quizzes must not be null");
        }
        this.quizzes = quizzes;
    }

    @Override
    public MiniGameResult launch(JFrame parent, String jobName) {
        QuizGameDialog dialog = new QuizGameDialog(
                parent, jobName + " — Client Brief Round", quizzes.getDesignQuestions(), null);
        return dialog.showAndPlay();
    }
}
