package view;

import model.User;
import controller.GameController;
import controller.RoutineController;
import controller.UserController;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel {
    private User currentUser;
    private GameController controller;
    private RoutineController routineController;
    private UserController userController;
    private JLabel usernameLabel, balanceLabel, healthLabel, energyLabel;
    private JTextArea chatArea;
    private JPanel animationPanel;
    private JPanel rightPanel;

    private JButton buyFoodButton, visitDoctorButton, workButton;

    public GamePanel(User user) {
        this.currentUser = user;
        this.controller = new GameController(user);
        this.userController = controller.getUserController();
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
        usernameLabel = new JLabel("User: " + currentUser.getUsername());
        balanceLabel = new JLabel("Balance: $" + currentUser.getBalance());
        healthLabel = new JLabel("Health: " + currentUser.getHealth() + "/100");
        profilePanel.add(usernameLabel);
        profilePanel.add(balanceLabel);
        profilePanel.add(healthLabel);

        // Identity Panel
        JPanel identityPanel = new JPanel();
        identityPanel.setLayout(new BoxLayout(identityPanel, BoxLayout.Y_AXIS));
        identityPanel.setBorder(BorderFactory.createTitledBorder("Current Identity"));
        JLabel identityName = new JLabel(currentUser.getIdentity().getClass().getSimpleName());
        energyLabel = new JLabel("Energy: " + currentUser.getEnergy() + "/100000");
        identityPanel.add(identityName);
        identityPanel.add(energyLabel);

        // Marketplace Panel
        JPanel marketPanel = new JPanel();
        marketPanel.setLayout(new BoxLayout(marketPanel, BoxLayout.Y_AXIS));
        marketPanel.setBorder(BorderFactory.createTitledBorder("Marketplace"));
        buyFoodButton = new JButton("Buy Food (+Energy $50)");
        visitDoctorButton = new JButton("Visit Doctor (+Health $100)");
        workButton = new JButton("Do Daily Work");
        marketPanel.add(buyFoodButton);
        marketPanel.add(visitDoctorButton);
        marketPanel.add(workButton);

        rightPanel.add(profilePanel);
        rightPanel.add(Box.createVerticalStrut(10));
        rightPanel.add(identityPanel);
        rightPanel.add(Box.createVerticalStrut(10));
        rightPanel.add(marketPanel);

        add(rightPanel, BorderLayout.EAST);

        // ---------------- Center Animation / World View ----------------
        animationPanel = new JPanel();
        animationPanel.setBorder(BorderFactory.createTitledBorder("WORLD VIEW"));
        animationPanel.setBackground(Color.BLACK);
        add(animationPanel, BorderLayout.CENTER);

        // ---------------- Chat ----------------
        chatArea = new JTextArea(5,50);
        chatArea.setEditable(false);
        chatArea.setBorder(BorderFactory.createTitledBorder("Chat / Notifications"));
        JScrollPane chatScroll = new JScrollPane(chatArea);
        add(chatScroll, BorderLayout.SOUTH);

        // ---------------- Action Listeners ----------------
        buyFoodButton.addActionListener(e -> {
            GameController.ActionResult result = controller.buyFood();
            addChatMessage(result.message);
            updateUserInfo();
        });

        visitDoctorButton.addActionListener(e -> {
            GameController.ActionResult result = controller.visitDoctor();
            addChatMessage(result.message);
            updateUserInfo();
        });

        workButton.addActionListener(e -> {
            GameController.ActionResult result = controller.doWork();
            if ("FREELANCER_TASK_DIALOG".equals(result.message)) {
                showFreelancerTaskDialog();
            } else {
                addChatMessage(result.message);
                updateUserInfo();
            }
        });

        // ---------------- Energy Timer ----------------
        Timer energyTimer = new Timer(60_000, e -> routineController.decreaseEnergy());
        energyTimer.start();

        // ---------------- Sickness Timer ----------------
        Timer sicknessTimer = new Timer(24*60*60*1000, e -> routineController.sicknessCheck());
        sicknessTimer.start();

        // ---------------- Check if blocked ----------------
        if(currentUser.isBlocked()) {
            JOptionPane.showMessageDialog(this,
                "Your account is blocked until " + new java.util.Date(currentUser.getBlockedUntil()));
            disableActions();
        }
    }

    // Remove all game logic methods (buyFood, visitDoctor, doWork, decreaseEnergy, checkHealth, blockUserFor48Hours, sicknessCheck)
    // Keep only UI-related methods:

    public void updateUserInfo() {
        usernameLabel.setText("User: " + currentUser.getUsername());
        balanceLabel.setText("Balance: $" + currentUser.getBalance());
        healthLabel.setText("Health: " + currentUser.getHealth() + "/100");
        energyLabel.setText("Energy: " + currentUser.getEnergy() + "/100000");
    }

    public void addChatMessage(String msg) {
        chatArea.append(msg + "\n");
    }

    public JPanel getAnimationPanel() {
        return animationPanel;
    }

    public void disableActions() {
        buyFoodButton.setEnabled(false);
        visitDoctorButton.setEnabled(false);
        workButton.setEnabled(false);
    }

    // Add UI helper methods for controller:
    public void showEnergyWarning() {
        JOptionPane.showMessageDialog(this,
            "Warning! Energy low (" + currentUser.getEnergy() + ")");
        addChatMessage("⚠️ Energy low warning!");
    }

    public void showHealthWarning() {
        JOptionPane.showMessageDialog(this,
            "Warning! Health is very low (" + currentUser.getHealth() + ")");
        addChatMessage("⚠️ Health low warning!");
    }

    public void showBlockDialog(String reason, int penalty) {
        JOptionPane.showMessageDialog(this, reason + "! Account blocked for 48 hours. Penalty: $" + penalty);
    }

    public void showFreelancerTaskDialog() {
        TaskDialog dialog = new TaskDialog((JFrame) SwingUtilities.getWindowAncestor(this), currentUser);
        dialog.setVisible(true);
    }
}
