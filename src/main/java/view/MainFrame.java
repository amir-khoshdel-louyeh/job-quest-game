package view;

import view.theme.AppTheme;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Main application frame with professional styling and fullscreen support.
 */
public class MainFrame extends JFrame {
    private GraphicsDevice graphicsDevice;
    
    public MainFrame() {
        // Initialize theme before creating UI
        AppTheme.initialize();
        
        setTitle("Job Quest - Professional Career Simulation");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // Remove window decorations for true fullscreen
        setUndecorated(true);
        
        // Get screen dimensions
        graphicsDevice = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        
        // Set fullscreen mode
        if (graphicsDevice.isFullScreenSupported()) {
            graphicsDevice.setFullScreenWindow(this);
        } else {
            // Fallback to maximized if fullscreen not supported
            setExtendedState(JFrame.MAXIMIZED_BOTH);
            setSize(Toolkit.getDefaultToolkit().getScreenSize());
        }
        
        // Add ESC key to exit fullscreen (F11 to toggle)
        addKeyListener(new KeyListener() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    int confirm = JOptionPane.showConfirmDialog(
                        MainFrame.this,
                        "Exit fullscreen and close the game?",
                        "Exit Game",
                        JOptionPane.YES_NO_OPTION
                    );
                    if (confirm == JOptionPane.YES_OPTION) {
                        MainFrame.this.exitFullscreen();
                        System.exit(0);
                    }
                } else if (e.getKeyCode() == KeyEvent.VK_F11) {
                    MainFrame.this.toggleFullscreen();
                }
            }
            
            @Override
            public void keyReleased(KeyEvent e) {}
            
            @Override
            public void keyTyped(KeyEvent e) {}
        });
        
        // Make sure frame can receive key events
        setFocusable(true);
        requestFocusInWindow();
        
        // Set application icon (if available)
        try {
            setIconImage(Toolkit.getDefaultToolkit().getImage(
                getClass().getResource("/images/icon.png")
            ));
        } catch (Exception e) {
            // Icon not found, continue without it
        }
        
        // Set background color
        getContentPane().setBackground(AppTheme.BACKGROUND_COLOR);
        
        // Show login panel
        showPanel(new LoginPanel(this));
        
        setVisible(true);
    }
    
    /** نمایش یک پنل با جابجایی نرم */
    public void showPanel(JPanel panel) {
        // Fade out effect (simple version)
        SwingUtilities.invokeLater(() -> {
            setContentPane(panel);
            revalidate();
            repaint();
        });
    }
    
    /** خروج از حالت تمام‌صفحه */
    private void exitFullscreen() {
        if (graphicsDevice != null && graphicsDevice.getFullScreenWindow() == this) {
            graphicsDevice.setFullScreenWindow(null);
        }
        dispose();
    }
    
    /** تغییر وضعیت تمام‌صفحه */
    private void toggleFullscreen() {
        if (graphicsDevice != null && graphicsDevice.isFullScreenSupported()) {
            if (graphicsDevice.getFullScreenWindow() == this) {
                // Exit fullscreen
                graphicsDevice.setFullScreenWindow(null);
                setUndecorated(false);
                setExtendedState(JFrame.MAXIMIZED_BOTH);
            } else {
                // Enter fullscreen
                dispose();
                setUndecorated(true);
                setVisible(true);
                graphicsDevice.setFullScreenWindow(this);
            }
        }
    }
}
