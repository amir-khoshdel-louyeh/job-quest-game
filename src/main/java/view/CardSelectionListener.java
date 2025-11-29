package view;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.AbstractButton;

// Selects a radio button when the card around it is clicked.
// A named listener instead of an anonymous adapter.
final class CardSelectionListener extends MouseAdapter {
    private final AbstractButton target;

    CardSelectionListener(AbstractButton target) {
        this.target = utils.Require.notNull(target, "target");
    }

    @Override
    public void mouseClicked(MouseEvent event) {
        target.setSelected(true);
    }
}
