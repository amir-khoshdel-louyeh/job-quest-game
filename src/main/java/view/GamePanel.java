package view;

// The main game screen: stats on the side, world in the middle, chat at the bottom.
import controller.GameController;
import controller.RoutineController;
import controller.UserController;
import model.User;
import model.LearnableSkill;
import model.Job;
import provider.JobProvider;
import provider.SkillProvider;
import provider.ServiceProvider;
import observer.ChangeType;
import observer.Observer;
import view.theme.AppTheme;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import utils.TimerUtil;

public class GamePanel extends JPanel implements Observer {
    private User currentUser;
    private GameController controller;
    private RoutineController routineController;
    private UserController userController;
    private JLabel usernameLabel, balanceLabel, healthLabel, energyLabel;
    private JLabel levelLabel, xpLabel, reputationLabel, jobBonusLabel;
    private JProgressBar xpProgressBar, healthBar, energyBar;
    private JTextArea chatArea;
    private JPanel animationPanel;
    private JPanel rightPanel;
    private JButton exitButton, helpButton, bottomExitButton, newGameButton;
    private javax.swing.Action exitAction;
    private final long sessionStartTime;
    // Reused toast popup for success messages.
    private JDialog toastDialog;
    private JLabel toastLabel;
    private javax.swing.Timer toastTimer;

