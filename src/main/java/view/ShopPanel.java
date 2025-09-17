package view;

import controller.UserController;
import model.ShopItem;
import model.ShopItemProvider;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ShopPanel extends JPanel {
    private UserController userController;

    public ShopPanel(UserController userController) {
        this.userController = userController;
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Title
        JLabel titleLabel = new JLabel("Welcome to the Shop", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        add(titleLabel, BorderLayout.NORTH);

        // Panel for the list of items
        JPanel itemsGridPanel = new JPanel();
        // Using a flexible grid layout that wraps
        itemsGridPanel.setLayout(new GridLayout(0, 3, 15, 15)); // 0 rows, 3 columns, with gaps

        List<ShopItem> itemsForSale = ShopItemProvider.getAvailableItems();

        for (ShopItem item : itemsForSale) {
            itemsGridPanel.add(createItemPanel(item));
        }

        // Add the grid to a scroll pane in case there are many items
        JScrollPane scrollPane = new JScrollPane(itemsGridPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        add(scrollPane, BorderLayout.CENTER);
    }

    private JPanel createItemPanel(ShopItem item) {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(BorderFactory.createTitledBorder(item.getName()));

        // Description
        JTextArea descriptionArea = new JTextArea(item.getDescription());
        descriptionArea.setWrapStyleWord(true);
        descriptionArea.setLineWrap(true);
        descriptionArea.setEditable(false);
        descriptionArea.setBackground(panel.getBackground());
        panel.add(descriptionArea, BorderLayout.CENTER);

        // Buy Button
        JButton buyButton = new JButton("Buy ($" + item.getPrice() + ")");
        buyButton.addActionListener(e -> {
            if (userController.deductBalance(item.getPrice())) {
                userController.addItemToInventory(item.getName()); // Delegate to UserController
                JOptionPane.showMessageDialog(this, "You bought " + item.getName() + "!");
                // You might want to update the main GamePanel's info here as well
            } else {
                JOptionPane.showMessageDialog(this, "Not enough money to buy " + item.getName() + ".");
            }
        });
        panel.add(buyButton, BorderLayout.SOUTH);

        return panel;
    }
}
