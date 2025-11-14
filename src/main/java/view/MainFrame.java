package view;

import view.theme.AppTheme;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

// The app window: starts fullscreen, F11 toggles windowed, ESC asks to exit.
public class MainFrame extends JFrame {
    private GraphicsDevice device;
    
    public MainFrame() {
        AppTheme.initialize();
        
        setTitle("Job Quest - Professional Career Simulation");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        

        // Try to enter fullscreen. If not supported, maximize the window.
        setUndecorated(true);
        device = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        if (device.isFullScreenSupported()) {
            device.setFullScreenWindow(this);
        } else {
            setExtendedState(JFrame.MAXIMIZED_BOTH);
            setSize(Toolkit.getDefaultToolkit().getScreenSize());
        }


        // Escape to exit, F11 to toggle fullscreen
        addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    handleExitRequest();
                } else if (e.getKeyCode() == KeyEvent.VK_F11) {
                    toggleFullscreen();
                }
            }
        });
        
        setFocusable(true);
        requestFocusInWindow();
        try {
            setIconImage(Toolkit.getDefaultToolkit().getImage(
                getClass().getResource("/images/icon.png")
            ));
        } catch (Exception e) {
        }
        getContentPane().setBackground(AppTheme.BACKGROUND_COLOR);
        
        // Show start panel - pick a career, no login needed
        showPanel(new StartPanel(this));
        
        setVisible(true);
    }
    
    public void showPanel(JPanel panel) {
        final JPanel myPanel = panel;
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                setContentPane(myPanel);
                revalidate();
                repaint();
            }
        });
    }
    
    private void handleExitRequest() {
        int confirm = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to exit the game?\n(Hint: F11 toggles windowed mode)",
            "Exit Game",
            JOptionPane.YES_NO_OPTION
        );
        if (confirm == JOptionPane.YES_OPTION) {
            exitFullscreenAndShutdown();
        }
    }

    // Toggle fullscreen; switching window style needs dispose and show again.
    private void toggleFullscreen() {
        boolean isFullscreen = (device != null && device.getFullScreenWindow() == this);
        dispose();
        if (isFullscreen) {
            device.setFullScreenWindow(null);
            setUndecorated(false);
            setSize(1280, 800);
            setLocationRelativeTo(null);
            setExtendedState(JFrame.NORMAL);
        } else {
            setUndecorated(true);
            if (device != null && device.isFullScreenSupported()) {
                device.setFullScreenWindow(this);
            } else {
                setExtendedState(JFrame.MAXIMIZED_BOTH);
            }
        }
        setVisible(true);
        requestFocusInWindow();
    }
    
    private void exitFullscreenAndShutdown() {
        if (device != null && device.getFullScreenWindow() == this) {
            device.setFullScreenWindow(null);
        }
        dispose();
        System.exit(0);
    }
    
}
