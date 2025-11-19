package view;

import controller.UserController;
import model.player.User;
import view.theme.AppTheme;

import javax.swing.*;
import java.awt.*;

// Profile + progression + identity stats on the side of the game screen.
// Extracted from GamePanel (SRP): this owns all stat labels/bars and the
// refresh logic; GamePanel only keeps a delegate so observers don't change.
public class UserStatsPanel extends JPanel {

    private final UserController userController;
    private final User user;

    private JLabel usernameLabel, balanceLabel, healthLabel, energyLabel;
    private JLabel levelLabel, xpLabel, reputationLabel, jobBonusLabel;
    private JProgressBar xpProgressBar, healthBar, energyBar;

    public UserStatsPanel(UserController userController, User user) {
        if (userController == null) {
            throw new IllegalArgumentException("userController must not be null");
        }
        if (user == null) {
            throw new IllegalArgumentException("user must not be null");
        }
        this.userController = userController;
        this.user = user;
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(AppTheme.BACKGROUND_COLOR);

        JPanel profilePanel = AppTheme.createSectionPanel("Your Profile");
        usernameLabel = new JLabel("👤 " + userController.getUsername());
        usernameLabel.setFont(AppTheme.FONT_BODY);
        balanceLabel = new JLabel("💰 Balance: $" + userController.getBalance());
        balanceLabel.setFont(AppTheme.FONT_BODY);
        balanceLabel.setForeground(AppTheme.MONEY_COLOR.darker());
        healthLabel = new JLabel("❤️ Health: " + userController.getHealth() + "/100");
        healthLabel.setFont(AppTheme.FONT_BODY);
        healthLabel.setForeground(AppTheme.HEALTH_COLOR);
        healthBar = new JProgressBar(0, 100);
        healthBar.setValue(userController.getHealth());
        healthBar.setStringPainted(true);
        healthBar.setString(userController.getHealth() + "%");
        healthBar.setForeground(AppTheme.HEALTH_COLOR);
        healthBar.setBackground(AppTheme.DIVIDER_COLOR);
        healthBar.setFont(AppTheme.FONT_SMALL);
        profilePanel.add(usernameLabel);
        profilePanel.add(balanceLabel);
        profilePanel.add(healthLabel);
        profilePanel.add(healthBar);

        JPanel progressPanel = AppTheme.createSectionPanel("⭐ Progression");

        levelLabel = new JLabel("Level: " + user.getLevel());
        levelLabel.setFont(AppTheme.FONT_BODY);
        xpLabel = new JLabel("XP: " + user.getExperience() + "/" + user.getRequiredExperienceForNextLevel());
        xpLabel.setFont(AppTheme.FONT_SMALL);
        xpLabel.setForeground(AppTheme.TEXT_SECONDARY);
        xpProgressBar = new JProgressBar(0, 100);
        xpProgressBar.setValue(user.getExperiencePercentage());
        xpProgressBar.setStringPainted(true);
        xpProgressBar.setString(user.getExperiencePercentage() + "%");
        xpProgressBar.setForeground(AppTheme.PRIMARY_COLOR);
        xpProgressBar.setBackground(AppTheme.DIVIDER_COLOR);
        xpProgressBar.setFont(AppTheme.FONT_SMALL);

        reputationLabel = new JLabel("🏆 Rep: " + user.getReputation() + " (" + utils.ReputationText.titleFor(user.getReputation()) + ")");
        reputationLabel.setFont(AppTheme.FONT_BODY);
        jobBonusLabel = new JLabel("💰 Job Bonus: " + user.getJobBonus());
        jobBonusLabel.setFont(AppTheme.FONT_BODY);
        jobBonusLabel.setForeground(AppTheme.WARNING_COLOR.darker());

        progressPanel.add(levelLabel);
        progressPanel.add(xpLabel);
        progressPanel.add(xpProgressBar);
        progressPanel.add(Box.createVerticalStrut(5));
        progressPanel.add(reputationLabel);
        progressPanel.add(jobBonusLabel);

        JPanel identityPanel = AppTheme.createSectionPanel("Current Identity");
        JLabel identityName = new JLabel("💼 " + userController.getIdentityName());
        identityName.setFont(AppTheme.FONT_BODY);
        energyLabel = new JLabel("⚡ Energy: " + userController.getEnergy() + "/100000");
        energyLabel.setFont(AppTheme.FONT_BODY);
        energyLabel.setForeground(AppTheme.TEXT_SECONDARY);
        energyBar = new JProgressBar(0, 100000);
        energyBar.setValue(userController.getEnergy());
        energyBar.setStringPainted(true);
        energyBar.setString((userController.getEnergy() * 100 / 100000) + "%");
        energyBar.setForeground(AppTheme.ENERGY_COLOR.darker());
        energyBar.setBackground(AppTheme.DIVIDER_COLOR);
        energyBar.setFont(AppTheme.FONT_SMALL);
        identityPanel.add(identityName);
        identityPanel.add(energyLabel);
        identityPanel.add(energyBar);

        add(profilePanel);
        add(Box.createVerticalStrut(10));
        add(progressPanel);
        add(Box.createVerticalStrut(10));
        add(identityPanel);
    }

    public void refresh() {
        refreshBalance();
        refreshHealth();
        refreshEnergy();
        refreshProgress();
    }

    public void refreshBalance() {
        usernameLabel.setText("User: " + userController.getUsername());
        balanceLabel.setText("Balance: $" + userController.getBalance());
    }

    public void refreshHealth() {
        int hp = userController.getHealth();
        healthLabel.setText("Health: " + hp + "/100");
        int hpValue = hp;
        if (hpValue < 0) {
            hpValue = 0;
        }
        if (hpValue > 100) {
            hpValue = 100;
        }
        healthBar.setValue(hpValue);
        healthBar.setString(hp + "%");
        if (hp < 30) {
            healthBar.setForeground(AppTheme.WARNING_COLOR);
        } else {
            healthBar.setForeground(AppTheme.HEALTH_COLOR);
        }
    }

    public void refreshEnergy() {
        int en = userController.getEnergy();
        energyLabel.setText("Energy: " + en + "/100000");
        int enValue = en;
        if (enValue < 0) {
            enValue = 0;
        }
        if (enValue > 100000) {
            enValue = 100000;
        }
        energyBar.setValue(enValue);
        energyBar.setString((en * 100 / 100000) + "%");
        if (en < 25000) {
            energyBar.setForeground(AppTheme.WARNING_COLOR);
        } else {
            energyBar.setForeground(AppTheme.ENERGY_COLOR.darker());
        }
    }

    public void refreshProgress() {
        levelLabel.setText("Level: " + user.getLevel());
        xpLabel.setText("XP: " + user.getExperience() + "/" + user.getRequiredExperienceForNextLevel());
        xpProgressBar.setValue(user.getExperiencePercentage());
        xpProgressBar.setString(user.getExperiencePercentage() + "%");
        reputationLabel.setText("🏆 Rep: " + user.getReputation() + " (" + utils.ReputationText.titleFor(user.getReputation()) + ")");
        jobBonusLabel.setText("💰 Job Bonus: " + user.getJobBonus());
    }
}
