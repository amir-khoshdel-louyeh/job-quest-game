package view.minigame;

import javax.swing.JFrame;

import model.minigame.MiniGameResult;
import provider.QuizProvider;

// The single quiz game every profession plays.
public final class QuizLauncher implements MiniGameLauncher {
    private final QuizProvider quizzes = new QuizProvider();

    @Override
    public MiniGameResult launch(JFrame parent, String jobName) {
        QuizGameDialog dialog = new QuizGameDialog(
                parent, jobName + " — Quiz Round", quizzes.getQuestions(), null);
        return dialog.showAndPlay();
    }
}
