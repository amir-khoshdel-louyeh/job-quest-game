package view.theme;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JButton;

// Swaps a button's colors while the pointer is over it.
// A named listener instead of an anonymous adapter.
final class ButtonHoverListener extends MouseAdapter {
    private final JButton button;
    private final Color base;
    private final Color hover;
    private final Color hoverForeground;
    private final Color baseForeground;

    ButtonHoverListener(JButton button, Color base, Color hover,
                        Color hoverForeground, Color baseForeground) {
        this.button = button;
        this.base = base;
        this.hover = hover;
        this.hoverForeground = hoverForeground;
        this.baseForeground = baseForeground;
    }

    @Override
    public void mouseEntered(MouseEvent event) {
        button.setBackground(hover);
        if (hoverForeground != null) {
            button.setForeground(hoverForeground);
        }
    }

    @Override
    public void mouseExited(MouseEvent event) {
        button.setBackground(base);
        if (baseForeground != null) {
            button.setForeground(baseForeground);
        }
    }
}
