package view;

import controller.GameController;
import model.Item;
import view.theme.AppTheme;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Step-6: inventory dialog — makes Step-3 storable items reachable.
 *
 * <p>Previously shop purchases went into a hidden inventory with no UI.
 * Pure view (SRP): lists owned items polymorphically via
 * {@link Item#getEffectDescription()} and delegates use to
 * {@link GameController#useItem(String)}. Stays open after use for
 * multi-consume, results go to chat + HUD via Observer.</p>
 */
public class InventoryDialog extends JDialog {

    private final GameController controller;
    private final GamePanel gamePanel;
    private final JPanel listContainer;

    public InventoryDialog(JFrame parent, GameController controller, GamePanel gamePanel) {
        super(parent, "🎒 Inventory", true);
        this.controller = controller;
        this.gamePanel = gamePanel;
        setLayout(new BorderLayout(10, 10));
        AppTheme.styleDialog(this, 450, 500);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JLabel title = new JLabel("Your items — click Use to apply (tools ♾️ reusable)", SwingConstants.CENTER);
        title.setFont(AppTheme.FONT_HEADING);
        title.setForeground(AppTheme.PRIMARY_COLOR);
        title.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));
        add(title, BorderLayout.NORTH);

        listContainer = new JPanel();
        listContainer.setLayout(new BoxLayout(listContainer, BoxLayout.Y_AXIS));
        listContainer.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        listContainer.setBackground(AppTheme.BACKGROUND_COLOR);

        refreshList();

        JScrollPane scroll = new JScrollPane(listContainer);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.setBorder(null);
        add(scroll, BorderLayout.CENTER);
    }

    /** Rebuild rows from the live inventory (called after each use). */
    private void refreshList() {
        listContainer.removeAll();
        List<Item> items = controller.getInventoryItems();
        if (items.isEmpty()) {
            JLabel empty = new JLabel("Inventory is empty. Visit the 🛍️ Shop or buy services.");
            empty.setFont(AppTheme.FONT_BODY);
            empty.setForeground(AppTheme.TEXT_SECONDARY);
            listContainer.add(empty);
        } else {
            // Group display by item: one row per owned instance is simplest
            // and honest about consumable counts.
            for (Item item : items) {
                listContainer.add(createRow(item));
                listContainer.add(Box.createVerticalStrut(8));
            }
        }
        listContainer.revalidate();
        listContainer.repaint();
    }

    private JPanel createRow(Item item) {
        JPanel row = AppTheme.createItemCard();
        JPanel info = new JPanel();
        info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));
        info.setOpaque(false);
        info.add(AppTheme.createCardTitle(item.getName() + (item.isConsumable() ? "" : " ♾️")));
        info.add(AppTheme.createCardDescription(item.getDescription() + "<br>Effect: " + item.getEffectDescription()
                + (item.isConsumable() ? "" : " (reusable)")));
        row.add(info, BorderLayout.CENTER);

        JButton use = AppTheme.createPrimaryButton("Use");
        final Item myItem = item;
        // Use item when clicked. Uses ActionListener (no lambda).
        use.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                GameController.ActionResult result = controller.useItem(myItem.getName());
                gamePanel.addChatMessage(result.message);
                gamePanel.updateUserInfo();
                if (!result.success) {
                    JOptionPane.showMessageDialog(InventoryDialog.this, result.message, "Cannot Use", JOptionPane.WARNING_MESSAGE);
                }
                refreshList();
            }
        });
        row.add(use, BorderLayout.EAST);
        return row;
    }
}
