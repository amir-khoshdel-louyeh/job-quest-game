package main;

/**
 * Application entry point. Starts database initialization and the main GUI.
 *
 * Follows Single Responsibility Principle by managing only application startup.
 */

import javax.swing.SwingUtilities;
import database.DatabaseInitializer;
import view.MainFrame;

public class Main {
    public static void main(String[] args) {
        // initialize DB and start the main GUI frame
        DatabaseInitializer.initialize();
        
        SwingUtilities.invokeLater(() -> new MainFrame());
    }
}
