package view.minigame;

import javax.swing.JFrame;

import model.minigame.MiniGameResult;
import provider.TypingTaskProvider;

// Typing sprint for typists.
public final class TypingLauncher implements MiniGameLauncher {
    @Override
    public MiniGameResult launch(JFrame parent, String jobName) {
        TypingGameDialog dialog = new TypingGameDialog(
                parent, jobName + " — Typing Sprint",
                TypingTaskProvider.get().getTaskForJob(jobName), null, false);
        return dialog.showAndPlay();
    }
}
