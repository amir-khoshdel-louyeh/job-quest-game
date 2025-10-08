package view;

import controller.LoginController;
import model.User;
import view.theme.AppTheme;

import javax.swing.*;
import java.awt.*;

/**
 * Professional login panel with modern design.
 */
public class LoginPanel extends JPanel {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton registerButton;
    private LoginController loginController = new LoginController();
    private MainFrame mainFrame;

    public LoginPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        setBackground(AppTheme.BACKGROUND_COLOR);
        initUI();
        initActions();
    }

    private void initUI() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        
        // Create a card panel for the login form
        JPanel cardPanel = AppTheme.createCard();
        cardPanel.setLayout(new BoxLayout(cardPanel, BoxLayout.Y_AXIS));
        cardPanel.setPreferredSize(new Dimension(450, 500));
        
        // Title with icon/emoji
        JLabel titleLabel = new JLabel("🎮 Job Quest", SwingConstants.CENTER);
        titleLabel.setFont(AppTheme.FONT_TITLE);
        titleLabel.setForeground(AppTheme.PRIMARY_COLOR);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
        
        JLabel subtitleLabel = new JLabel("Professional Career Simulation", SwingConstants.CENTER);
        subtitleLabel.setFont(AppTheme.FONT_BODY);
        subtitleLabel.setForeground(AppTheme.TEXT_SECONDARY);
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        subtitleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0));
        
        // Username field with label
        JLabel userLabel = new JLabel("Username");
        userLabel.setFont(AppTheme.FONT_BODY);
        userLabel.setForeground(AppTheme.TEXT_PRIMARY);
        userLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        usernameField = AppTheme.createTextField(20);
        usernameField.setMaximumSize(new Dimension(Integer.MAX_VALUE, AppTheme.INPUT_HEIGHT));
        usernameField.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Password field with label
        JLabel passLabel = new JLabel("Password");
        passLabel.setFont(AppTheme.FONT_BODY);
        passLabel.setForeground(AppTheme.TEXT_PRIMARY);
        passLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        passLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        
        passwordField = AppTheme.createPasswordField(20);
        passwordField.setMaximumSize(new Dimension(Integer.MAX_VALUE, AppTheme.INPUT_HEIGHT));
        passwordField.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Login button
        loginButton = AppTheme.createPrimaryButton("Login");
        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, AppTheme.BUTTON_HEIGHT));
        
        // Register button
        registerButton = AppTheme.createSecondaryButton("Create New Account");
        registerButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        registerButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, AppTheme.BUTTON_HEIGHT));
        
        // Add components to card
        cardPanel.add(titleLabel);
        cardPanel.add(subtitleLabel);
        cardPanel.add(userLabel);
        cardPanel.add(Box.createVerticalStrut(8));
        cardPanel.add(usernameField);
        cardPanel.add(passLabel);
        cardPanel.add(Box.createVerticalStrut(8));
        cardPanel.add(passwordField);
        cardPanel.add(Box.createVerticalStrut(30));
        cardPanel.add(loginButton);
        cardPanel.add(Box.createVerticalStrut(15));
        cardPanel.add(registerButton);
        cardPanel.add(Box.createVerticalGlue());
        
        // Add card to main panel
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(cardPanel, gbc);
        
        // Footer
        JLabel footerLabel = new JLabel("© 2025 Job Quest Game - Amir Khoshdel Louyeh");
        footerLabel.setFont(AppTheme.FONT_SMALL);
        footerLabel.setForeground(AppTheme.TEXT_HINT);
        gbc.gridy = 1;
        gbc.insets = new Insets(20, 0, 10, 0);
        add(footerLabel, gbc);
    }

    private void initActions() {
        loginButton.addActionListener(e -> performLogin());
        registerButton.addActionListener(e -> navigateToRegister());
        
        // Enter key support
        passwordField.addActionListener(e -> performLogin());
        usernameField.addActionListener(e -> passwordField.requestFocus());
    }

    private void performLogin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());

        if(username.isEmpty() || password.isEmpty()) {
            showError("Please enter both username and password!");
            return;
        }

        // Disable button during login
        loginButton.setEnabled(false);
        loginButton.setText("Logging in...");
        
        // Simulate async operation
        SwingWorker<User, Void> worker = new SwingWorker<>() {
            @Override
            protected User doInBackground() {
                return loginController.login(username, password);
            }
            
            @Override
            protected void done() {
                try {
                    User user = get();
                    if (user != null) {
                        // Clear fields on successful login
                        usernameField.setText("");
                        passwordField.setText("");
                        mainFrame.showPanel(new GamePanel(user));
                    } else {
                        showError("Login failed! Incorrect username or password.");
                    }
                } catch (Exception e) {
                    showError("An error occurred during login.");
                    e.printStackTrace();
                } finally {
                    loginButton.setEnabled(true);
                    loginButton.setText("Login");
                }
            }
        };
        worker.execute();
    }

    private void navigateToRegister() {
        mainFrame.showPanel(new RegisterPanel(mainFrame));
    }
    
    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Login Error", 
            JOptionPane.ERROR_MESSAGE);
    }
}
