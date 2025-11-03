package view;

import controller.RegisterController;
import model.IdentityOption;
import provider.IdentityOptionProvider;
import view.theme.AppTheme;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Professional registration panel with modern design.
 */
public class RegisterPanel extends JPanel {
    private MainFrame mainFrame;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton registerButton;
    private JButton backButton;
    private RegisterController registerController = new RegisterController();
    private ButtonGroup identityGroup;
    public RegisterPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        setBackground(AppTheme.BACKGROUND_COLOR);
        setLayout(new BorderLayout(AppTheme.PADDING_LARGE, AppTheme.PADDING_LARGE));
        setBorder(BorderFactory.createEmptyBorder(
            AppTheme.PADDING_LARGE,
            AppTheme.PADDING_LARGE,
            AppTheme.PADDING_LARGE,
            AppTheme.PADDING_LARGE
        ));

        initUI();
        initActions();
    }

    // Build UI components for the registration screen
    private void initUI() {
        // Header Panel
        JPanel headerPanel = new JPanel();
        headerPanel.setOpaque(false);
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        
        JLabel title = new JLabel("Create Your Account");
        title.setFont(AppTheme.FONT_TITLE);
        title.setForeground(AppTheme.PRIMARY_COLOR);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel subtitle = new JLabel("Choose your career path and start your journey");
        subtitle.setFont(AppTheme.FONT_BODY);
        subtitle.setForeground(AppTheme.TEXT_SECONDARY);
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        headerPanel.add(title);
        headerPanel.add(Box.createVerticalStrut(8));
        headerPanel.add(subtitle);
        headerPanel.add(Box.createVerticalStrut(20));
        
        add(headerPanel, BorderLayout.NORTH);

        // Center Panel with Identity Options
        List<IdentityOption> options = IdentityOptionProvider.getAvailableOptions();

        JPanel optionsPanel = new JPanel();
        optionsPanel.setOpaque(false);
        optionsPanel.setLayout(new BoxLayout(optionsPanel, BoxLayout.Y_AXIS));

        identityGroup = new ButtonGroup();
        
        for(IdentityOption option : options) {
            JPanel optionCard = createIdentityCard(option);
            optionsPanel.add(optionCard);
            optionsPanel.add(Box.createVerticalStrut(12));
        }

        JScrollPane scrollPane = new JScrollPane(optionsPanel);
        scrollPane.setBorder(null);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        
        add(scrollPane, BorderLayout.CENTER);

        // Bottom Form Panel
        JPanel bottomPanel = new JPanel();
        bottomPanel.setOpaque(false);
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.Y_AXIS));
        
        JPanel formCard = AppTheme.createCard();
        formCard.setLayout(new BoxLayout(formCard, BoxLayout.Y_AXIS));
        formCard.setMaximumSize(new Dimension(600, 250));
        formCard.setPreferredSize(new Dimension(600, 250));
        
        // Username
        JLabel userLabel = new JLabel("Username");
        userLabel.setFont(AppTheme.FONT_BODY);
        userLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        usernameField = AppTheme.createTextField(20);
        usernameField.setMaximumSize(new Dimension(Integer.MAX_VALUE, AppTheme.INPUT_HEIGHT));
        usernameField.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Password
        JLabel passLabel = new JLabel("Password");
        passLabel.setFont(AppTheme.FONT_BODY);
        passLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        passwordField = AppTheme.createPasswordField(20);
        passwordField.setMaximumSize(new Dimension(Integer.MAX_VALUE, AppTheme.INPUT_HEIGHT));
        passwordField.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        formCard.add(userLabel);
        formCard.add(Box.createVerticalStrut(8));
        formCard.add(usernameField);
        formCard.add(Box.createVerticalStrut(16));
        formCard.add(passLabel);
        formCard.add(Box.createVerticalStrut(8));
        formCard.add(passwordField);
        
        // Buttons Panel
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 0));
        buttonsPanel.setOpaque(false);
        
        registerButton = AppTheme.createSuccessButton("Create Account");
        registerButton.setPreferredSize(new Dimension(180, AppTheme.BUTTON_HEIGHT));
        
        backButton = AppTheme.createSecondaryButton("Back to Login");
        backButton.setPreferredSize(new Dimension(180, AppTheme.BUTTON_HEIGHT));
        
        buttonsPanel.add(registerButton);
        buttonsPanel.add(backButton);
        
        bottomPanel.add(formCard);
        bottomPanel.add(Box.createVerticalStrut(16));
        bottomPanel.add(buttonsPanel);
        
        // Center the bottom panel
        JPanel bottomWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomWrapper.setOpaque(false);
        bottomWrapper.add(bottomPanel);
        
        add(bottomWrapper, BorderLayout.SOUTH);
    }

    // Create a selectable identity option card
    private JPanel createIdentityCard(IdentityOption option) {
        JPanel card = new JPanel();
        card.setLayout(new BorderLayout(12, 12));
        card.setBackground(AppTheme.CARD_BACKGROUND);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(AppTheme.BORDER_COLOR, 1),
            BorderFactory.createEmptyBorder(16, 16, 16, 16)
        ));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
        
        // Radio button with name and price
        JRadioButton radioButton = new JRadioButton(
            String.format("%s - Starting Balance: $%d", option.getName(), option.getPrice())
        );
        radioButton.setFont(AppTheme.FONT_SUBHEADING);
        radioButton.setForeground(AppTheme.TEXT_PRIMARY);
        radioButton.setOpaque(false);
        radioButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        identityGroup.add(radioButton);
        radioButton.putClientProperty("option", option);
        
        // Description
        JTextArea desc = new JTextArea(option.getDescription());
        desc.setWrapStyleWord(true);
        desc.setLineWrap(true);
        desc.setEditable(false);
        desc.setOpaque(false);
        desc.setFont(AppTheme.FONT_BODY);
        desc.setForeground(AppTheme.TEXT_SECONDARY);
        desc.setBorder(BorderFactory.createEmptyBorder(8, 24, 0, 0));
        card.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                card.setBackground(AppTheme.PRIMARY_LIGHT.brighter().brighter());
                card.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(AppTheme.PRIMARY_COLOR, 2),
                    BorderFactory.createEmptyBorder(15, 15, 15, 15)
                ));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                card.setBackground(AppTheme.CARD_BACKGROUND);
                card.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(AppTheme.BORDER_COLOR, 1),
                    BorderFactory.createEmptyBorder(16, 16, 16, 16)
                ));
            }
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                radioButton.setSelected(true);
            }
        });
        
        card.add(radioButton, BorderLayout.NORTH);
        card.add(desc, BorderLayout.CENTER);
        
        return card;
    }

    // Attach action listeners for registration and navigation
    private void initActions() {
        registerButton.addActionListener(e -> performRegistration());
        backButton.addActionListener(e -> navigateToLogin());
        passwordField.addActionListener(e -> performRegistration());
    }

    // Validate input and perform account registration
    private void performRegistration() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());

        IdentityOption selectedOption = getSelectedOption();

        if (username.isEmpty() || password.isEmpty() || selectedOption == null) {
            JOptionPane.showMessageDialog(this, 
                "Please fill all fields and select a career path!",
                "Registration Error",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Disable button during registration
        registerButton.setEnabled(false);
        registerButton.setText("Creating Account...");

        try {
            String identityStr = selectedOption.getName();
            int startingBalance = selectedOption.getPrice();

            boolean success = registerController.register(username, password, identityStr, startingBalance);

            if (success) {
                JOptionPane.showMessageDialog(this, 
                    "Registration successful! You can now login.",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
                navigateToLogin();
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Registration failed! Username may already exist.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
                registerButton.setEnabled(true);
                registerButton.setText("Create Account");
            }
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
            registerButton.setEnabled(true);
            registerButton.setText("Create Account");
        }
    }

    // Navigate back to the login panel and reset form
    private void navigateToLogin() {
        usernameField.setText("");
        passwordField.setText("");
        identityGroup.clearSelection();
        mainFrame.showPanel(new LoginPanel(mainFrame));
    }
    private IdentityOption getSelectedOption() {
        for (AbstractButton button : java.util.Collections.list(identityGroup.getElements())) {
            if (button.isSelected()) {
                return (IdentityOption) button.getClientProperty("option");
            }
        }
        return null;
    }
}
