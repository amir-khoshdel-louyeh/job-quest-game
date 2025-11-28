package view.minigame;

import javax.swing.JFrame;

import model.minigame.MiniGameResult;

// How to play the work game; launched by JobDialog.
public interface MiniGameLauncher {
    MiniGameResult launch(JFrame parent, String jobName);
}
