package view;

/**
 * UI panel for the in-game shop where users can browse and buy items or services.
 *
 * Follows Single Responsibility Principle by encapsulating shop UI logic only.
 */

import controller.UserController;
import controller.GameController;
import model.Item;
import view.theme.AppTheme;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ShopPanel extends JPanel {
    private GameController gameController;
    private GamePanel gamePanel;
    private JDialog parentDialog; 
    private UserController userController;

    // Construct the shop panel listing items for sale
    public ShopPanel(GameController gameController, GamePanel gamePanel, JDialog parentDialog, List<Item> itemsForSale) {
        this.gameController = gameController;
        this.userController = gameController.getUserController();
        this.gamePanel = gamePanel;
        this.parentDialog = parentDialog;
        setLayout(new BorderLayout(10, 10)); // Set layout before adding components
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setBackground(AppTheme.BACKGROUND_COLOR);

        // Title
        JLabel titleLabel = new JLabel("Welcome to the Shop", SwingConstants.CENTER);
        titleLabel.setFont(AppTheme.FONT_HEADING);
        titleLabel.setForeground(AppTheme.PRIMARY_COLOR);
        add(titleLabel, BorderLayout.NORTH);

        // Panel for the list of items
        JPanel itemsGridPanel = new JPanel();
        // Using a flexible grid layout that wraps
        itemsGridPanel.setLayout(new GridLayout(0, 3, 15, 15)); // 0 rows, 3 columns, with gaps
        itemsGridPanel.setBackground(AppTheme.BACKGROUND_COLOR);

        for (Item item : itemsForSale) {
            itemsGridPanel.add(createItemPanel(item));
        }

        // Add the grid to a scroll pane in case there are many items
        JScrollPane scrollPane = new JScrollPane(itemsGridPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setBorder(null);
        add(scrollPane, BorderLayout.CENTER);
    }

    // Create UI panel for a single shop item with buy action
    private JPanel createItemPanel(Item item) {
        JPanel panel = AppTheme.createItemCard();
        panel.setLayout(new BorderLayout(5, 5));

        // Title + effect (Step-3 items now carry real effects)
        JLabel nameLabel = AppTheme.createCardTitle(item.getName());
        panel.add(nameLabel, BorderLayout.NORTH);

        // Description
        JTextArea descriptionArea = new JTextArea(item.getDescription() + "\nEffect: " + item.getEffectDescription());
        descriptionArea.setWrapStyleWord(true);
        descriptionArea.setLineWrap(true);
        descriptionArea.setEditable(false);
        descriptionArea.setFont(AppTheme.FONT_SMALL);
        descriptionArea.setForeground(AppTheme.TEXT_SECONDARY);
        descriptionArea.setBackground(AppTheme.CARD_BACKGROUND);
        panel.add(descriptionArea, BorderLayout.CENTER);

        // Buy Button. Uses ActionListener (no lambda).
        JButton buyButton = AppTheme.createSuccessButton("Buy ($" + item.getPrice() + ")");
        final Item myItem = item;
        buyButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                handleBuy(myItem);
            }
        });
        panel.add(buyButton, BorderLayout.SOUTH);

        return panel;
    }

    // Helper that performs the purchase flow with small client-side checks.
    // Step-5 UX: stays open after success for multi-buy (previously dispose()
    // closed the shop every purchase); result goes to chat + HUD.
    private void handleBuy(Item item) {
        // Quick client-side checks before requesting purchase
        if (userController.isBlocked()) {
            JOptionPane.showMessageDialog(this,
                    "Your account is blocked. You cannot make purchases.",
                    "Purchase Failed",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (userController.getBalance() < item.getPrice()) {
            JOptionPane.showMessageDialog(this,
                    "You don't have enough money to buy this item.",
                    "Purchase Failed",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        GameController.ActionResult result = gameController.purchaseItem(item);
        gamePanel.addChatMessage(result.message);
        gamePanel.updateUserInfo();
        if (!result.success) {
            JOptionPane.showMessageDialog(this, result.message, "Purchase Failed", JOptionPane.WARNING_MESSAGE);
        }
    }
}
