package view.minigame;

import javax.swing.JFrame;

import model.minigame.MiniGameResult;
import provider.QuizProvider;

// Diagnosis quiz for doctors.
public final class DiagnosisLauncher implements MiniGameLauncher {
    @Override
    public MiniGameResult launch(JFrame parent, String jobName) {
        QuizGameDialog dialog = new QuizGameDialog(
                parent, jobName + " — Diagnosis Round", QuizProvider.getDiagnosisQuestions(), null);
        return dialog.showAndPlay();
    }
}
