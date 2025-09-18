package view;

import controller.GameController;

import javax.swing.*;

/**
 * A dialog window that hosts the ShopPanel.
 */
public class ShopDialog extends JDialog {

    public ShopDialog(JFrame parent, GameController gameController, GamePanel gamePanel) {
        super(parent, "Shop", true); // 'true' for modal
        ShopPanel shopPanel = new ShopPanel(gameController, gamePanel, this);
        setContentPane(shopPanel);
        pack(); // Sizes the dialog to fit the preferred size of its content
        setLocationRelativeTo(parent);
    }
}