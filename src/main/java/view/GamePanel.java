package view;

// The main game screen: stats on the side, world in the middle, chat at the bottom.
import controller.GameController;
import controller.MessageSink;
import controller.RoutineController;
import controller.UserController;
import model.player.User;
import observer.ChangeType;
import observer.Observer;
import view.theme.AppTheme;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.EnumMap;
import java.util.Map;
import java.util.function.Consumer;
import utils.TimerUtil;

public class GamePanel extends JPanel implements Observer, MessageSink {
    private User currentUser;
    private GameController controller;
    private RoutineController routineController;
    private UserController userController;
    private UserStatsPanel statsPanel;
    private ChatPanel chatPanel;
    private NavBar navBar;
    // Dispatch table instead of a switch: each change type knows which
    // section to repaint. SKILLS maps to a no-op (nothing skill-related
    // shows in this panel).
    private final Map<ChangeType, Consumer<UserStatsPanel>> refreshers = new EnumMap<>(ChangeType.class);
    {
        refreshers.put(ChangeType.BALANCE, UserStatsPanel::refreshBalance);
        refreshers.put(ChangeType.HEALTH, UserStatsPanel::refreshHealth);
        refreshers.put(ChangeType.ENERGY, UserStatsPanel::refreshEnergy);
        refreshers.put(ChangeType.XP, UserStatsPanel::refreshProgress);
        refreshers.put(ChangeType.REPUTATION, UserStatsPanel::refreshProgress);
        refreshers.put(ChangeType.JOBS, UserStatsPanel::refreshProgress);
        refreshers.put(ChangeType.SKILLS, panel -> { /* no skill display here */ });
    }
    private JPanel animationPanel;
    private JPanel rightPanel;
    private JButton exitButton, helpButton, bottomExitButton, newGameButton;
    private javax.swing.Action exitAction;
    private final long sessionStartTime;

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

        statsPanel = new UserStatsPanel(userController, currentUser);

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

        rightPanel.add(statsPanel);
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
                            navBar.openHospital();
                        } else if (x >= sectionWidth && x < sectionWidth * 2) {
                            navBar.openWorkshop();
                        } else if (x >= sectionWidth * 2 && x < sectionWidth * 3) {
                            // Middle zone intentionally does nothing.
                        } else if (x >= sectionWidth * 3 && x < sectionWidth * 4) {
                            navBar.openMarket();
                        } else if (x >= sectionWidth * 4 && x < panelWidth) {
                            navBar.openSchool();
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
        navBar = new NavBar(controller, currentUser, userController, this);
        centerPanel.add(navBar, BorderLayout.NORTH);
        centerPanel.add(animationPanel, BorderLayout.CENTER);
        add(centerPanel, BorderLayout.CENTER);

        chatPanel = new ChatPanel();

        JPanel southPanel = new JPanel(new BorderLayout());
        southPanel.add(chatPanel, BorderLayout.CENTER);
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

    // Selective refresh via the dispatch table above — no switch.
    @Override
    public void update(ChangeType whatChanged) {
        if (whatChanged == null) {
            updateUserInfo();
            return;
        }
        Consumer<UserStatsPanel> refresh = refreshers.get(whatChanged);
        if (refresh == null) {
            updateUserInfo();
            return;
        }
        refresh.accept(statsPanel);
    }

    public void updateUserInfo() {
        statsPanel.refresh();
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
    

    // Delegate: chat lives in ChatPanel; this stays a MessageSink
    // so existing callers (dialogs, RoutineController) don't change.
    @Override
    public void addChatMessage(String msg) {
        chatPanel.addChatMessage(msg);
    }

    /** Returns the player's profession object for game routing. */
    public model.Identity getUserIdentity() {
        return currentUser.getIdentity();
    }
}
