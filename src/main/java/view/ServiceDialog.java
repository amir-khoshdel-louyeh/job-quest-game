package view;

import controller.GameController;
import model.Service;
import view.theme.AppTheme;
// ...existing code...

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * A dialog window that displays a list of purchasable services.
 */
public class ServiceDialog extends JDialog {

    private final GameController controller;
    private final GamePanel gamePanel;

    // Dialog showing available services the user can purchase
    public ServiceDialog(JFrame parent, GameController controller, GamePanel gamePanel, List<Service> services) {
        super(parent, "Available Services", true);
        this.controller = controller;
        this.gamePanel = gamePanel;
        setLayout(new BorderLayout(10, 10)); // Set layout before adding components
        // Step-5: shared dialog look
        AppTheme.styleDialog(this, 400, 500);
        // Keep dialog behavior simple for beginners: dispose when closed
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        // Panel to hold all the service cards
        JPanel servicesContainer = new JPanel();
        servicesContainer.setLayout(new BoxLayout(servicesContainer, BoxLayout.Y_AXIS));
        servicesContainer.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        servicesContainer.setBackground(AppTheme.BACKGROUND_COLOR);

        for (Service service : services) {
            servicesContainer.add(createServicePanel(service));
            servicesContainer.add(Box.createVerticalStrut(10));
        }

        // Make the list scrollable
        JScrollPane scrollPane = new JScrollPane(servicesContainer);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBorder(null);

        add(scrollPane, BorderLayout.CENTER);
    }

    // Create UI panel for a single service with buy action
    private JPanel createServicePanel(Service service) {
        // This UI creation logic is moved from GamePanel for a consistent look and feel.
        JPanel panel = AppTheme.createItemCard();

        // --- Top section for Name and Description ---
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setOpaque(false);

        JLabel nameLabel = AppTheme.createCardTitle(service.getName());
        nameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Show effect uniformly via Purchasable contract.
        JLabel descriptionLabel = AppTheme.createCardDescription(
                service.getDescription() + "<br>Effect: " + service.getEffectDescription());
        descriptionLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        infoPanel.add(nameLabel);
        infoPanel.add(Box.createVerticalStrut(2));
        infoPanel.add(descriptionLabel);

        panel.add(infoPanel, BorderLayout.CENTER);

        // --- Bottom section for Cost and Buy Button ---
        JPanel actionPanel = new JPanel(new BorderLayout());
        actionPanel.setOpaque(false);

        JLabel costLabel = new JLabel("$" + service.getCost());
        costLabel.setFont(AppTheme.FONT_HEADING);
        costLabel.setForeground(AppTheme.MONEY_COLOR.darker());
        actionPanel.add(costLabel, BorderLayout.WEST);

        JButton buyButton = AppTheme.createSuccessButton("Buy");
        // Buy when clicked. Uses simple ActionListener (no lambda).
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

    // Helper to handle service purchase flow. Keeps the UI method readable for beginners.
    // Step-5 UX: stays open for repeat buys; success goes to chat + HUD,
    // only failures pop a dialog (previously every purchase popped one).
    private void handleBuyService(Service service) {
        GameController.ActionResult result = controller.purchaseService(service.getName());
        gamePanel.addChatMessage(result.message);
        gamePanel.updateUserInfo();
        if (!result.success) {
            JOptionPane.showMessageDialog(this, result.message, "Cannot Purchase", JOptionPane.WARNING_MESSAGE);
        }
    }
}