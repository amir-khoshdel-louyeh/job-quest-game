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
import provider.ShopItemProvider;
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
    private JProgressBar xpProgressBar, healthBar, energyBar;
    private JTextArea chatArea;
    private JPanel animationPanel;
    private JPanel rightPanel;
    // These buttons are being moved to the world view, so they are removed from here.
    private JButton saveAndExitButton, helpButton, bottomSaveButton;
    private javax.swing.Action saveAndExitAction;
    private final long sessionStartTime;
    // Step-6: single non-blocking toast reused for success feedback.
    private JDialog toastDialog;
    private JLabel toastLabel;
    private javax.swing.Timer toastTimer;

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
        // Step-6: live HUD bar — Observer.update() refreshes it, no polling.
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
        // Step-6: live HUD bar for the 0–100k energy pool.
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
                // Step-6: image zones delegate to the same openX() helpers as the
                // nav bar below (DRY) — blind clicks are now discoverable.
                animationPanel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                animationPanel.setToolTipText("Hospital | Workshop | Market | School — or use the buttons above");
                animationPanel.addMouseListener(new MouseAdapter() {
                    
                    public void mouseClicked(MouseEvent e) {
                        int panelWidth = animationPanel.getWidth();
                        int sectionWidth = panelWidth / 5;
                        int x = e.getX();

                        if (x >= 0 && x < sectionWidth) {
                            openHospital();
                        } else if (x >= sectionWidth && x < sectionWidth * 2) {
                            openWorkshop();
                        } else if (x >= sectionWidth * 2 && x < sectionWidth * 3) {
                            addChatMessage("Empty lot — try the buttons above: Hospital, Workshop, Market, School, Shop, Inventory.");
                        } else if (x >= sectionWidth * 3 && x < sectionWidth * 4) {
                            openMarket();
                        } else if (x >= sectionWidth * 4 && x < panelWidth) {
                            openSchool();
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
        // Step-6: labeled nav bar with tooltips — the fix for blind image-click.
        // Same openX() helpers as image zones, plus previously unreachable Shop/Inventory.
        JPanel centerPanel = new JPanel(new BorderLayout(0, 8));
        centerPanel.setBackground(AppTheme.BACKGROUND_COLOR);
        centerPanel.add(buildNavBar(), BorderLayout.NORTH);
        centerPanel.add(animationPanel, BorderLayout.CENTER);
        add(centerPanel, BorderLayout.CENTER);

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
    // Step-8: first-run onboarding — tutorial evaporated after login before.
    maybeShowOnboarding();

        // ---------------- Check if blocked ----------------
        if(userController.isBlocked()) {
            JOptionPane.showMessageDialog(this,
                "Your account is blocked until " + new java.util.Date(userController.getBlockedUntil()));
        }

        // Unblocking user after 48 hours (in milliseconds)
        Timer.runOnce(48 * 60 * 60 * 1000, () -> userController.unblockUser());
    }

    
    // Observer callback: refresh panel when observed state changes
    public void update() {
        updateUserInfo();
    }

    // Update UI labels and progress bars with the latest user info
    // Called directly AND via Observer.update() on every stat mutation.
    public void updateUserInfo() {
        usernameLabel.setText("👤 " + userController.getUsername());
        balanceLabel.setText("💰 Balance: $" + userController.getBalance());
        int hp = userController.getHealth();
        healthLabel.setText("❤️ Health: " + hp + "/100");
        healthBar.setValue(Math.max(0, Math.min(100, hp)));
        healthBar.setString(hp + "%");
        // Low-HP flash: switch to warning color under 30 (simulator readability).
        healthBar.setForeground(hp < 30 ? AppTheme.WARNING_COLOR : AppTheme.HEALTH_COLOR);
        int en = userController.getEnergy();
        energyLabel.setText("⚡ Energy: " + en + "/100000");
        energyBar.setValue(Math.max(0, Math.min(100000, en)));
        energyBar.setString((en * 100 / 100000) + "%");
        energyBar.setForeground(en < 25000 ? AppTheme.WARNING_COLOR : AppTheme.ENERGY_COLOR.darker());
        
        // Update progression info
        levelLabel.setText("Level: " + currentUser.getLevel());
        xpLabel.setText("XP: " + currentUser.getExperience() + "/" + currentUser.getRequiredExperienceForNextLevel());
        xpProgressBar.setValue(currentUser.getExperiencePercentage());
        xpProgressBar.setString(currentUser.getExperiencePercentage() + "%");
        reputationLabel.setText("🏆 Rep: " + currentUser.getReputation() + " (" + currentUser.getReputationTitle() + ")");
        streakLabel.setText("🔥 Streak: " + currentUser.getCurrentStreak() + " days");
    }

    // Helper to start periodic timers used by the panel. Kept small and readable.
    // Step-8 tuning: energy ticks were chat-noisy at 5s; RoutineService only
    // messages on poor-health/low-energy edge cases, so a calmer 30s cadence
    // preserves survival pressure without log spam. Health/sickness unchanged.
    private void setupTimers() {
        // Decrease energy every 30 seconds (was 5s — same totals, less noise)
        Timer.runPeriodic(30_000, e -> routineController.decreaseEnergy());
        // Decrease health every 1 minute
        Timer.runPeriodic(60_000, e -> routineController.decreaseHealth());
        // Sickness check once a day
        Timer.runPeriodic(24 * 60 * 60 * 1000, e -> routineController.sicknessCheck());

        // Unblocking user after 48 hours (in milliseconds)
        Timer.runOnce(48 * 60 * 60 * 1000, () -> userController.unblockUser());
    }

    /**
     * Step-8: onboarding checklist for fresh accounts (SRP: view-only hints,
     * game state untouched). Veterans (jobs/skills done) see nothing.
     */
    private void maybeShowOnboarding() {
        boolean fresh = currentUser.getTotalJobsCompleted() == 0
                && currentUser.getSkills().isEmpty();
        if (!fresh) return;
        addChatMessage("👋 Welcome to Job Quest! Your goal: earn, learn skills, unlock better jobs.");
        addChatMessage("🎯 Next: 1) 🔨 Workshop — do your first job  2) 📊 Progress — track challenges  3) 🎓 School — learn a skill");
        addChatMessage("💡 Tip: low bars? 🛒 Market restores energy, 🏥 Hospital restores health, 🛍️ Shop stocks 🎒 Inventory. F11 toggles windowed mode.");
    }
    

    // Append a message to the chat / notifications area
    // Step-6: auto-scroll + non-blocking toast for success lines so rewards
    // are noticed without modal spam (dialogs already stopped popping).
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
        toastTimer = new javax.swing.Timer(2500, e -> toastDialog.setVisible(false));
        toastTimer.setRepeats(false);
        toastTimer.start();
    }

    /** Identity simple name (Typist/Programmer/LogoDesigner/Doctor) for game routing. */
    public String getUserIdentity() {
        return currentUser.getIdentity().getClass().getSimpleName();
    }

    // ---------------- Step-6: navigation + affordance ----------------
    // One helper per destination (SRP, DRY): nav buttons and image zones share them.

    private JFrame parentFrame() {
        return (JFrame) SwingUtilities.getWindowAncestor(this);
    }

    private void openHospital() {
        new ServiceDialog(parentFrame(), controller, this, ServiceProvider.getHealthServices()).setVisible(true);
    }

    private void openWorkshop() {
        GameController.ActionResult result = controller.doWork();
        if ("JOB_DIALOG_REQUIRED".equals(result.message)) {
            String identity = currentUser.getIdentity().getClass().getSimpleName();
            java.util.List<Job> myJobs = JobProvider.getJobsForIdentity(identity);
            new JobDialog(parentFrame(), controller, this, myJobs, currentUser).setVisible(true);
        } else {
            addChatMessage(result.message);
        }
    }

    private void openMarket() {
        new ServiceDialog(parentFrame(), controller, this, ServiceProvider.getEnergyServices()).setVisible(true);
    }

    private void openSchool() {
        String identity = currentUser.getIdentity().getClass().getSimpleName();
        java.util.List<LearnableSkill> mySkills = SkillProvider.getSkillsForIdentity(identity);
        new SkillDialog(parentFrame(), controller, this, mySkills, currentUser).setVisible(true);
    }

    private void openShop() {
        new ShopDialog(parentFrame(), controller, this, ShopItemProvider.getAvailableItems()).setVisible(true);
    }

    private void openInventory() {
        new InventoryDialog(parentFrame(), controller, this).setVisible(true);
    }

    private void openProgress() {
        new ProgressDialog(parentFrame(), controller, this).setVisible(true);
    }

    private void openDevControls() {
        controller.DeveloperController dev = new controller.DeveloperController(userController);
        new DeveloperDialog(parentFrame(), dev, this).setVisible(true);
    }

    /** Labeled nav bar: icon + tooltip per destination, themed buttons. */
    private JPanel buildNavBar() {
        JPanel nav = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 4));
        nav.setBackground(AppTheme.BACKGROUND_COLOR);
        nav.add(navButton("🏥 Hospital", "Buy health services (heal)", true, e -> openHospital()));
        nav.add(navButton("🔨 Workshop", "Do jobs to earn money and XP", true, e -> openWorkshop()));
        nav.add(navButton("🛒 Market", "Buy energy services (eat)", true, e -> openMarket()));
        nav.add(navButton("🎓 School", "Learn skills to unlock better jobs", true, e -> openSchool()));
        nav.add(navButton("🛍️ Shop", "Buy storable items, then use from inventory", false, e -> openShop()));
        nav.add(navButton("🎒 Inventory", "Use owned items (" + controller.getInventoryItems().size() + ")", false, e -> openInventory()));
        nav.add(navButton("📊 Progress", "Challenges, achievements and stats", false, e -> openProgress()));
        nav.add(navButton("🛠 Dev", "Developer demo controls (change variables)", false, e -> openDevControls()));
        return nav;
    }

    private JButton navButton(String text, String tooltip, boolean primary,
                              java.awt.event.ActionListener action) {
        JButton b = primary ? AppTheme.createPrimaryButton(text) : AppTheme.createSecondaryButton(text);
        b.setToolTipText(tooltip);
        b.addActionListener(action);
        return b;
    }
}
