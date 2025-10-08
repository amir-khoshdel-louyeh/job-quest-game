package main;

import javax.swing.SwingUtilities;
import database.DatabaseInitializer;
import view.MainFrame;

public class Main {
    public static void main(String[] args) {
        // Initialize database before starting the application
        DatabaseInitializer.initialize();
        
        SwingUtilities.invokeLater(() -> new MainFrame());
    }
}
