package view;

import controller.RegisterController;
import model.IdentityOption;
import model.User;
import model.Identity;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class RegisterPanel extends JPanel {
    private MainFrame mainFrame;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton registerButton;
    private JButton backButton;
    private IdentityOption selectedOption;
    private RegisterController registerController = new RegisterController();

    public RegisterPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        setLayout(new BorderLayout(10, 10));

        JLabel title = new JLabel("Create New Account", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        add(title, BorderLayout.NORTH);

        // 🔹 تعریف گزینه‌ها
        List<IdentityOption> options = new ArrayList<>();
        options.add(new IdentityOption("Freelancer", "Work online, earn flexible income.", 1000, "images/freelancer.png"));
        options.add(new IdentityOption("Chef", "Cook meals and manage a restaurant.", 1200, "images/chef.png"));
        options.add(new IdentityOption("Doctor", "Heal players and earn big money.", 1500, "images/doctor.png"));

        // پنل عمودی برای گزینه‌ها
        JPanel optionsPanel = new JPanel();
        optionsPanel.setLayout(new BoxLayout(optionsPanel, BoxLayout.Y_AXIS));
        optionsPanel.setBorder(BorderFactory.createEmptyBorder(10, 50, 10, 50));

        ButtonGroup group = new ButtonGroup();
        for(IdentityOption option : options) {
            JRadioButton radioButton = new JRadioButton(option.getName() + " ($" + option.getPrice() + ")");
            group.add(radioButton);

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

            radioButton.addActionListener(e -> selectedOption = option);
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

        // اکشن‌ها
        registerButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            if(username.isEmpty() || password.isEmpty() || selectedOption == null) {
                JOptionPane.showMessageDialog(this, "Please fill all fields and select an option!");
                return;
            }

            // 🔹 انتخاب Identity با else if
            Identity identityObj;
            if(selectedOption.getName().equals("Freelancer")) {
                identityObj = new model.Freelancer();
            } else if(selectedOption.getName().equals("Chef")) {
                identityObj = new model.Chef();
            } else if(selectedOption.getName().equals("Doctor")) {
                identityObj = new model.Doctor();
            } else {
                identityObj = new model.Freelancer();
            }

            // تبدیل Identity به String برای دیتابیس و گرفتن بالانس
            String identityStr = identityObj.getClass().getSimpleName();
            int startingBalance = selectedOption.getPrice();

            // ثبت کاربر در دیتابیس
            boolean success = registerController.register(username, password, identityStr, startingBalance);

            if (success) {
                User newUser = new User(username, password, identityObj, startingBalance);
                mainFrame.showPanel(new GamePanel(newUser));
            } else {
                JOptionPane.showMessageDialog(this, "Registration failed! Username may already exist.");
            }
        });
    }
}
