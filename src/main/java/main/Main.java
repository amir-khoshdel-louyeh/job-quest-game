package main;

import javax.swing.SwingUtilities;
import view.MainFrame;

// Starts the game and opens the main window.
public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainFrame());
    }
}
