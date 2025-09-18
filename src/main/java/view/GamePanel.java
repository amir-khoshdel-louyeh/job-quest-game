package view;

import controller.GameController;
import controller.RoutineController;
import controller.UserController;
import model.User;
import model.LearnableSkill;
import model.Job;
import model.JobProvider;
import model.SkillProvider;
import model.ServiceProvider;
import model.ShopItemProvider;
import observer.Observer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import utils.Timer; // Add this import

public class GamePanel extends JPanel implements Observer {
    private User currentUser;
    private GameController controller;
    private RoutineController routineController;
    private UserController userController;
    private JLabel usernameLabel, balanceLabel, healthLabel, energyLabel;
    private JTextArea chatArea;
    private JPanel animationPanel;
    private JPanel rightPanel;
    // These buttons are being moved to the world view, so they are removed from here.
    private JButton workButton, shopButton, servicesButton, saveAndExitButton;
    private final long sessionStartTime;

    public GamePanel(User user) {
        this.currentUser = user;
        this.sessionStartTime = System.currentTimeMillis();
        this.controller = new GameController(user, sessionStartTime);
        this.userController = controller.getUserController();
        this.userController.addObserver(this); // Register the panel as an observer
        this.routineController = new RoutineController(userController, this);
        setLayout(new BorderLayout(10,10));

        // ---------------- Title ----------------
        JLabel titleLabel = new JLabel("Job-Quest-Game", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 32));
        add(titleLabel, BorderLayout.NORTH);

        // ---------------- Right Side Bar ----------------
        rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setPreferredSize(new Dimension(240, 0));
        rightPanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        // Profile Panel
        JPanel profilePanel = new JPanel();
        profilePanel.setLayout(new BoxLayout(profilePanel, BoxLayout.Y_AXIS));
        profilePanel.setBorder(BorderFactory.createTitledBorder("Your Profile"));
        usernameLabel = new JLabel("User: " + userController.getUsername());
        balanceLabel = new JLabel("Balance: $" + userController.getBalance());
        healthLabel = new JLabel("Health: " + userController.getHealth() + "/100");
        profilePanel.add(usernameLabel);
        profilePanel.add(balanceLabel);
        profilePanel.add(healthLabel);

        // Identity Panel
        JPanel identityPanel = new JPanel();
        identityPanel.setLayout(new BoxLayout(identityPanel, BoxLayout.Y_AXIS));
        identityPanel.setBorder(BorderFactory.createTitledBorder("Current Identity"));
        JLabel identityName = new JLabel(userController.getUser().getIdentity().getClass().getSimpleName());
        energyLabel = new JLabel("Energy: " + userController.getEnergy() + "/100000");
        identityPanel.add(identityName);
        identityPanel.add(energyLabel);

        // Save & Exit Button
        saveAndExitButton = new JButton("Save & Exit");
        saveAndExitButton.setBackground(new Color(220, 53, 69)); // A reddish color for emphasis
        saveAndExitButton.setForeground(Color.WHITE);

        rightPanel.add(profilePanel);
        rightPanel.add(Box.createVerticalStrut(10));
        rightPanel.add(identityPanel);

        // Wrap the right panel in a scroll pane to handle overflow
        JScrollPane rightScrollPane = new JScrollPane(rightPanel);
        rightScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        rightScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        rightScrollPane.setBorder(null); // The inner panel already has borders
        add(rightScrollPane, BorderLayout.EAST);

        JPanel bottomActionsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomActionsPanel.add(saveAndExitButton);

        // ---------------- Center Animation / World View ----------------
        // Load and display the world view image
        try {
            java.net.URL imageUrl = getClass().getResource("/images/WorldView.png");
            if (imageUrl != null) {
                // Use a custom panel that scales the background image
                animationPanel = new JPanel() { // No layout needed as we use a MouseListener
                    private final Image bgImage = new ImageIcon(imageUrl).getImage();

                    @Override
                    protected void paintComponent(Graphics g) {
                        super.paintComponent(g);
                        // Draw the image scaled to fit the panel's current size
                        g.drawImage(bgImage, 0, 0, getWidth(), getHeight(), this);
                    }
                };
                animationPanel.setBorder(BorderFactory.createTitledBorder("WORLD VIEW"));

                // Add a mouse listener to handle clicks on different regions of the image
                animationPanel.addMouseListener(new MouseAdapter() {
                    @Override
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
                animationPanel.setBorder(BorderFactory.createTitledBorder("WORLD VIEW"));
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
        chatArea.setBorder(BorderFactory.createTitledBorder("Chat / Notifications"));
        JScrollPane chatScroll = new JScrollPane(chatArea);

        JPanel southPanel = new JPanel(new BorderLayout());
        southPanel.add(chatScroll, BorderLayout.CENTER);
        southPanel.add(bottomActionsPanel, BorderLayout.SOUTH);
        add(southPanel, BorderLayout.SOUTH);

        // ---------------- Action Listeners ----------------
        saveAndExitButton.addActionListener(e -> {
            GameController.ActionResult result = controller.saveGame();
            addChatMessage("SYSTEM: " + result.message);
            if (result.success) {
                // Close the application after a successful save
                System.exit(0);
            }
        });

        // Use the Timer utility for periodic routines
        Timer.runPeriodic(5_000, e -> routineController.decreaseEnergy()); // Decrease energy every 5 seconds
        Timer.runPeriodic(60_000, e -> routineController.decreaseHealth()); // Decrease health every 1 minute
        Timer.runPeriodic(24 * 60 * 60 * 1000, e -> routineController.sicknessCheck()); // Sickness check once a day

        // ---------------- Check if blocked ----------------
        if(userController.isBlocked()) {
            JOptionPane.showMessageDialog(this,
                "Your account is blocked until " + new java.util.Date(userController.getBlockedUntil()));
            disableActions();
        }

        // Unblocking user after 48 hours (in milliseconds)
        Timer.runOnce(48 * 60 * 60 * 1000, () -> userController.unblockUser());
    }

    @Override
    public void update() {
        updateUserInfo();
    }

    public void updateUserInfo() {
        usernameLabel.setText("User: " + userController.getUsername());
        balanceLabel.setText("Balance: $" + userController.getBalance());
        healthLabel.setText("Health: " + userController.getHealth() + "/100");
        energyLabel.setText("Energy: " + userController.getEnergy() + "/100000");
    }

    public void addChatMessage(String msg) {
        chatArea.append(msg + "\n");
    }

    public JPanel getAnimationPanel() {
        return animationPanel;
    }

    public void disableActions() {
        // This method can be expanded later to disable the world view buttons if needed
    }

    // Add UI helper methods for controller:
    public void showEnergyWarning() {
        JOptionPane.showMessageDialog(this,
            "Warning! Energy low (" + userController.getEnergy() + ")");
        addChatMessage("⚠️ Energy low warning!");
    }

    public void showHealthWarning() {
        JOptionPane.showMessageDialog(this,
            "Warning! Health is very low (" + userController.getHealth() + ")");
        addChatMessage("⚠️ Health low warning!");
    }

    public void showBlockDialog(String reason, int penalty) {
        JOptionPane.showMessageDialog(this, reason + "! Account blocked for 48 hours. Penalty: $" + penalty);
    }
}
