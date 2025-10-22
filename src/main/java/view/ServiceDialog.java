package view;

import controller.GameController;
import model.Service;
import provider.ServiceProvider;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * A dialog window that displays a list of purchasable services.
 */
public class ServiceDialog extends JDialog {

    private final GameController controller;
    private final GamePanel gamePanel;

    public ServiceDialog(JFrame parent, GameController controller, GamePanel gamePanel, List<Service> services) {
        super(parent, "Available Services", true);
        this.controller = controller;
        this.gamePanel = gamePanel;
        setLayout(new BorderLayout(10, 10)); // Set layout before adding components
        setSize(400, 500);
        setLocationRelativeTo(parent);

        // Panel to hold all the service cards
        JPanel servicesContainer = new JPanel();
        servicesContainer.setLayout(new BoxLayout(servicesContainer, BoxLayout.Y_AXIS));
        servicesContainer.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

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

    private JPanel createServicePanel(Service service) {
        // This UI creation logic is moved from GamePanel for a consistent look and feel.
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220)),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        panel.setBackground(Color.WHITE);

        // --- Top section for Name and Description ---
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setOpaque(false);

        JLabel nameLabel = new JLabel(service.getName());
        nameLabel.setFont(new Font("Arial", Font.BOLD, 14));
        nameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel descriptionLabel = new JLabel(service.getDescription());
        descriptionLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        descriptionLabel.setForeground(Color.GRAY);
        descriptionLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        infoPanel.add(nameLabel);
        infoPanel.add(Box.createVerticalStrut(2));
        infoPanel.add(descriptionLabel);

        panel.add(infoPanel, BorderLayout.CENTER);

        // --- Bottom section for Cost and Buy Button ---
        JPanel actionPanel = new JPanel(new BorderLayout());
        actionPanel.setOpaque(false);

        JLabel costLabel = new JLabel(String.format("$%d", service.getCost()));
        costLabel.setFont(new Font("Arial", Font.BOLD, 16));
        actionPanel.add(costLabel, BorderLayout.WEST);

        JButton buyButton = new JButton("Buy");
        buyButton.setBackground(new Color(25, 135, 84));
        buyButton.setForeground(Color.WHITE);
        buyButton.setFocusPainted(false);
        buyButton.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        buyButton.addActionListener(e -> {
            GameController.ActionResult result = controller.purchaseService(service.getName());
            if (result.success) {
                gamePanel.addChatMessage(result.message);
            }
            JOptionPane.showMessageDialog(this, result.message, "Service Used", result.success ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.ERROR_MESSAGE);
        });
        actionPanel.add(buyButton, BorderLayout.EAST);

        panel.add(actionPanel, BorderLayout.SOUTH);

        return panel;
    }
}