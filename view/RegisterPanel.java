package view;

import model.IdentityOption;
import model.User;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;


import model.Identity;


public class RegisterPanel extends JPanel {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton registerButton;
    private JButton backButton;
    private IdentityOption selectedOption;

    public RegisterPanel() {
        setLayout(new BorderLayout(10, 10));

        JLabel title = new JLabel("Create New Account", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        add(title, BorderLayout.NORTH);

        // 🔹 تعریف گزینه‌ها
        List<IdentityOption> options = new ArrayList<>();
        options.add(new IdentityOption("Freelancer", "Work online, earn flexible income.", 1000, "images/freelancer.png"));
        options.add(new IdentityOption("Chef", "Cook meals and manage a restaurant.", 1200, "images/chef.png"));
        options.add(new IdentityOption("Doctor", "Heal players and earn big money.", 1500, "images/doctor.png"));
        // اضافه کردن کار جدید راحت: options.add(new IdentityOption("Baker", "Bake breads.", 900, "images/baker.png"));

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

            // 🔹 ساخت Identity واقعی بر اساس گزینه انتخاب شده
            Identity identity;
            switch(selectedOption.getName()) {
                case "Freelancer":
                    identity = new model.Freelancer();
                    break;
                case "Chef":
                    identity = new model.Chef();
                    break;
                case "Doctor":
                    identity = new model.Doctor();
                    break;
                default:
                    identity = new model.Freelancer(); // پیش‌فرض
            }

            User newUser = new User(username, password, identity, 1000);

            JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
            topFrame.setContentPane(new GamePanel(newUser));
            topFrame.revalidate();
        });
    }
}
