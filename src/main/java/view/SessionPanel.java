package view;

import view.theme.AppTheme;

import javax.swing.*;
import java.awt.*;

// Side session controls: help, new game, exit.
// Shares its exit action with the bottom bar.
public class SessionPanel extends JPanel {

    private final Action exitAction;

    public SessionPanel() {
        super();
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        setBackground(AppTheme.BACKGROUND_COLOR);

        JButton helpButton = AppTheme.createSecondaryButton("📖 Help & About");
        helpButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        helpButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                AboutDialog aboutDialog = new AboutDialog(
                        (JFrame) SwingUtilities.getWindowAncestor(SessionPanel.this));
                aboutDialog.setVisible(true);
            }
        });

        // No save - in-memory only. Exit closes, New Game goes back to career pick.
        exitAction = new AbstractAction("❌ Exit") {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                System.exit(0);
            }
        };
        JButton exitButton = AppTheme.createDangerButton("❌ Exit");
        exitButton.setAction(exitAction);
        exitButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton newGameButton = AppTheme.createSecondaryButton("🔄 New Game");
        newGameButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        newGameButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(SessionPanel.this);
                if (frame instanceof MainFrame) {
                    ((MainFrame) frame).showPanel(new StartPanel((MainFrame) frame));
                }
            }
        });

        add(helpButton);
        add(Box.createVerticalStrut(5));
        add(newGameButton);
        add(Box.createVerticalStrut(5));
        add(exitButton);
    }

    public Action getExitAction() {
        return exitAction;
    }
}
