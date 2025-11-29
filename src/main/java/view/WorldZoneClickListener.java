package view;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

// Forwards a click on the world map to the panel's zone routing.
// A named listener instead of an anonymous adapter.
final class WorldZoneClickListener extends MouseAdapter {
    private final WorldViewPanel panel;

    WorldZoneClickListener(WorldViewPanel panel) {
        this.panel = utils.Require.notNull(panel, "panel");
    }

    @Override
    public void mouseClicked(MouseEvent event) {
        panel.openZone(event.getX());
    }
}
