package main;

/**
 * Game launcher responsible for initializing any pre-GUI logic and launching the app.
 *
 * Follows Single Responsibility Principle by handling only application launch tasks.
 */

import javax.swing.SwingUtilities;
import database.DatabaseInitializer;
import view.MainFrame;

public class GameLauncher {

    public static void launch() {
        // initialize DB and start the main GUI frame
        // Initialize database before starting the application
        DatabaseInitializer.initialize();
        
        SwingUtilities.invokeLater(() -> new MainFrame());
    }

    public static void main(String[] args) {
        // program entry that delegates to launch()
        launch();
    }
}
