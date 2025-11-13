package main;

/**
 * Application entry point.
 */

import javax.swing.SwingUtilities;
import view.MainFrame;

public class Main {
    public static void main(String[] args) {
        // No database - start directly in picking a career.
        SwingUtilities.invokeLater(() -> new MainFrame());
    }
}
