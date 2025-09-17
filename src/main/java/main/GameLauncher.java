package main;

import javax.swing.SwingUtilities;
import view.MainFrame;

public class GameLauncher {

    public static void launch() {
        SwingUtilities.invokeLater(() -> new MainFrame());
    }

    public static void main(String[] args) {
        launch();
    }
}