    public GamePanel(User user) {
        this.currentUser = user;
        this.sessionStartTime = System.currentTimeMillis();
        this.controller = new GameController(user, sessionStartTime);
        this.userController = controller.getUserController();
        this.userController.addObserver(this); // Get stat updates.
        this.routineController = new RoutineController(userController, this);
        setLayout(new BorderLayout(10,10));
        setBackground(AppTheme.BACKGROUND_COLOR);

        JLabel titleLabel = new JLabel("Job-Quest-Game", SwingConstants.CENTER);
        titleLabel.setFont(AppTheme.FONT_TITLE);
        titleLabel.setForeground(AppTheme.PRIMARY_COLOR);
        add(titleLabel, BorderLayout.NORTH);

        rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setPreferredSize(new Dimension(240, 0));
        rightPanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        rightPanel.setBackground(AppTheme.BACKGROUND_COLOR);

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
        jobBonusLabel = new JLabel("💰 Job Bonus: " + currentUser.getJobBonus());
        jobBonusLabel.setFont(AppTheme.FONT_BODY);
        jobBonusLabel.setForeground(AppTheme.WARNING_COLOR.darker());
        
        progressPanel.add(levelLabel);
        progressPanel.add(xpLabel);
        progressPanel.add(xpProgressBar);
        progressPanel.add(Box.createVerticalStrut(5));
        progressPanel.add(reputationLabel);
        progressPanel.add(jobBonusLabel);

        JPanel identityPanel = AppTheme.createSectionPanel("Current Identity");
        JLabel identityName = new JLabel("💼 " + userController.getUser().getIdentity().getDisplayName());
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

        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.Y_AXIS));
        buttonsPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        buttonsPanel.setBackground(AppTheme.BACKGROUND_COLOR);
        
        helpButton = AppTheme.createSecondaryButton("📖 Help & About");
        helpButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        // No save - in-memory only. Exit closes, New Game goes back to career pick.
        exitAction = new AbstractAction("❌ Exit") {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                System.exit(0);
            }
        };
        exitButton = AppTheme.createDangerButton("❌ Exit");
        exitButton.setAction(exitAction);
        exitButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        bottomExitButton = AppTheme.createDangerButton("❌ Exit");
        bottomExitButton.setAction(exitAction);

        newGameButton = AppTheme.createSecondaryButton("🔄 New Game");
        newGameButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        newGameButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(GamePanel.this);
                if (frame instanceof MainFrame) {
                    ((MainFrame) frame).showPanel(new StartPanel((MainFrame) frame));
                }
            }
        });
        
        buttonsPanel.add(helpButton);
        buttonsPanel.add(Box.createVerticalStrut(5));
        buttonsPanel.add(newGameButton);
        buttonsPanel.add(Box.createVerticalStrut(5));
        buttonsPanel.add(exitButton);

        rightPanel.add(profilePanel);
        rightPanel.add(Box.createVerticalStrut(10));
        rightPanel.add(progressPanel);
        rightPanel.add(Box.createVerticalStrut(10));
        rightPanel.add(identityPanel);
        rightPanel.add(Box.createVerticalStrut(10));
        rightPanel.add(buttonsPanel);

        JScrollPane rightScrollPane = new JScrollPane(rightPanel);
        rightScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        rightScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        rightScrollPane.setBorder(null); // The inner panel already has borders
        add(rightScrollPane, BorderLayout.EAST);

        JPanel bottomActionsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomActionsPanel.setBackground(AppTheme.BACKGROUND_COLOR);
        bottomActionsPanel.add(bottomExitButton);

        try {
            java.net.URL imageUrl = getClass().getResource("/images/WorldView.png");
            if (imageUrl != null) {
                animationPanel = new JPanel() {
                    private final Image bgImage = new ImageIcon(imageUrl).getImage();

                    protected void paintComponent(Graphics g) {
                        super.paintComponent(g);
                        g.drawImage(bgImage, 0, 0, getWidth(), getHeight(), this);
                    }
                };
                animationPanel.setBorder(BorderFactory.createTitledBorder(
                    BorderFactory.createLineBorder(AppTheme.BORDER_COLOR, 1), "WORLD VIEW",
                    javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.TOP,
                    AppTheme.FONT_SUBHEADING, AppTheme.TEXT_PRIMARY));

                // Click zones from left to right: Hospital, Workshop, empty middle, Market, School.
                animationPanel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                animationPanel.setToolTipText("Hospital | Workshop | (closed) | Market | School — or use the buttons above");
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
                            // Middle zone intentionally does nothing.
                        } else if (x >= sectionWidth * 3 && x < sectionWidth * 4) {
                            openMarket();
                        } else if (x >= sectionWidth * 4 && x < panelWidth) {
                            openSchool();
                        }
                    }
                });

            } else {
                animationPanel = new JPanel(new GridBagLayout());
                animationPanel.setBorder(BorderFactory.createTitledBorder(
                    BorderFactory.createLineBorder(AppTheme.BORDER_COLOR, 1), "WORLD VIEW",
                    javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.TOP,
                    AppTheme.FONT_SUBHEADING, AppTheme.TEXT_PRIMARY));
                animationPanel.setBackground(Color.BLACK);
                animationPanel.add(new JLabel("World view image not found."));
            }
        } catch (Exception e) {
            e.printStackTrace();
            animationPanel.add(new JLabel("Error loading world view.", SwingConstants.CENTER), BorderLayout.CENTER);
        }
        JPanel centerPanel = new JPanel(new BorderLayout(0, 8));
        centerPanel.setBackground(AppTheme.BACKGROUND_COLOR);
        centerPanel.add(buildNavBar(), BorderLayout.NORTH);
        centerPanel.add(animationPanel, BorderLayout.CENTER);
        add(centerPanel, BorderLayout.CENTER);

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

        helpButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                AboutDialog aboutDialog = new AboutDialog((JFrame) SwingUtilities.getWindowAncestor(GamePanel.this));
                aboutDialog.setVisible(true);
            }
        });
        // NOTE: exit is handled by the shared exitAction above.

    // Start periodic background tasks.
    setupTimers();
    // Show onboarding hints for fresh accounts.
    maybeShowOnboarding();

        if (userController.isBlocked()) {
            JOptionPane.showMessageDialog(this,
                "Your account is blocked until " + new java.util.Date(userController.getBlockedUntil()));
        }

        // Unblock after 5 minutes (10h-run tuning, was 48 hours).
        final controller.UserController myUserController1 = userController;
        utils.TimerUtil.runOnce(5 * 60 * 1000, new Runnable() {
            public void run() {
                myUserController1.unblockUser();
            }
        });
    }

    
    // Refresh panel when stats change.
    public void update() {
        updateUserInfo();
    }

    // Full refresh when anything changes.
    @Override
    public void update(ChangeType whatChanged) {
        updateUserInfo();
    }

    public void updateUserInfo() {
        usernameLabel.setText("User: " + userController.getUsername());
        balanceLabel.setText("Balance: $" + userController.getBalance());
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
        
        levelLabel.setText("Level: " + currentUser.getLevel());
        xpLabel.setText("XP: " + currentUser.getExperience() + "/" + currentUser.getRequiredExperienceForNextLevel());
        xpProgressBar.setValue(currentUser.getExperiencePercentage());
        xpProgressBar.setString(currentUser.getExperiencePercentage() + "%");
        reputationLabel.setText("🏆 Rep: " + currentUser.getReputation() + " (" + currentUser.getReputationTitle() + ")");
        jobBonusLabel.setText("💰 Job Bonus: " + currentUser.getJobBonus());
    }

    private void setupTimers() {
        final controller.RoutineController myRoutine1 = routineController;
        TimerUtil.runPeriodic(30000, new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                myRoutine1.decreaseEnergy();
            }
        });
        final controller.RoutineController myRoutine2 = routineController;
        TimerUtil.runPeriodic(60000, new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                myRoutine2.decreaseHealth();
            }
        });
        final controller.RoutineController myRoutine3 = routineController;
        TimerUtil.runPeriodic(24 * 60 * 60 * 1000, new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                myRoutine3.sicknessCheck();
            }
        });

        final controller.UserController myUser2 = userController;
        TimerUtil.runOnce(5 * 60 * 1000, new Runnable() {
            public void run() {
                myUser2.unblockUser();
            }
        });
    }

    private void maybeShowOnboarding() {
        boolean fresh = currentUser.getTotalJobsCompleted() == 0
                && currentUser.getSkills().isEmpty();
        if (!fresh) return;
        addChatMessage("👋 Welcome to Job Quest! Your goal: earn, learn skills, unlock better jobs.");
        addChatMessage("🎯 Next: 1) 🔨 Workshop — do your first job  2) 📊 Progress — track challenges  3) 🎓 School — learn a skill");
        addChatMessage("💡 Tip: low bars? 🛒 Market restores energy, 🏥 Hospital restores health. F11 toggles windowed mode.");
    }
    

    // Auto-scrolls; success lines also show a toast so rewards are noticed.
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

    /** Returns the player's profession object for game routing. */
    public model.Identity getUserIdentity() {
        return currentUser.getIdentity();
    }

    private JFrame parentFrame() {
        return (JFrame) SwingUtilities.getWindowAncestor(this);
    }

    private void openHospital() {
        new ServiceDialog(parentFrame(), controller, this, ServiceProvider.getHealthServices()).setVisible(true);
    }

    private void openWorkshop() {
        GameController.ActionResult result = controller.doWork();
        if ("JOB_DIALOG_REQUIRED".equals(result.message)) {
            java.util.List<Job> myJobs = JobProvider.getJobsForIdentity(currentUser.getIdentity());
            new JobDialog(parentFrame(), controller, this, myJobs, currentUser).setVisible(true);
        } else {
            addChatMessage(result.message);
        }
    }

    private void openMarket() {
        new ServiceDialog(parentFrame(), controller, this, ServiceProvider.getEnergyServices()).setVisible(true);
    }

    private void openSchool() {
        java.util.List<LearnableSkill> mySkills = SkillProvider.getSkillsForIdentity(currentUser.getIdentity());
        new SkillDialog(parentFrame(), controller, this, mySkills, currentUser).setVisible(true);
    }

    private void openProgress() {
        new ProgressDialog(parentFrame(), controller, this).setVisible(true);
    }

    private void openDevControls() {
        controller.DeveloperController dev = new controller.DeveloperController(userController);
        new DeveloperDialog(parentFrame(), dev, this).setVisible(true);
    }

    private JPanel buildNavBar() {
        JPanel nav = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 4));
        nav.setBackground(AppTheme.BACKGROUND_COLOR);
        nav.add(navButton("Hospital", "Buy health services (heal)", true, new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                openHospital();
            }
        }));
        nav.add(navButton("Workshop", "Do jobs to earn money and XP", true, new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                openWorkshop();
            }
        }));
        nav.add(navButton("Market", "Buy energy services (eat)", true, new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                openMarket();
            }
        }));
        nav.add(navButton("School", "Learn skills to unlock better jobs", true, new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                openSchool();
            }
        }));
        nav.add(navButton("Progress", "Challenges, achievements and stats", false, new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                openProgress();
            }
        }));
        nav.add(navButton("Dev", "Developer demo controls (change variables)", false, new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                openDevControls();
            }
        }));
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
