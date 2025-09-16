package view;

import model.User;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel {
    private User currentUser;
    private JLabel usernameLabel, balanceLabel, healthLabel, energyLabel;
    private JTextArea chatArea;
    private JPanel animationPanel;
    private JPanel rightPanel;

    private JButton buyFoodButton, visitDoctorButton, workButton;

    public GamePanel(User user) {
        this.currentUser = user;
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
        buyFoodButton.addActionListener(e -> buyFood());
        visitDoctorButton.addActionListener(e -> visitDoctor());
        workButton.addActionListener(e -> doWork());

        // ---------------- Energy Timer ----------------
        Timer energyTimer = new Timer(60_000, e -> decreaseEnergy());
        energyTimer.start();

        // ---------------- Sickness Timer ----------------
        Timer sicknessTimer = new Timer(24*60*60*1000, e -> sicknessCheck());
        sicknessTimer.start();

        // ---------------- Check if blocked ----------------
        if(currentUser.isBlocked()) {
            JOptionPane.showMessageDialog(this,
                "Your account is blocked until " + new java.util.Date(currentUser.getBlockedUntil()));
            disableActions();
        }
    }

    // ---------------- Action Methods ----------------
    private void buyFood() {
        if(currentUser.getBalance() >= 50) {
            currentUser.setBalance(currentUser.getBalance() - 50);
            currentUser.setEnergy(Math.min(100_000, currentUser.getEnergy() + 5000)); // +5000 انرژی
            addChatMessage(currentUser.getUsername() + " bought food (+Energy, -$50)");
            updateUserInfo();
        } else {
            addChatMessage("Not enough money to buy food!");
        }
    }

    private void visitDoctor() {
        if(currentUser.getBalance() >= 100) {
            currentUser.setBalance(currentUser.getBalance() - 100);
            currentUser.setHealth(Math.min(100, currentUser.getHealth() + 20));
            addChatMessage(currentUser.getUsername() + " visited doctor (+Health, -$100)");
            updateUserInfo();
        } else {
            addChatMessage("Not enough money to visit doctor!");
        }
    }


    private void doWork() {
        if(currentUser.getIdentity().getClass().getSimpleName().equals("Freelancer")) {
            // باز کردن پنجره تسک فریلنسری
            FreelancerTaskDialog dialog = new FreelancerTaskDialog((JFrame) SwingUtilities.getWindowAncestor(this), currentUser);
            dialog.setVisible(true);
        } else {
            // بقیه کارها مثل Chef یا Doctor
            currentUser.getIdentity().performDailyWork(currentUser);
            addChatMessage(currentUser.getUsername() + " did daily work. Current balance: $" + currentUser.getBalance());
        }
        updateUserInfo();
    }

    // ---------------- Energy and Health Management ----------------
    private void decreaseEnergy() {
        if(currentUser.getEnergy() > 0) {
            currentUser.setEnergy(currentUser.getEnergy() - 1);
            updateUserInfo();

            if(currentUser.getEnergy() <= 10_000 && currentUser.getEnergy() > 0) {
                JOptionPane.showMessageDialog(this,
                    "Warning! Energy low (" + currentUser.getEnergy() + ")");
                addChatMessage("⚠️ Energy low warning!");
            }

            if(currentUser.getEnergy() == 0) {
                blockUserFor48Hours("Energy reached 0");
            }
        }
    }

    private void checkHealth() {
        if(currentUser.getHealth() <= 20 && currentUser.getHealth() > 0) {
            JOptionPane.showMessageDialog(this,
                "Warning! Health is very low (" + currentUser.getHealth() + ")");
            addChatMessage("⚠️ Health low warning!");
        }

        if(currentUser.getHealth() == 0) {
            blockUserFor48Hours("Health reached 0");
        }
    }

    private void blockUserFor48Hours(String reason) {
        int penalty = 2000;
        currentUser.setBalance(Math.max(0, currentUser.getBalance() - penalty));
        addChatMessage("❌ " + currentUser.getUsername() + " is blocked for 48 hours due to: " + reason + ". Penalty: $" + penalty);
        JOptionPane.showMessageDialog(this, reason + "! Account blocked for 48 hours. Penalty: $" + penalty);

        long unblockTime = System.currentTimeMillis() + 48 * 60 * 60 * 1000L;
        currentUser.setBlockedUntil(unblockTime);
        disableActions();
    }

    // ---------------- Random Sickness ----------------
    private void sicknessCheck() {
        long now = System.currentTimeMillis();
        long lastSick = currentUser.getLastSicknessTime();

        boolean forcedSickness = (now - lastSick) >= 72 * 60 * 60 * 1000L;
        boolean randomSickness = Math.random() < 0.2;

        if(forcedSickness || randomSickness) {
            int healthLoss = (int)(currentUser.getHealth() * 0.05);
            currentUser.setHealth(Math.max(0, currentUser.getHealth() - healthLoss));
            currentUser.setLastSicknessTime(now);
            addChatMessage("💀 " + currentUser.getUsername() + " got sick! Lost 5% health.");
            updateUserInfo();
            checkHealth();
        }
    }

    // ---------------- Utility ----------------
    private void disableActions() {
        buyFoodButton.setEnabled(false);
        visitDoctorButton.setEnabled(false);
        workButton.setEnabled(false);
    }

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
}
