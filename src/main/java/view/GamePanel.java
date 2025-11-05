package view;

/**
 * Main game UI panel that renders the core game interface and user interactions.
 *
 * Follows Single Responsibility Principle by containing only UI-related code for the game panel.
 */

import controller.GameController;
import controller.RoutineController;
import controller.UserController;
import model.User;
import model.LearnableSkill;
import model.Job;
import provider.JobProvider;
import provider.SkillProvider;
import provider.ServiceProvider;
import observer.Observer;
import view.theme.AppTheme;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import utils.Timer;

public class GamePanel extends JPanel implements Observer {
    private User currentUser;
    private GameController controller;
    private RoutineController routineController;
    private UserController userController;
    private JLabel usernameLabel, balanceLabel, healthLabel, energyLabel;
    private JLabel levelLabel, xpLabel, reputationLabel, streakLabel;
    private JProgressBar xpProgressBar;
    private JTextArea chatArea;
    private JPanel animationPanel;
    private JPanel rightPanel;
    // These buttons are being moved to the world view, so they are removed from here.
    private JButton saveAndExitButton, helpButton, bottomSaveButton;
    private javax.swing.Action saveAndExitAction;
    private final long sessionStartTime;

    // Construct the main game panel for the given user
    public GamePanel(User user) {
        this.currentUser = user;
        this.sessionStartTime = System.currentTimeMillis();
        this.controller = new GameController(user, sessionStartTime);
        this.userController = controller.getUserController();
        this.userController.addObserver(this); // Register the panel as an observer
        this.routineController = new RoutineController(userController, this);
        setLayout(new BorderLayout(10,10));
        // Step-5: one design system — same background as login/register.
        setBackground(AppTheme.BACKGROUND_COLOR);

        // ---------------- Title ----------------
        JLabel titleLabel = new JLabel("Job-Quest-Game", SwingConstants.CENTER);
        titleLabel.setFont(AppTheme.FONT_TITLE);
        titleLabel.setForeground(AppTheme.PRIMARY_COLOR);
        add(titleLabel, BorderLayout.NORTH);

        // ---------------- Right Side Bar ----------------
        rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setPreferredSize(new Dimension(240, 0));
        rightPanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        rightPanel.setBackground(AppTheme.BACKGROUND_COLOR);

        // Profile Panel (themed section — same card system as dialogs)
        JPanel profilePanel = AppTheme.createSectionPanel("Your Profile");
        usernameLabel = new JLabel("👤 " + userController.getUsername());
        usernameLabel.setFont(AppTheme.FONT_BODY);
        balanceLabel = new JLabel("💰 Balance: $" + userController.getBalance());
        balanceLabel.setFont(AppTheme.FONT_BODY);
        balanceLabel.setForeground(AppTheme.MONEY_COLOR.darker());
        healthLabel = new JLabel("❤️ Health: " + userController.getHealth() + "/100");
        healthLabel.setFont(AppTheme.FONT_BODY);
        healthLabel.setForeground(AppTheme.HEALTH_COLOR);
        profilePanel.add(usernameLabel);
        profilePanel.add(balanceLabel);
        profilePanel.add(healthLabel);

        // Progression Panel
        JPanel progressPanel = AppTheme.createSectionPanel("⭐ Progression");
        
        levelLabel = new JLabel("Level: " + currentUser.getLevel());
        levelLabel.setFont(AppTheme.FONT_BODY);
        xpLabel = new JLabel("XP: " + currentUser.getExperience() + "/" + currentUser.getRequiredExperienceForNextLevel());
        xpLabel.setFont(AppTheme.FONT_SMALL);
        xpLabel.setForeground(AppTheme.TEXT_SECONDARY);
        xpProgressBar = new JProgressBar(0, 100);
        xpProgressBar.setValue(currentUser.getExperiencePercentage());
        xpProgressBar.setStringPainted(true);
        xpProgressBar.setString(currentUser.getExperiencePercentage() + "%");
        xpProgressBar.setForeground(AppTheme.PRIMARY_COLOR);
        xpProgressBar.setBackground(AppTheme.DIVIDER_COLOR);
        xpProgressBar.setFont(AppTheme.FONT_SMALL);
        
        reputationLabel = new JLabel("🏆 Rep: " + currentUser.getReputation() + " (" + currentUser.getReputationTitle() + ")");
        reputationLabel.setFont(AppTheme.FONT_BODY);
        streakLabel = new JLabel("🔥 Streak: " + currentUser.getCurrentStreak() + " days");
        streakLabel.setFont(AppTheme.FONT_BODY);
        streakLabel.setForeground(AppTheme.WARNING_COLOR.darker());
        
        progressPanel.add(levelLabel);
        progressPanel.add(xpLabel);
        progressPanel.add(xpProgressBar);
        progressPanel.add(Box.createVerticalStrut(5));
        progressPanel.add(reputationLabel);
        progressPanel.add(streakLabel);

        // Identity Panel
        JPanel identityPanel = AppTheme.createSectionPanel("Current Identity");
        JLabel identityName = new JLabel("💼 " + userController.getUser().getIdentity().getClass().getSimpleName());
        identityName.setFont(AppTheme.FONT_BODY);
        energyLabel = new JLabel("⚡ Energy: " + userController.getEnergy() + "/100000");
        energyLabel.setFont(AppTheme.FONT_BODY);
        energyLabel.setForeground(AppTheme.TEXT_SECONDARY);
        identityPanel.add(identityName);
        identityPanel.add(energyLabel);

        // Buttons Panel
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.Y_AXIS));
        buttonsPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        buttonsPanel.setBackground(AppTheme.BACKGROUND_COLOR);
        
        helpButton = AppTheme.createSecondaryButton("📖 Help & About");
        helpButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Step-5 fix: one shared Action, two separate buttons.
        // Previously the SAME JButton instance was added to buttonsPanel AND
        // bottomActionsPanel — Swing re-parents it, so one location went blank.
        saveAndExitAction = new AbstractAction("💾 Save & Exit") {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                GameController.ActionResult result = controller.saveGame();
                addChatMessage("SYSTEM: " + result.message);
                if (result.success) {
                    System.exit(0);
                }
            }
        };
        saveAndExitButton = AppTheme.createDangerButton("💾 Save & Exit");
        saveAndExitButton.setAction(saveAndExitAction);
        saveAndExitButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        bottomSaveButton = AppTheme.createDangerButton("💾 Save & Exit");
        bottomSaveButton.setAction(saveAndExitAction);
        
        buttonsPanel.add(helpButton);
        buttonsPanel.add(Box.createVerticalStrut(5));
        buttonsPanel.add(saveAndExitButton);

        rightPanel.add(profilePanel);
        rightPanel.add(Box.createVerticalStrut(10));
        rightPanel.add(progressPanel);
        rightPanel.add(Box.createVerticalStrut(10));
        rightPanel.add(identityPanel);
        rightPanel.add(Box.createVerticalStrut(10));
        rightPanel.add(buttonsPanel);

        // Wrap the right panel in a scroll pane to handle overflow
        JScrollPane rightScrollPane = new JScrollPane(rightPanel);
        rightScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        rightScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        rightScrollPane.setBorder(null); // The inner panel already has borders
        add(rightScrollPane, BorderLayout.EAST);

        JPanel bottomActionsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomActionsPanel.setBackground(AppTheme.BACKGROUND_COLOR);
        bottomActionsPanel.add(bottomSaveButton);

        // ---------------- Center Animation / World View ----------------
        // Load and display the world view image
        try {
            java.net.URL imageUrl = getClass().getResource("/images/WorldView.png");
            if (imageUrl != null) {
                // Use a custom panel that scales the background image
                animationPanel = new JPanel() { // No layout needed as we use a MouseListener
                    private final Image bgImage = new ImageIcon(imageUrl).getImage();

                    
                    protected void paintComponent(Graphics g) {
                        super.paintComponent(g);
                        // Draw the image scaled to fit the panel's current size
                        g.drawImage(bgImage, 0, 0, getWidth(), getHeight(), this);
                    }
                };
                animationPanel.setBorder(BorderFactory.createTitledBorder(
                    BorderFactory.createLineBorder(AppTheme.BORDER_COLOR, 1), "WORLD VIEW",
                    javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.TOP,
                    AppTheme.FONT_SUBHEADING, AppTheme.TEXT_PRIMARY));

                // Add a mouse listener to handle clicks on different regions of the image
                animationPanel.addMouseListener(new MouseAdapter() {
                    
                    public void mouseClicked(MouseEvent e) {
                        int panelWidth = animationPanel.getWidth();
                        int sectionWidth = panelWidth / 5;
                        int x = e.getX();

                        if (x >= 0 && x < sectionWidth) {
                            // Section 1: Hospital
                            ServiceDialog dialog = new ServiceDialog((JFrame) SwingUtilities.getWindowAncestor(GamePanel.this), controller, GamePanel.this, ServiceProvider.getHealthServices());
                            dialog.setVisible(true);
                        } else if (x >= sectionWidth && x < sectionWidth * 2) {
                            // Section 2: WorkShop
                            GameController.ActionResult result = controller.doWork();
                            if ("JOB_DIALOG_REQUIRED".equals(result.message)) {
                                java.util.List<Job> availableJobs = JobProvider.getAvailableJobsForUser(currentUser);
                                JobDialog dialog = new JobDialog((JFrame) SwingUtilities.getWindowAncestor(GamePanel.this), controller, GamePanel.this, availableJobs);
                                dialog.setVisible(true);
                            } else {
                                addChatMessage(result.message);
                            }
                        } else if (x >= sectionWidth * 2 && x < sectionWidth * 3) {
                            // Section 3: Empty
                            // No action needed
                        } else if (x >= sectionWidth * 3 && x < sectionWidth * 4) {
                            // Section 4: SuperMarket
                            ServiceDialog dialog = new ServiceDialog((JFrame) SwingUtilities.getWindowAncestor(GamePanel.this), controller, GamePanel.this, ServiceProvider.getEnergyServices());
                            dialog.setVisible(true);
                        } else if (x >= sectionWidth * 4 && x < panelWidth) {
                            // Section 5: School
                            java.util.List<LearnableSkill> availableSkills = SkillProvider.getAvailableSkillsForUser(currentUser);
                            SkillDialog dialog = new SkillDialog((JFrame) SwingUtilities.getWindowAncestor(GamePanel.this), controller, GamePanel.this, availableSkills);
                            dialog.setVisible(true);
                        }
                    }
                });

            } else {
                // Fallback if image is not found
                animationPanel = new JPanel(new GridBagLayout());
                animationPanel.setBorder(BorderFactory.createTitledBorder(
                    BorderFactory.createLineBorder(AppTheme.BORDER_COLOR, 1), "WORLD VIEW",
                    javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.TOP,
                    AppTheme.FONT_SUBHEADING, AppTheme.TEXT_PRIMARY));
                animationPanel.setBackground(Color.BLACK);
                // Fallback if image is not found
                animationPanel.add(new JLabel("World view image not found."));
            }
        } catch (Exception e) {
            e.printStackTrace();
            animationPanel.add(new JLabel("Error loading world view.", SwingConstants.CENTER), BorderLayout.CENTER);
        }
        add(animationPanel, BorderLayout.CENTER);

        // ---------------- Chat ----------------
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

        JPanel southPanel = new JPanel(new BorderLayout());
        southPanel.add(chatScroll, BorderLayout.CENTER);
        southPanel.add(bottomActionsPanel, BorderLayout.SOUTH);
        add(southPanel, BorderLayout.SOUTH);

        // ---------------- Action Listeners ----------------
        helpButton.addActionListener(e -> {
            AboutDialog aboutDialog = new AboutDialog((JFrame) SwingUtilities.getWindowAncestor(GamePanel.this));
            aboutDialog.setVisible(true);
        });
        // NOTE: save/exit is handled by the shared saveAndExitAction above —
        // no per-button listener, so both buttons stay in sync (DRY).

    // Start periodic background tasks (timers). We keep them in a small helper
    // method to make the constructor easier to read for beginners.
    setupTimers();

        // ---------------- Check if blocked ----------------
        if(userController.isBlocked()) {
            JOptionPane.showMessageDialog(this,
                "Your account is blocked until " + new java.util.Date(userController.getBlockedUntil()));
            disableActions();
        }

        // Unblocking user after 48 hours (in milliseconds)
        Timer.runOnce(48 * 60 * 60 * 1000, () -> userController.unblockUser());
    }

    
    // Observer callback: refresh panel when observed state changes
    public void update() {
        updateUserInfo();
    }

    // Update UI labels and progress bars with the latest user info
    public void updateUserInfo() {
        usernameLabel.setText("👤 " + userController.getUsername());
        balanceLabel.setText("💰 Balance: $" + userController.getBalance());
        healthLabel.setText("❤️ Health: " + userController.getHealth() + "/100");
        energyLabel.setText("⚡ Energy: " + userController.getEnergy() + "/100000");
        
        // Update progression info
        levelLabel.setText("Level: " + currentUser.getLevel());
        xpLabel.setText("XP: " + currentUser.getExperience() + "/" + currentUser.getRequiredExperienceForNextLevel());
        xpProgressBar.setValue(currentUser.getExperiencePercentage());
        xpProgressBar.setString(currentUser.getExperiencePercentage() + "%");
        reputationLabel.setText("🏆 Rep: " + currentUser.getReputation() + " (" + currentUser.getReputationTitle() + ")");
        streakLabel.setText("🔥 Streak: " + currentUser.getCurrentStreak() + " days");
    }

    // Helper to start periodic timers used by the panel. Kept small and readable.
    private void setupTimers() {
        // Decrease energy every 5 seconds
        Timer.runPeriodic(5_000, e -> routineController.decreaseEnergy());
        // Decrease health every 1 minute
        Timer.runPeriodic(60_000, e -> routineController.decreaseHealth());
        // Sickness check once a day
        Timer.runPeriodic(24 * 60 * 60 * 1000, e -> routineController.sicknessCheck());

        // Unblocking user after 48 hours (in milliseconds)
        Timer.runOnce(48 * 60 * 60 * 1000, () -> userController.unblockUser());
    }
    

    // Append a message to the chat / notifications area
    public void addChatMessage(String msg) {
        chatArea.append(msg + "\n");
    }

    // Return the panel that displays the world view animation/image
    public JPanel getAnimationPanel() {
        return animationPanel;
    }

    // Disable interactive actions (placeholder for future use)
    public void disableActions() {
        // This method can be expanded later to disable the world view buttons if needed
    }

    // Add UI helper methods for controller:
    // Show an energy warning dialog and log it
    public void showEnergyWarning() {
        JOptionPane.showMessageDialog(this,
            "Warning! Energy low (" + userController.getEnergy() + ")");
        addChatMessage("⚠️ Energy low warning!");
    }

    // Show a health warning dialog and log it
    public void showHealthWarning() {
        JOptionPane.showMessageDialog(this,
            "Warning! Health is very low (" + userController.getHealth() + ")");
        addChatMessage("⚠️ Health low warning!");
    }

    // Notify the user about account blocking with penalty info
    public void showBlockDialog(String reason, int penalty) {
        JOptionPane.showMessageDialog(this, reason + "! Account blocked for 48 hours. Penalty: $" + penalty);
    }
}
