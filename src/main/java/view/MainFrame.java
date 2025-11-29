package view;

import view.theme.AppTheme;
import provider.IdentityProvider;
import javax.swing.*;
import java.awt.*;

// The app window: starts maximized with standard exit button (X).
public class MainFrame extends JFrame {
    // One shared read-only catalog for every start screen in this window.
    private final IdentityProvider identityOptions = new IdentityProvider();
    
    public MainFrame() {
        AppTheme.initialize();
        
        setTitle("Job Quest - Professional Career Simulation");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        

        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setSize(Toolkit.getDefaultToolkit().getScreenSize());

        
        setFocusable(true);
        requestFocusInWindow();
        // Icon is cosmetic: skip it silently when the resource is absent.
        java.net.URL iconUrl = getClass().getResource("/images/icon.png");
        if (iconUrl != null) {
            setIconImage(Toolkit.getDefaultToolkit().getImage(iconUrl));
        }
        getContentPane().setBackground(AppTheme.BACKGROUND_COLOR);
        
        // Show start panel - pick a career, no login needed
        showStartPanel();
        
        setVisible(true);
    }

    // Builds the start screen with the shared career catalog.
    public void showStartPanel() {
        showPanel(new StartPanel(this, identityOptions));
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

    
}
