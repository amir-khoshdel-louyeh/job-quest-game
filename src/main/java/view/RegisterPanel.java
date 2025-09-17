package view;

import controller.RegisterController;
import model.IdentityFactory;
import model.IdentityOption;
import model.IdentityOptionProvider;
import model.Identity;

import javax.swing.*;
import java.awt.*;
import java.util.List;

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
        setLayout(new BorderLayout(10, 10));

        initUI();
        initActions();
    }

    private void initUI() {

        JLabel title = new JLabel("Create New Account", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        add(title, BorderLayout.NORTH);

        // Get options from the central provider instead of defining them here.
        List<IdentityOption> options = IdentityOptionProvider.getAvailableOptions();

        // پنل عمودی برای گزینه‌ها
        JPanel optionsPanel = new JPanel();
        optionsPanel.setLayout(new BoxLayout(optionsPanel, BoxLayout.Y_AXIS));
        optionsPanel.setBorder(BorderFactory.createEmptyBorder(10, 50, 10, 50));

        identityGroup = new ButtonGroup();
        for(IdentityOption option : options) {
            JRadioButton radioButton = new JRadioButton(option.getName() + " ($" + option.getPrice() + ")");
            identityGroup.add(radioButton);

            JTextArea desc = new JTextArea(option.getDescription());
            desc.setWrapStyleWord(true);
            desc.setLineWrap(true);
            desc.setEditable(false);
            desc.setBackground(getBackground());

            JPanel optionPanel = new JPanel(new BorderLayout());
            optionPanel.setBorder(BorderFactory.createTitledBorder(option.getName()));
            optionPanel.add(radioButton, BorderLayout.NORTH);
            optionPanel.add(desc, BorderLayout.CENTER);
            optionPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));

            optionsPanel.add(optionPanel);
            optionsPanel.add(Box.createVerticalStrut(10));

            // Attach the data model directly to the UI component
            radioButton.putClientProperty("option", option);
        }

        JScrollPane scrollPane = new JScrollPane(optionsPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        add(scrollPane, BorderLayout.CENTER);

        // فرم ثبت‌نام
        JPanel formPanel = new JPanel(new GridLayout(2,2,5,5));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10,50,10,50));

        formPanel.add(new JLabel("Username:"));
        usernameField = new JTextField();
        formPanel.add(usernameField);

        formPanel.add(new JLabel("Password:"));
        passwordField = new JPasswordField();
        formPanel.add(passwordField);

        registerButton = new JButton("Register");
        backButton = new JButton("Back to Login");

        JPanel buttonsPanel = new JPanel(new GridLayout(1,2,10,10));
        buttonsPanel.add(registerButton);
        buttonsPanel.add(backButton);

        JPanel southPanel = new JPanel(new BorderLayout());
        southPanel.add(formPanel, BorderLayout.CENTER);
        southPanel.add(buttonsPanel, BorderLayout.SOUTH);

        add(southPanel, BorderLayout.SOUTH);
    }

    private void initActions() {
        registerButton.addActionListener(e -> performRegistration());
        backButton.addActionListener(e -> navigateToLogin());
    }

    private void performRegistration() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        IdentityOption selectedOption = getSelectedOption();

        if (username.isEmpty() || password.isEmpty() || selectedOption == null) {
            JOptionPane.showMessageDialog(this, "Please fill all fields and select an identity!");
            return;
        }

        try {
            // Get details for registration
            String identityStr = selectedOption.getName();
            int startingBalance = selectedOption.getPrice();

            boolean success = registerController.register(username, password, identityStr, startingBalance);

            if (success) {
                JOptionPane.showMessageDialog(this, "Registration successful! Please log in.");
                navigateToLogin();
            } else {
                JOptionPane.showMessageDialog(this, "Registration failed! Username may already exist.");
            }
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void navigateToLogin() {
        // Clear fields when navigating away
        usernameField.setText("");
        passwordField.setText("");
        // Clear the radio button selection
        identityGroup.clearSelection();

        mainFrame.showPanel(new LoginPanel(mainFrame));
    }

    /**
     * Finds the selected radio button and returns its associated IdentityOption.
     * @return The selected IdentityOption, or null if none is selected.
     */
    private IdentityOption getSelectedOption() {
        for (AbstractButton button : java.util.Collections.list(identityGroup.getElements())) {
            if (button.isSelected()) {
                return (IdentityOption) button.getClientProperty("option");
            }
        }
        return null;
    }
}
