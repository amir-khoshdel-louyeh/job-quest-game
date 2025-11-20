package view.minigame;

import javax.swing.JFrame;

import model.minigame.MiniGameResult;
import provider.QuizProvider;

// Client-brief quiz for designers.
public final class DesignLauncher implements MiniGameLauncher {
    @Override
    public MiniGameResult launch(JFrame parent, String jobName) {
        QuizGameDialog dialog = new QuizGameDialog(
                parent, jobName + " — Client Brief Round", QuizProvider.getDesignQuestions(), null);
        return dialog.showAndPlay();
    }
}
