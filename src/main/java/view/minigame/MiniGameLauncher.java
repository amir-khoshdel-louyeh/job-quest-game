package view.minigame;

import javax.swing.JFrame;

import model.minigame.MiniGameResult;

// Strategy: how to play the work game. JobDialog launches it directly;
// a new game adds a launcher without touching the dialog (OCP).
public interface MiniGameLauncher {
    MiniGameResult launch(JFrame parent, String jobName);
}
