package view;

import java.awt.*;
import javax.swing.*;

import view.theme.AppTheme;

// Reusable bottom-right popup for important chat lines.
// ChatPanel sends every message here; this class decides what deserves a toast.
final class NotificationToast {
    private static final int HIDE_DELAY_MILLIS = 2500;
    private static final int MAX_LINE_LENGTH = 90;
    private static final String[] IMPORTANT_MARKERS = {
            "✅", "🏆", "🏅", "🎉", "✨", "🛒"
    };

    private JDialog dialog;
    private JLabel label;
    private Timer timer;

    // ----- Commands -----
    void showFor(Component anchor, String message) {
        if (!isImportant(message)) {
            return;
        }
        show(anchor, firstLine(message));
    }

    // ----- Private helpers -----
    private static boolean isImportant(String message) {
        if (message == null) {
            return false;
        }
        for (String marker : IMPORTANT_MARKERS) {
            if (message.contains(marker)) {
                return true;
            }
        }
        return false;
    }

    private static String firstLine(String message) {
        int newline = message.indexOf('\n');
        String line = newline < 0 ? message : message.substring(0, newline);
        if (line.length() > MAX_LINE_LENGTH) {
            line = line.substring(0, MAX_LINE_LENGTH - 3) + "...";
        }
        return line;
    }

    private void show(Component anchor, String text) {
        Window owner = SwingUtilities.getWindowAncestor(anchor);
        if (owner == null) {
            return;
        }
        ensureDialog(owner);
        label.setText(text);
        dialog.pack();

        Point ownerLocation = owner.getLocationOnScreen();
        int x = ownerLocation.x + owner.getWidth() - dialog.getWidth() - 24;
        int y = ownerLocation.y + owner.getHeight() - dialog.getHeight() - 70;
        dialog.setLocation(Math.max(ownerLocation.x, x), Math.max(ownerLocation.y, y));
        dialog.setVisible(true);

        if (timer != null && timer.isRunning()) {
            timer.stop();
        }
        timer = new Timer(HIDE_DELAY_MILLIS, event -> dialog.setVisible(false));
        timer.setRepeats(false);
        timer.start();
    }

    private void ensureDialog(Window owner) {
        if (dialog != null) {
            return;
        }
        dialog = new JDialog(owner);
        dialog.setUndecorated(true);
        dialog.setFocusableWindowState(false);
        label = new JLabel();
        label.setFont(AppTheme.FONT_BODY);
        label.setForeground(Color.WHITE);
        label.setBorder(BorderFactory.createEmptyBorder(10, 16, 10, 16));
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(50, 50, 50, 230));
        panel.setBorder(BorderFactory.createLineBorder(new Color(0, 0, 0, 60), 1));
        panel.add(label, BorderLayout.CENTER);
        dialog.getContentPane().add(panel);
        dialog.pack();
    }
}
