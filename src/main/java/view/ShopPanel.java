package view;

import controller.UserController;
import javax.swing.*;
import java.awt.*;

public class ShopPanel extends JPanel {
    private UserController userController;

    public ShopPanel(UserController userController) {
        this.userController = userController;
        setLayout(new BorderLayout());
        add(new JLabel("Shop Panel - Placeholder", SwingConstants.CENTER), BorderLayout.CENTER);

        // Example: Buy button (future)
        JButton buyButton = new JButton("Buy Item ($100)");
        buyButton.addActionListener(e -> {
            if (userController.deductBalance(100)) {
                JOptionPane.showMessageDialog(this, "Purchase successful!");
            } else {
                JOptionPane.showMessageDialog(this, "Not enough balance!");
            }
        });
        add(buyButton, BorderLayout.SOUTH);
    }
}
