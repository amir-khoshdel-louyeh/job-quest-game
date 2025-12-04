package view;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;

// Ends the game. A named action so SessionPanel needs no anonymous class and
// the exit behaviour has exactly one owner.
public final class ExitAction extends AbstractAction {
    private static final long serialVersionUID = 1L;

    ExitAction() {
        super("❌ Exit");
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        System.exit(0);
    }
}
