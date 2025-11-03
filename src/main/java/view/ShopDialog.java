package view;

import controller.GameController;
import model.Item;

import javax.swing.*;
import java.util.List;

/**
 * A dialog window that hosts the ShopPanel.
 */
public class ShopDialog extends JDialog {

    // Dialog wrapper that hosts the shop panel
    public ShopDialog(JFrame parent, GameController gameController, GamePanel gamePanel, List<Item> items) {
        super(parent, "Shop", true); // 'true' for modal
        // Create and set the shop content. Keep the dialog simple for beginners.
        ShopPanel shopPanel = new ShopPanel(gameController, gamePanel, this, items);
        setContentPane(shopPanel);
        pack(); // Size the dialog to fit the preferred size of its content
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }
}