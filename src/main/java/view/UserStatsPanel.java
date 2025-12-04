package view;

import model.player.PlayerView;
import model.player.Vitals;
import utils.GameBalance;
import utils.ReputationText;
import utils.Require;
import view.theme.AppTheme;

import javax.swing.*;

// Side stats: profile, progression, and identity bars.
// Owned by GamePanel; reads only through PlayerView.
public class UserStatsPanel extends JPanel {

    private final PlayerView player;

    private JLabel usernameLabel, balanceLabel, healthLabel, energyLabel;
    private JLabel levelLabel, xpLabel, reputationLabel, jobBonusLabel;
    private JProgressBar xpProgressBar, healthBar, energyBar;

    UserStatsPanel(PlayerView player) {
        this.player = Require.notNull(player, "player");
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(AppTheme.BACKGROUND_COLOR);

        JPanel profilePanel = AppTheme.createSectionPanel("Your Profile");
        usernameLabel = new JLabel();
        usernameLabel.setFont(AppTheme.FONT_BODY);
        balanceLabel = new JLabel();
        balanceLabel.setFont(AppTheme.FONT_BODY);
        balanceLabel.setForeground(AppTheme.MONEY_COLOR.darker());
        healthLabel = new JLabel();
        healthLabel.setFont(AppTheme.FONT_BODY);
        healthLabel.setForeground(AppTheme.HEALTH_COLOR);
        healthBar = new JProgressBar(0, Vitals.MAX_HEALTH);
        healthBar.setStringPainted(true);
        healthBar.setBackground(AppTheme.DIVIDER_COLOR);
        healthBar.setFont(AppTheme.FONT_SMALL);
        profilePanel.add(usernameLabel);
        profilePanel.add(balanceLabel);
        profilePanel.add(healthLabel);
        profilePanel.add(healthBar);

        JPanel progressPanel = AppTheme.createSectionPanel("⭐ Progression");

        levelLabel = new JLabel();
        levelLabel.setFont(AppTheme.FONT_BODY);
        xpLabel = new JLabel();
        xpLabel.setFont(AppTheme.FONT_SMALL);
        xpLabel.setForeground(AppTheme.TEXT_SECONDARY);
        xpProgressBar = new JProgressBar(0, 100); // percent scale
        xpProgressBar.setStringPainted(true);
        xpProgressBar.setForeground(AppTheme.PRIMARY_COLOR);
        xpProgressBar.setBackground(AppTheme.DIVIDER_COLOR);
        xpProgressBar.setFont(AppTheme.FONT_SMALL);

        reputationLabel = new JLabel();
        reputationLabel.setFont(AppTheme.FONT_BODY);
        jobBonusLabel = new JLabel();
        jobBonusLabel.setFont(AppTheme.FONT_BODY);
        jobBonusLabel.setForeground(AppTheme.WARNING_COLOR.darker());

        progressPanel.add(levelLabel);
        progressPanel.add(xpLabel);
        progressPanel.add(xpProgressBar);
        progressPanel.add(Box.createVerticalStrut(5));
        progressPanel.add(reputationLabel);
        progressPanel.add(jobBonusLabel);

        JPanel identityPanel = AppTheme.createSectionPanel("Current Identity");
        JLabel identityName = new JLabel("💼 " + player.getIdentityName());
        identityName.setFont(AppTheme.FONT_BODY);
        energyLabel = new JLabel();
        energyLabel.setFont(AppTheme.FONT_BODY);
        energyLabel.setForeground(AppTheme.TEXT_SECONDARY);
        energyBar = new JProgressBar(0, Vitals.MAX_ENERGY);
        energyBar.setStringPainted(true);
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

        refresh();
    }

    // ----- Rendering -----
    public void refresh() {
        refreshBalance();
        refreshHealth();
        refreshEnergy();
        refreshProgress();
    }

    public void refreshBalance() {
        usernameLabel.setText("👤 " + player.getUsername());
        balanceLabel.setText("💰 Balance: $" + player.getBalance());
    }

    public void refreshHealth() {
        int health = player.getHealth();
        healthLabel.setText("❤️ Health: " + health + "/" + Vitals.MAX_HEALTH);
        healthBar.setValue(health);
        healthBar.setString(player.getHealthPercentage() + "%");
        healthBar.setForeground(health < GameBalance.LOW_HEALTH_THRESHOLD
                ? AppTheme.WARNING_COLOR : AppTheme.HEALTH_COLOR);
    }

    public void refreshEnergy() {
        int energy = player.getEnergy();
        energyLabel.setText("⚡ Energy: " + energy + "/" + Vitals.MAX_ENERGY);
        energyBar.setValue(energy);
        energyBar.setString(player.getEnergyPercentage() + "%");
        energyBar.setForeground(energy < GameBalance.LOW_ENERGY_THRESHOLD
                ? AppTheme.WARNING_COLOR : AppTheme.ENERGY_COLOR.darker());
    }

    public void refreshProgress() {
        levelLabel.setText("Level: " + player.getLevel());
        xpLabel.setText("XP: " + player.getExperience() + "/"
                + player.getRequiredExperienceForNextLevel());
        xpProgressBar.setValue(player.getExperiencePercentage());
        xpProgressBar.setString(player.getExperiencePercentage() + "%");
        reputationLabel.setText("🏆 Rep: " + player.getReputation()
                + " (" + ReputationText.titleFor(player.getReputation()) + ")");
        jobBonusLabel.setText("💰 Job Bonus: " + player.getJobBonus());
    }
}
