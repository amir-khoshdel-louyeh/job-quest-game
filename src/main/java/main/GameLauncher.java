package main;

import javax.swing.SwingUtilities;
import database.DatabaseInitializer;
import view.MainFrame;

public class GameLauncher {

    public static void launch() {
        // Initialize database before starting the application
        DatabaseInitializer.initialize();
        
        SwingUtilities.invokeLater(() -> new MainFrame());
    }

    public static void main(String[] args) {
        launch();
    }
}
