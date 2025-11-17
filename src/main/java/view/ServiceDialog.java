package view;

import controller.GameController;
import model.store.Service;
import view.theme.AppTheme;

import javax.swing.*;
import java.awt.*;
import java.util.List;

// The shop screen: buy services to restore health or energy.
public class ServiceDialog extends JDialog {

    private final GameController controller;
    private final GamePanel gamePanel;

    public ServiceDialog(JFrame parent, GameController controller, GamePanel gamePanel, List<Service> services) {
        super(parent, "Available Services", true);
        this.controller = controller;
        this.gamePanel = gamePanel;
        setLayout(new BorderLayout(10, 10));
        AppTheme.styleDialog(this, 400, 500);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel servicesContainer = new JPanel();
        servicesContainer.setLayout(new BoxLayout(servicesContainer, BoxLayout.Y_AXIS));
        servicesContainer.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        servicesContainer.setBackground(AppTheme.BACKGROUND_COLOR);

        for (Service service : services) {
            servicesContainer.add(createServicePanel(service));
            servicesContainer.add(Box.createVerticalStrut(10));
        }

        JScrollPane scrollPane = new JScrollPane(servicesContainer);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBorder(null);

        add(scrollPane, BorderLayout.CENTER);
    }

    private JPanel createServicePanel(Service service) {
        JPanel panel = AppTheme.createItemCard();

        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setOpaque(false);

        JLabel nameLabel = AppTheme.createCardTitle(service.getName());
        nameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel descriptionLabel = AppTheme.createCardDescription(
                service.getDescription() + "<br>Effect: " + utils.EffectText.formatServiceEffect(service.getEnergyEffect(), service.getHealthEffect()));
        descriptionLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        infoPanel.add(nameLabel);
        infoPanel.add(Box.createVerticalStrut(2));
        infoPanel.add(descriptionLabel);

        panel.add(infoPanel, BorderLayout.CENTER);

        JPanel actionPanel = new JPanel(new BorderLayout());
        actionPanel.setOpaque(false);

        JLabel costLabel = new JLabel("$" + service.getPrice());
        costLabel.setFont(AppTheme.FONT_HEADING);
        costLabel.setForeground(AppTheme.MONEY_COLOR.darker());
        actionPanel.add(costLabel, BorderLayout.WEST);

        JButton buyButton = AppTheme.createSuccessButton("Buy");
        final Service myService = service;
        buyButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                handleBuyService(myService);
            }
        });
        actionPanel.add(buyButton, BorderLayout.EAST);

        panel.add(actionPanel, BorderLayout.SOUTH);

        return panel;
    }

    // Stays open for repeat buys; success goes to chat, only failures pop up.
    private void handleBuyService(Service service) {
        GameController.ActionResult result = controller.purchaseService(service.getName());
        gamePanel.addChatMessage(result.getMessage());
        gamePanel.updateUserInfo();
        if (!result.isSuccess()) {
            JOptionPane.showMessageDialog(this, result.getMessage(), "Cannot Purchase", JOptionPane.WARNING_MESSAGE);
        }
    }
}