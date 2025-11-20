package view.minigame;

import javax.swing.JFrame;

import model.minigame.MiniGameResult;
import provider.CodeTaskProvider;

// Code writing for programmers.
public final class CodeLauncher implements MiniGameLauncher {
    @Override
    public MiniGameResult launch(JFrame parent, String jobName) {
        TypingGameDialog dialog = new TypingGameDialog(
                parent, jobName + " — Write the Program",
                CodeTaskProvider.get().getTaskForJob(jobName), null, true);
        return dialog.showAndPlay();
    }
}
