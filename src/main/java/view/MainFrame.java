package view;

import controller.GameStarter;
import provider.IdentityCatalog;
import view.minigame.MiniGameLauncher;
import view.theme.AppTheme;
import javax.swing.*;
import java.awt.*;

// The app window: starts maximized with standard exit button (X).
public class MainFrame extends JFrame {
    // All three collaborators are contracts; main.Main wires the concretes.
    private final IdentityCatalog identityOptions;
    private final GameStarter gameStarter;
    private final MiniGameLauncher workGame;

    public MainFrame(IdentityCatalog identityOptions, GameStarter gameStarter,
                     MiniGameLauncher workGame) {
        this.identityOptions = utils.Require.notNull(identityOptions, "identityOptions");
        this.gameStarter = utils.Require.notNull(gameStarter, "gameStarter");
        this.workGame = utils.Require.notNull(workGame, "workGame");
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
        showPanel(new StartPanel(this, identityOptions, gameStarter, workGame));
    }
    
    public void showPanel(JPanel panel) {
        SwingUtilities.invokeLater(() -> {
            setContentPane(panel);
            revalidate();
            repaint();
        });
    }

    
}
