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
    private JLabel errorLabel;
    // Controller handles the login logic (keeps UI and logic separated)
    private LoginController loginController = new LoginController();
    private MainFrame mainFrame;

    // Create the login panel and wire UI/actions
    public LoginPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        setBackground(AppTheme.BACKGROUND_COLOR);
        initUI();
        initActions();
    }

    // Build UI components and layout for the login form
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

        // Step-8: inline error label — empty/short input no longer needs a modal.
        errorLabel = new JLabel(" ");
        errorLabel.setFont(AppTheme.FONT_SMALL);
        errorLabel.setForeground(AppTheme.ERROR_COLOR);
        errorLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        errorLabel.setBorder(BorderFactory.createEmptyBorder(12, 0, 0, 0));
        
        // Login button
        loginButton = AppTheme.createPrimaryButton("Login");
        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, AppTheme.BUTTON_HEIGHT));
        
        // Register button
        registerButton = AppTheme.createSecondaryButton("Create New Account");
        registerButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        registerButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, AppTheme.BUTTON_HEIGHT));
        cardPanel.add(titleLabel);
        cardPanel.add(subtitleLabel);
        cardPanel.add(userLabel);
        cardPanel.add(Box.createVerticalStrut(8));
        cardPanel.add(usernameField);
        cardPanel.add(passLabel);
        cardPanel.add(Box.createVerticalStrut(8));
        cardPanel.add(passwordField);
        cardPanel.add(errorLabel);
        cardPanel.add(Box.createVerticalStrut(30));
        cardPanel.add(loginButton);
        cardPanel.add(Box.createVerticalStrut(15));
        cardPanel.add(registerButton);
        cardPanel.add(Box.createVerticalGlue());
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

    // Attach action listeners for buttons and fields
    // Uses simple ActionListener classes (beginner style, no lambda).
    private void initActions() {
        // When user clicks login -> try login
        loginButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                performLogin();
            }
        });
        registerButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                navigateToRegister();
            }
        });
        // Enter key in password triggers login
        passwordField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                performLogin();
            }
        });
        // Enter in username moves to password
        usernameField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                passwordField.requestFocus();
            }
        });
    }

    // Perform login asynchronously and handle navigation on success
    private void performLogin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());

        // Step-8: inline validation (OOP: rule lives next to the form, SRP).
        String error = validateInput(username, password);
        if (error != null) {
            showError(error);
            return;
        }
        clearError();

        // Disable button during login
        loginButton.setEnabled(false);
        loginButton.setText("Logging in...");

        // Do login in background so GUI does not freeze.
        // Uses SwingWorker with explicit types (beginner clear).
        SwingWorker<User, Void> worker = new SwingWorker<User, Void>() {

            protected User doInBackground() {
                return loginController.login(username, password);
            }
            
            
            protected void done() {
                try {
                    User user = get();
                    if (user != null) {
                        // Clear fields on successful login
                        usernameField.setText("");
                        passwordField.setText("");
                        mainFrame.showPanel(new GamePanel(user));
                    } else {
                        showError("Login failed: incorrect username or password.");
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

    // Navigate to the account registration panel
    private void navigateToRegister() {
        mainFrame.showPanel(new RegisterPanel(mainFrame));
    }
    
    // Show an error dialog with the provided message
    // Step-8: inline label first (non-blocking); modal only for auth failures.
    private void showError(String message) {
        errorLabel.setText(message);
    }

    private void clearError() {
        errorLabel.setText(" ");
    }

    /** Return error message or null if ok. Uses simple if. */
    private static String validateInput(String username, String password) {
        if (username.equals("") || password.equals("")) {
            return "Please enter both username and password.";
        }
        if (username.length() < 3) {
            return "Username must be at least 3 characters.";
        }
        if (password.length() < 4) {
            return "Password must be at least 4 characters.";
        }
        return null;
    }
}
