package view;

import controller.UserController;
import model.player.Vitals;
import utils.GameBalance;
import view.theme.AppTheme;

import javax.swing.*;

// Side stats: profile, progression, and identity bars.
// Owned by GamePanel.
public class UserStatsPanel extends JPanel {

    private final UserController userController;

    private JLabel usernameLabel, balanceLabel, healthLabel, energyLabel;
    private JLabel levelLabel, xpLabel, reputationLabel, jobBonusLabel;
    private JProgressBar xpProgressBar, healthBar, energyBar;

    public UserStatsPanel(UserController userController) {
        if (userController == null) {
            throw new IllegalArgumentException("userController must not be null");
        }
        this.userController = userController;
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(AppTheme.BACKGROUND_COLOR);

        JPanel profilePanel = AppTheme.createSectionPanel("Your Profile");
        usernameLabel = new JLabel("👤 " + userController.getUsername());
        usernameLabel.setFont(AppTheme.FONT_BODY);
        balanceLabel = new JLabel("💰 Balance: $" + userController.getBalance());
        balanceLabel.setFont(AppTheme.FONT_BODY);
        balanceLabel.setForeground(AppTheme.MONEY_COLOR.darker());
        healthLabel = new JLabel("❤️ Health: " + userController.getHealth() + "/" + Vitals.MAX_HEALTH);
        healthLabel.setFont(AppTheme.FONT_BODY);
        healthLabel.setForeground(AppTheme.HEALTH_COLOR);
        healthBar = new JProgressBar(0, Vitals.MAX_HEALTH);
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

        levelLabel = new JLabel("Level: " + userController.getLevel());
        levelLabel.setFont(AppTheme.FONT_BODY);
        xpLabel = new JLabel("XP: " + userController.getExperience() + "/" + userController.getRequiredExperienceForNextLevel());
        xpLabel.setFont(AppTheme.FONT_SMALL);
        xpLabel.setForeground(AppTheme.TEXT_SECONDARY);
        xpProgressBar = new JProgressBar(0, 100); // percent scale
        xpProgressBar.setValue(userController.getExperiencePercentage());
        xpProgressBar.setStringPainted(true);
        xpProgressBar.setString(userController.getExperiencePercentage() + "%");
        xpProgressBar.setForeground(AppTheme.PRIMARY_COLOR);
        xpProgressBar.setBackground(AppTheme.DIVIDER_COLOR);
        xpProgressBar.setFont(AppTheme.FONT_SMALL);

        reputationLabel = new JLabel("🏆 Rep: " + userController.getReputation() + " (" + utils.ReputationText.titleFor(userController.getReputation()) + ")");
        reputationLabel.setFont(AppTheme.FONT_BODY);
        jobBonusLabel = new JLabel("💰 Job Bonus: " + userController.getJobBonus());
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
        energyLabel = new JLabel("⚡ Energy: " + userController.getEnergy() + "/" + Vitals.MAX_ENERGY);
        energyLabel.setFont(AppTheme.FONT_BODY);
        energyLabel.setForeground(AppTheme.TEXT_SECONDARY);
        energyBar = new JProgressBar(0, Vitals.MAX_ENERGY);
        energyBar.setValue(userController.getEnergy());
        energyBar.setStringPainted(true);
        energyBar.setString((userController.getEnergy() * 100 / Vitals.MAX_ENERGY) + "%");
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
        healthLabel.setText("Health: " + hp + "/" + Vitals.MAX_HEALTH);
        int hpValue = hp;
        if (hpValue < 0) {
            hpValue = 0;
        }
        if (hpValue > Vitals.MAX_HEALTH) {
            hpValue = Vitals.MAX_HEALTH;
        }
        healthBar.setValue(hpValue);
        healthBar.setString(hp + "%");
        if (hp < GameBalance.LOW_HEALTH_THRESHOLD) {
            healthBar.setForeground(AppTheme.WARNING_COLOR);
        } else {
            healthBar.setForeground(AppTheme.HEALTH_COLOR);
        }
    }

    public void refreshEnergy() {
        int en = userController.getEnergy();
        energyLabel.setText("Energy: " + en + "/" + Vitals.MAX_ENERGY);
        int enValue = en;
        if (enValue < 0) {
            enValue = 0;
        }
        if (enValue > Vitals.MAX_ENERGY) {
            enValue = Vitals.MAX_ENERGY;
        }
        energyBar.setValue(enValue);
        energyBar.setString((en * 100 / Vitals.MAX_ENERGY) + "%");
        if (en < GameBalance.LOW_ENERGY_THRESHOLD) {
            energyBar.setForeground(AppTheme.WARNING_COLOR);
        } else {
            energyBar.setForeground(AppTheme.ENERGY_COLOR.darker());
        }
    }

    public void refreshProgress() {
        levelLabel.setText("Level: " + userController.getLevel());
        xpLabel.setText("XP: " + userController.getExperience() + "/" + userController.getRequiredExperienceForNextLevel());
        xpProgressBar.setValue(userController.getExperiencePercentage());
        xpProgressBar.setString(userController.getExperiencePercentage() + "%");
        reputationLabel.setText("🏆 Rep: " + userController.getReputation() + " (" + utils.ReputationText.titleFor(userController.getReputation()) + ")");
        jobBonusLabel.setText("💰 Job Bonus: " + userController.getJobBonus());
    }
}
