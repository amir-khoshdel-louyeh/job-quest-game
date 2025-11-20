package view.minigame;

import javax.swing.JFrame;

import model.minigame.MiniGameResult;

// Strategy: how to play one work-game kind. JobDialog picks by WorkGameKind
// from a registry instead of switching + news (OCP): a new game kind adds
// a launcher, never edits the dialog.
public interface MiniGameLauncher {
    MiniGameResult launch(JFrame parent, String jobName);
}
