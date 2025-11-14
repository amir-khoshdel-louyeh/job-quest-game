package view;

import model.IdentityOption;
import model.User;
import provider.IdentityOptionProvider;
import provider.IdentityProvider;
import view.theme.AppTheme;

import javax.swing.*;
import java.awt.*;
import java.util.List;

// The start screen: pick a career, enter a nickname, and start playing.
public class StartPanel extends JPanel {
    private MainFrame mainFrame;
    private JTextField nameField;
    private JButton startButton;
    private JLabel errorLabel;
    private ButtonGroup identityGroup;

    public StartPanel(MainFrame mainFrame) {
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

    private void initUI() {
        JPanel headerPanel = new JPanel();
        headerPanel.setOpaque(false);
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("🎮 Job Quest", SwingConstants.CENTER);
        title.setFont(AppTheme.FONT_TITLE);
        title.setForeground(AppTheme.PRIMARY_COLOR);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitle = new JLabel("Pick your career and start from 0");
        subtitle.setFont(AppTheme.FONT_BODY);
        subtitle.setForeground(AppTheme.TEXT_SECONDARY);
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        headerPanel.add(title);
        headerPanel.add(Box.createVerticalStrut(8));
        headerPanel.add(subtitle);
        headerPanel.add(Box.createVerticalStrut(20));
        add(headerPanel, BorderLayout.NORTH);

        List<IdentityOption> options = IdentityOptionProvider.getAvailableOptions();
        JPanel optionsPanel = new JPanel();
        optionsPanel.setOpaque(false);
        optionsPanel.setLayout(new BoxLayout(optionsPanel, BoxLayout.Y_AXIS));
        identityGroup = new ButtonGroup();

        for (IdentityOption option : options) {
            optionsPanel.add(createIdentityCard(option));
            optionsPanel.add(Box.createVerticalStrut(12));
        }

        JScrollPane scrollPane = new JScrollPane(optionsPanel);
        scrollPane.setBorder(null);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        add(scrollPane, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setOpaque(false);
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.Y_AXIS));

        JPanel formCard = AppTheme.createCard();
        formCard.setLayout(new BoxLayout(formCard, BoxLayout.Y_AXIS));
        formCard.setMaximumSize(new Dimension(600, 150));

        JLabel nameLabel = new JLabel("Nickname (optional)");
        nameLabel.setFont(AppTheme.FONT_BODY);
        nameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        nameField = AppTheme.createTextField(20);
        nameField.setMaximumSize(new Dimension(Integer.MAX_VALUE, AppTheme.INPUT_HEIGHT));
        nameField.setAlignmentX(Component.LEFT_ALIGNMENT);
        nameField.setText("Player");

        formCard.add(nameLabel);
        formCard.add(Box.createVerticalStrut(8));
        formCard.add(nameField);

        errorLabel = new JLabel(" ");
        errorLabel.setFont(AppTheme.FONT_SMALL);
        errorLabel.setForeground(AppTheme.ERROR_COLOR);
        errorLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        formCard.add(errorLabel);

        startButton = AppTheme.createSuccessButton("▶ Start Game");
        startButton.setPreferredSize(new Dimension(220, AppTheme.BUTTON_HEIGHT));
        startButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel buttonWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonWrapper.setOpaque(false);
        buttonWrapper.add(startButton);

        bottomPanel.add(formCard);
        bottomPanel.add(Box.createVerticalStrut(16));
        bottomPanel.add(buttonWrapper);

        JPanel bottomWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomWrapper.setOpaque(false);
        bottomWrapper.add(bottomPanel);
        add(bottomWrapper, BorderLayout.SOUTH);
    }

    private JPanel createIdentityCard(IdentityOption option) {
        JPanel card = new JPanel(new BorderLayout(12, 12));
        card.setBackground(AppTheme.CARD_BACKGROUND);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(AppTheme.BORDER_COLOR, 1),
            BorderFactory.createEmptyBorder(16, 16, 16, 16)
        ));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));

        JRadioButton radioButton = new JRadioButton(
            option.getName() + " - Starting Balance: $" + option.getPrice()
        );
        radioButton.setFont(AppTheme.FONT_SUBHEADING);
        radioButton.setForeground(AppTheme.TEXT_PRIMARY);
        radioButton.setOpaque(false);
        radioButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        identityGroup.add(radioButton);
        radioButton.putClientProperty("option", option);

        JTextArea desc = new JTextArea(option.getDescription());
        desc.setWrapStyleWord(true);
        desc.setLineWrap(true);
        desc.setEditable(false);
        desc.setOpaque(false);
        desc.setFont(AppTheme.FONT_BODY);
        desc.setForeground(AppTheme.TEXT_SECONDARY);
        desc.setBorder(BorderFactory.createEmptyBorder(8, 24, 0, 0));

        card.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                radioButton.setSelected(true);
            }
        });

        card.add(radioButton, BorderLayout.NORTH);
        card.add(desc, BorderLayout.CENTER);
        return card;
    }

    private void initActions() {
        startButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                startGame();
            }
        });
        nameField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                startGame();
            }
        });
    }

    private void startGame() {
        IdentityOption selected = getSelectedOption();
        if (selected == null) {
            errorLabel.setText("Please select a career path above.");
            return;
        }
        String name = nameField.getText().trim();
        if (name.equals("")) {
            name = "Player";
        }
        try {
            User user = new User(
                name,
                IdentityProvider.createIdentity(selected.getName()),
                selected.getPrice());
            user.setCurrentStreak(1);
            user.setLastLoginDate(System.currentTimeMillis());
            mainFrame.showPanel(new GamePanel(user));
        } catch (IllegalArgumentException ex) {
            errorLabel.setText("Error: " + ex.getMessage());
        }
    }

    private IdentityOption getSelectedOption() {
        java.util.Enumeration<javax.swing.AbstractButton> buttons = identityGroup.getElements();
        while (buttons.hasMoreElements()) {
            javax.swing.AbstractButton button = buttons.nextElement();
            if (button.isSelected()) {
                return (IdentityOption) button.getClientProperty("option");
            }
        }
        return null;
    }
}
