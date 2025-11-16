package view;

import model.Identity;
import model.player.User;
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

        List<Identity> options = IdentityOptionProvider.getAvailableOptions();
        JPanel optionsPanel = new JPanel();
        optionsPanel.setOpaque(false);
        optionsPanel.setLayout(new GridLayout(2, 2, 12, 12));
        identityGroup = new ButtonGroup();

        for (Identity option : options) {
            optionsPanel.add(createIdentityCard(option));
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

    private JPanel createIdentityCard(Identity option) {
        JPanel card = new JPanel(new BorderLayout(12, 12));
        card.setBackground(AppTheme.CARD_BACKGROUND);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(AppTheme.BORDER_COLOR, 1),
            BorderFactory.createEmptyBorder(16, 16, 16, 16)
        ));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 170));

        // Header row: picture and name side by side on one line.
        JRadioButton radioButton = new JRadioButton(
            option.getDisplayName() + " - Starting Balance: $" + option.getStartingBalance()
        );
        radioButton.setFont(AppTheme.FONT_SUBHEADING);
        radioButton.setForeground(AppTheme.TEXT_PRIMARY);
        radioButton.setOpaque(false);
        radioButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        identityGroup.add(radioButton);
        radioButton.putClientProperty("option", option);

        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        headerPanel.setOpaque(false);
        // Skipped silently if the picture file is not there yet.
        java.net.URL imageUrl = getClass().getResource(option.getImagePath());
        if (imageUrl != null) {
            ImageIcon icon = new ImageIcon(imageUrl);
            java.awt.Image scaled = icon.getImage().getScaledInstance(80, 80, java.awt.Image.SCALE_SMOOTH);
            headerPanel.add(new JLabel(new ImageIcon(scaled)));
        }
        headerPanel.add(radioButton);
        card.add(headerPanel, BorderLayout.NORTH);

        JTextArea desc = new JTextArea(option.getDescription());
        desc.setWrapStyleWord(true);
        desc.setLineWrap(true);
        desc.setEditable(false);
        desc.setOpaque(false);
        desc.setFont(AppTheme.FONT_BODY);
        desc.setForeground(AppTheme.TEXT_SECONDARY);
        desc.setBorder(BorderFactory.createEmptyBorder(8, 0, 0, 0));
        card.add(desc, BorderLayout.CENTER);

        card.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                radioButton.setSelected(true);
            }
        });

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
        Identity selected = getSelectedOption();
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
                IdentityProvider.createIdentity(selected.getProfessionName()),
                selected.getStartingBalance());
            user.setJobBonus(1);
            mainFrame.showPanel(new GamePanel(user));
        } catch (IllegalArgumentException ex) {
            errorLabel.setText("Error: " + ex.getMessage());
        }
    }

    private Identity getSelectedOption() {
        java.util.Enumeration<javax.swing.AbstractButton> buttons = identityGroup.getElements();
        while (buttons.hasMoreElements()) {
            javax.swing.AbstractButton button = buttons.nextElement();
            if (button.isSelected()) {
                return (Identity) button.getClientProperty("option");
            }
        }
        return null;
    }
}
