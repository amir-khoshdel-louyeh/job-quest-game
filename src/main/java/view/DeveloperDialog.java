package view;

import controller.DeveloperController;
import model.User;
import view.theme.AppTheme;

import javax.swing.*;
import java.awt.*;

/**
 * Developer demo dialog: changeable variables for live presentations and
 * manual testing (balance, health, energy, level, reputation, playtime,
 * streak) plus one-click shortcuts.
 *
 * Pure view (SRP): all mutations go through {@link DeveloperController},
 * which re-syncs observers so the HUD updates live.
 */
public class DeveloperDialog extends JDialog {

    private final DeveloperController devController;
    private final GamePanel gamePanel;

    private JTextField balanceField;
    private JTextField healthField;
    private JTextField energyField;
    private JTextField levelField;
    private JTextField reputationField;
    private JTextField playtimeField;
    private JTextField streakField;

    // Dialog with editable demo variables for the current user
    public DeveloperDialog(JFrame parent, DeveloperController devController, GamePanel gamePanel) {
        super(parent, "🛠 Developer Controls", true);
        this.devController = devController;
        this.gamePanel = gamePanel;
        setLayout(new BorderLayout(10, 10));
        AppTheme.styleDialog(this, 420, 560);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JLabel hint = new JLabel("Demo only — changes apply instantly to this session.",
                SwingConstants.CENTER);
        hint.setFont(AppTheme.FONT_SMALL);
        hint.setForeground(AppTheme.TEXT_SECONDARY);
        hint.setBorder(BorderFactory.createEmptyBorder(8, 10, 0, 10));
        add(hint, BorderLayout.NORTH);

        JPanel form = new JPanel(new GridLayout(0, 2, 8, 8));
        form.setBackground(AppTheme.BACKGROUND_COLOR);
        form.setBorder(BorderFactory.createEmptyBorder(10, 14, 10, 14));

        balanceField = row(form, "💰 Balance ($):");
        healthField = row(form, "❤️ Health (0-100):");
        energyField = row(form, "⚡ Energy (0-100k):");
        levelField = row(form, "⭐ Level (1-100):");
        reputationField = row(form, "🏆 Reputation (0-100):");
        playtimeField = row(form, "⏱ Playtime (min):");
        streakField = row(form, "🔥 Streak (days):");

        refreshFields();
        add(form, BorderLayout.CENTER);

        JPanel bottom = new JPanel();
        bottom.setLayout(new BoxLayout(bottom, BoxLayout.Y_AXIS));
        bottom.setBackground(AppTheme.BACKGROUND_COLOR);
        bottom.setBorder(BorderFactory.createEmptyBorder(0, 14, 14, 14));

        JPanel quickRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 6, 4));
        quickRow.setBackground(AppTheme.BACKGROUND_COLOR);
        quickRow.add(quickButton("❤️ Full HP", () -> devController.setHealth(100)));
        quickRow.add(quickButton("⚡ Full EN", () -> devController.setEnergy(100_000)));
        quickRow.add(quickButton("+$5k", () -> devController.setBalance(
                devController.getUser().getBalance() + 5000)));
        quickRow.add(quickButton("+60 min", () -> devController.addPlaytimeMinutes(60)));
        quickRow.add(quickButton("🔓 Unblock", devController::unblock));
        bottom.add(quickRow);

        JButton applyButton = AppTheme.createSuccessButton("✔ Apply Values");
        applyButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        applyButton.addActionListener(e -> applyValues());
        bottom.add(Box.createVerticalStrut(6));
        bottom.add(applyButton);

        add(bottom, BorderLayout.SOUTH);
    }

    // One label + text field row; returns the field for later read/write.
    private JTextField row(JPanel form, String labelText) {
        JLabel label = new JLabel(labelText);
        label.setFont(AppTheme.FONT_BODY);
        label.setForeground(AppTheme.TEXT_PRIMARY);
        JTextField field = AppTheme.createTextField(10);
        form.add(label);
        form.add(field);
        return field;
    }

    private JButton quickButton(String text, Runnable action) {
        JButton button = AppTheme.createSecondaryButton(text);
        button.addActionListener(e -> {
            action.run();
            refreshFields();
            gamePanel.addChatMessage("🛠 Dev: " + text + " applied.");
            gamePanel.updateUserInfo();
        });
        return button;
    }

    // Reload fields from the current user state.
    private void refreshFields() {
        User user = devController.getUser();
        balanceField.setText(String.valueOf(user.getBalance()));
        healthField.setText(String.valueOf(user.getHealth()));
        energyField.setText(String.valueOf(user.getEnergy()));
        levelField.setText(String.valueOf(user.getLevel()));
        reputationField.setText(String.valueOf(user.getReputation()));
        playtimeField.setText(String.valueOf(user.getTotalPlayTime() / (1000 * 60)));
        streakField.setText(String.valueOf(user.getCurrentStreak()));
    }

    // Parse fields (invalid input keeps the current value) and apply.
    private void applyValues() {
        User user = devController.getUser();
        devController.setBalance(parse(balanceField, user.getBalance()));
        devController.setHealth(parse(healthField, user.getHealth()));
        devController.setEnergy(parse(energyField, user.getEnergy()));
        devController.setLevel(parse(levelField, user.getLevel()));
        devController.setReputation(parse(reputationField, user.getReputation()));
        long currentMinutes = user.getTotalPlayTime() / (1000 * 60);
        long wantedMinutes = parse(playtimeField, (int) Math.min(currentMinutes, Integer.MAX_VALUE));
        devController.addPlaytimeMinutes(wantedMinutes - currentMinutes);
        devController.setStreak(parse(streakField, user.getCurrentStreak()));
        refreshFields();
        gamePanel.addChatMessage("🛠 Dev: values applied.");
        gamePanel.updateUserInfo();
    }

    private static int parse(JTextField field, int fallback) {
        try {
            return Integer.parseInt(field.getText().trim());
        } catch (NumberFormatException ex) {
            field.setText(String.valueOf(fallback));
            return fallback;
        }
    }
}
