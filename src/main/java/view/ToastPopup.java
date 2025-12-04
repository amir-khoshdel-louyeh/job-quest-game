package view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Point;
import java.awt.Window;
import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import java.awt.BorderLayout;

import view.theme.AppTheme;

// Reusable bottom-right popup for important chat lines.
// Decides which messages deserve a toast; only ChatPanel uses it.
final class ToastPopup {
    private static final int HIDE_DELAY_MILLIS = 2500;
    private static final int MAX_LINE_LENGTH = 90;
    private static final String[] IMPORTANT_MARKERS = {
            "✅", "🏆", "🏅", "🎉", "✨", "🛒"
    };

    private JDialog dialog;
    private JLabel label;
    private Timer timer;

    void showFor(Component anchor, String message) {
        if (!isImportant(message)) {
            return;
        }
        show(anchor, firstLine(message));
    }

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
