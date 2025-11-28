package view;

import controller.MessageSink;
import view.theme.AppTheme;

import javax.swing.*;
import java.awt.*;

// Chat + toast notifications at the bottom of the game screen.
// Owned by GamePanel.
public class ChatPanel extends JPanel implements MessageSink {

    private final JTextArea chatArea;
    // Reused toast popup for success messages.
    private JDialog toastDialog;
    private JLabel toastLabel;
    private javax.swing.Timer toastTimer;

    public ChatPanel() {
        super(new BorderLayout());
        chatArea = new JTextArea(5,50);
        chatArea.setEditable(false);
        chatArea.setFont(AppTheme.FONT_BODY);
        chatArea.setBackground(AppTheme.SURFACE_COLOR);
        chatArea.setForeground(AppTheme.TEXT_PRIMARY);
        chatArea.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(AppTheme.BORDER_COLOR, 1),
            BorderFactory.createEmptyBorder(6, 8, 6, 8)));
        JScrollPane chatScroll = new JScrollPane(chatArea);
        chatScroll.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(AppTheme.BORDER_COLOR, 1), "Chat / Notifications",
            javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.TOP,
            AppTheme.FONT_SUBHEADING, AppTheme.TEXT_PRIMARY));
        add(chatScroll, BorderLayout.CENTER);
    }

    // Auto-scrolls; success lines also show a toast so rewards are noticed.
    @Override
    public void addChatMessage(String msg) {
        chatArea.append(msg + "\n");
        chatArea.setCaretPosition(chatArea.getDocument().getLength());
        if (msg != null && (msg.contains("✅") || msg.contains("🏆") || msg.contains("🏅")
                || msg.contains("🎉") || msg.contains("✨") || msg.contains("🛒"))) {
            String firstLine = msg.split("\n")[0];
            if (firstLine.length() > 90) {
                firstLine = firstLine.substring(0, 87) + "...";
            }
            showToast(firstLine);
        }
    }

    /**
     * Non-blocking toast bottom-right of the game window, auto-hides.
     * Reuses one dialog instance (no window spam on rapid jobs).
     */
    private void showToast(String text) {
        Window owner = SwingUtilities.getWindowAncestor(this);
        if (owner == null) return;
        if (toastDialog == null) {
            toastDialog = new JDialog(owner);
            toastDialog.setUndecorated(true);
            toastDialog.setFocusableWindowState(false);
            toastLabel = new JLabel();
            toastLabel.setFont(AppTheme.FONT_BODY);
            toastLabel.setForeground(Color.WHITE);
            toastLabel.setBorder(BorderFactory.createEmptyBorder(10, 16, 10, 16));
            JPanel p = new JPanel(new BorderLayout());
            p.setBackground(new Color(50, 50, 50, 230));
            p.setBorder(BorderFactory.createLineBorder(new Color(0, 0, 0, 60), 1));
            p.add(toastLabel, BorderLayout.CENTER);
            toastDialog.getContentPane().add(p);
            toastDialog.pack();
        }
        toastLabel.setText(text);
        toastDialog.pack();
        Point ownerLoc = owner.getLocationOnScreen();
        int x = ownerLoc.x + owner.getWidth() - toastDialog.getWidth() - 24;
        int y = ownerLoc.y + owner.getHeight() - toastDialog.getHeight() - 70;
        toastDialog.setLocation(Math.max(ownerLoc.x, x), Math.max(ownerLoc.y, y));
        toastDialog.setVisible(true);
        if (toastTimer != null && toastTimer.isRunning()) {
            toastTimer.stop();
        }
        final javax.swing.JDialog myToast = toastDialog;
        toastTimer = new javax.swing.Timer(2500, new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                myToast.setVisible(false);
            }
        });
        toastTimer.setRepeats(false);
        toastTimer.start();
    }
}
