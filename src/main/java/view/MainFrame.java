package view;

import view.theme.AppTheme;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

/**
 * Main application frame with professional styling and fullscreen support.
 */
public class MainFrame extends JFrame {
    // Graphics device used for fullscreen operations
    private GraphicsDevice device;
    
    // Main application frame constructor and initialization
    public MainFrame() {
        // Initialize theme before creating UI
        AppTheme.initialize();
        
        setTitle("Job Quest - Professional Career Simulation");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        

        // Try to enter fullscreen. If not supported, maximize the window.
        setUndecorated(true);
        device = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        if (device.isFullScreenSupported()) {
            device.setFullScreenWindow(this);
        } else {
            // Fallback to maximized if fullscreen not supported
            setExtendedState(JFrame.MAXIMIZED_BOTH);
            setSize(Toolkit.getDefaultToolkit().getScreenSize());
        }


        // Escape to exit, F11 to toggle fullscreen
        addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    handleExitRequest();
                }
            }
        });
        
        // Make sure frame can receive key events
        setFocusable(true);
        requestFocusInWindow();
        try {
            setIconImage(Toolkit.getDefaultToolkit().getImage(
                getClass().getResource("/images/icon.png")
            ));
        } catch (Exception e) {
            // Icon not found, continue without it
        }
        getContentPane().setBackground(AppTheme.BACKGROUND_COLOR);
        
        // Show login panel
        showPanel(new LoginPanel(this));
        
        setVisible(true);
    }
    
    // Show a panel with a smooth transition
    public void showPanel(JPanel panel) {
        // Fade out effect (simple version)
        SwingUtilities.invokeLater(() -> {
            setContentPane(panel);
            revalidate();
            repaint();
        });
    }
    
    // Handle user request to exit the game via Escape key
    private void handleExitRequest() {
        int confirm = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to exit the game?",
            "Exit Game",
            JOptionPane.YES_NO_OPTION
        );
        if (confirm == JOptionPane.YES_OPTION) {
            exitFullscreenAndShutdown();
        }
    }
    
    // A helper method to exit fullscreen and then shut down the application
    private void exitFullscreenAndShutdown() {
        if (device != null && device.getFullScreenWindow() == this) {
            device.setFullScreenWindow(null);
        }
        dispose();
        System.exit(0);
    }
    
}
