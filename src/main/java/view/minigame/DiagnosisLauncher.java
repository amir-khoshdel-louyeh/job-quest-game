package view.minigame;

import javax.swing.JFrame;

import model.minigame.MiniGameResult;
import provider.QuizProvider;

// Diagnosis quiz for doctors.
public final class DiagnosisLauncher implements MiniGameLauncher {
    private final QuizProvider quizzes;

    public DiagnosisLauncher() {
        this(new QuizProvider());
    }

    // Test seam: fixed question bank makes every run reproducible.
    public DiagnosisLauncher(QuizProvider quizzes) {
        if (quizzes == null) {
            throw new IllegalArgumentException("quizzes must not be null");
        }
        this.quizzes = quizzes;
    }

    @Override
    public MiniGameResult launch(JFrame parent, String jobName) {
        QuizGameDialog dialog = new QuizGameDialog(
                parent, jobName + " — Diagnosis Round", quizzes.getDiagnosisQuestions(), null);
        return dialog.showAndPlay();
    }
}
