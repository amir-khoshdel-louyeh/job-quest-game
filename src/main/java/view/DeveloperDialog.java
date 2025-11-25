package view;

import controller.DeveloperController;
import view.theme.AppTheme;

import javax.swing.*;
import java.awt.*;

// The developer screen: change money, health, and other stats quickly for demos.
public class DeveloperDialog extends JDialog {

    private DeveloperController devController;
    private GamePanel gamePanel;

    private JTextField balanceField;
    private JTextField healthField;
    private JTextField energyField;
    private JTextField levelField;
    private JTextField reputationField;
    private JTextField playtimeField;
    private JTextField jobBonusField;

    public DeveloperDialog(JFrame parent, DeveloperController devController, GamePanel gamePanel) {
        super(parent, "Developer Controls", true);
        this.devController = devController;
        this.gamePanel = gamePanel;
        setLayout(new BorderLayout(10, 10));
        AppTheme.styleDialog(this, 420, 560);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JLabel hint = new JLabel("Demo only — changes apply instantly.", SwingConstants.CENTER);
        hint.setFont(AppTheme.FONT_SMALL);
        hint.setForeground(AppTheme.TEXT_SECONDARY);
        hint.setBorder(BorderFactory.createEmptyBorder(8, 10, 0, 10));
        add(hint, BorderLayout.NORTH);

        JPanel form = new JPanel(new GridLayout(0, 2, 8, 8));
        form.setBackground(AppTheme.BACKGROUND_COLOR);
        form.setBorder(BorderFactory.createEmptyBorder(10, 14, 10, 14));

        balanceField = row(form, "Balance ($):");
        healthField = row(form, "Health (0-100):");
        energyField = row(form, "Energy (0-100k):");
        levelField = row(form, "Level (1-100):");
        reputationField = row(form, "Reputation (0-" + model.player.CareerProgress.MAX_REPUTATION + "):");
        playtimeField = row(form, "Playtime (min):");
        jobBonusField = row(form, "Job Bonus:");

        refreshFields();
        add(form, BorderLayout.CENTER);

        JPanel bottom = new JPanel();
        bottom.setLayout(new BoxLayout(bottom, BoxLayout.Y_AXIS));
        bottom.setBackground(AppTheme.BACKGROUND_COLOR);
        bottom.setBorder(BorderFactory.createEmptyBorder(0, 14, 14, 14));

        JPanel quickRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 6, 4));
        quickRow.setBackground(AppTheme.BACKGROUND_COLOR);
        quickRow.add(quickButton("Full HP", new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                devController.setHealth(100);
                afterQuick("Full HP");
            }
        }));
        quickRow.add(quickButton("Full EN", new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                devController.setEnergy(100000);
                afterQuick("Full EN");
            }
        }));
        quickRow.add(quickButton("+$5k", new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                int now = devController.getBalance();
                devController.setBalance(now + 5000);
                afterQuick("+$5k");
            }
        }));
        quickRow.add(quickButton("+60 min", new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                devController.addPlaytimeMinutes(60);
                afterQuick("+60 min");
            }
        }));
        quickRow.add(quickButton("Unblock", new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                devController.unblock();
                afterQuick("Unblock");
            }
        }));
        bottom.add(quickRow);

        JButton applyButton = AppTheme.createSuccessButton("Apply Values");
        applyButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        applyButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                applyValues();
            }
        });
        bottom.add(Box.createVerticalStrut(6));
        bottom.add(applyButton);

        add(bottom, BorderLayout.SOUTH);
    }

    private JTextField row(JPanel form, String labelText) {
        JLabel label = new JLabel(labelText);
        label.setFont(AppTheme.FONT_BODY);
        label.setForeground(AppTheme.TEXT_PRIMARY);
        JTextField field = AppTheme.createTextField(10);
        form.add(label);
        form.add(field);
        return field;
    }

    private JButton quickButton(String text, java.awt.event.ActionListener action) {
        JButton button = AppTheme.createSecondaryButton(text);
        button.addActionListener(action);
        return button;
    }

    private void afterQuick(String text) {
        refreshFields();
        gamePanel.addChatMessage("Dev: " + text + " applied.");
        gamePanel.updateUserInfo();
    }

    private void refreshFields() {
        balanceField.setText(String.valueOf(devController.getBalance()));
        healthField.setText(String.valueOf(devController.getHealth()));
        energyField.setText(String.valueOf(devController.getEnergy()));
        levelField.setText(String.valueOf(devController.getLevel()));
        reputationField.setText(String.valueOf(devController.getReputation()));
        playtimeField.setText(String.valueOf(devController.getTotalPlayTime() / (1000 * 60)));
        jobBonusField.setText(String.valueOf(devController.getJobBonus()));
    }

    private void applyValues() {
        devController.setBalance(parse(balanceField, devController.getBalance()));
        devController.setHealth(parse(healthField, devController.getHealth()));
        devController.setEnergy(parse(energyField, devController.getEnergy()));
        devController.setLevel(parse(levelField, devController.getLevel()));
        devController.setReputation(parse(reputationField, devController.getReputation()));
        long currentMinutes = devController.getTotalPlayTime() / (1000 * 60);
        int currentInt = (int) currentMinutes;
        // Keep inside int range.
        if (currentMinutes > Integer.MAX_VALUE) {
            currentInt = Integer.MAX_VALUE;
        }
        long wanted = parse(playtimeField, currentInt);
        devController.addPlaytimeMinutes(wanted - currentMinutes);
        devController.setJobBonus(parse(jobBonusField, devController.getJobBonus()));
        refreshFields();
        gamePanel.addChatMessage("Dev: values applied.");
        gamePanel.updateUserInfo();
    }

    // Multi-catch: only the two failures a parse can honestly produce
    // (bad digits -> NumberFormatException, missing text -> NullPointerException).
    // A broad catch (Exception) would also swallow real bugs; the fallback
    // restores the field so the dialog never shows a broken value.
    private static int parse(JTextField field, int fallback) {
        if (field == null) {
            return fallback;
        }
        try {
            return Integer.parseInt(field.getText().trim());
        } catch (NumberFormatException | NullPointerException ex) {
            field.setText(String.valueOf(fallback));
            return fallback;
        }
    }
}
