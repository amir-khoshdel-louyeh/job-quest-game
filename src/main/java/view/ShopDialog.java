package view;

import controller.GameController;
import model.Item;

import javax.swing.*;
import java.util.List;

/**
 * A dialog window that hosts the ShopPanel.
 */
public class ShopDialog extends JDialog {

    public ShopDialog(JFrame parent, GameController gameController, GamePanel gamePanel, List<Item> items) {
        super(parent, "Shop", true); // 'true' for modal
        ShopPanel shopPanel = new ShopPanel(gameController, gamePanel, this, items);
        setContentPane(shopPanel);
        pack(); // Sizes the dialog to fit the preferred size of its content
        setLocationRelativeTo(parent);
    }
}