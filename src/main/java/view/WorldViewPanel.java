package view;

import java.awt.*;
import javax.swing.*;

import view.contract.Navigator;
import view.theme.AppTheme;
import utils.GameLog;
import utils.Require;

// The world map in the middle of the game screen.
// One responsibility: draw the image and turn a click into a navigation call.
public class WorldViewPanel extends JPanel {

    private final Navigator navigator;
    private Image background;

    WorldViewPanel(Navigator navigator) {
        this.navigator = Require.notNull(navigator, "navigator");
        setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(AppTheme.BORDER_COLOR, 1), "WORLD VIEW",
                javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.TOP,
                AppTheme.FONT_SUBHEADING, AppTheme.TEXT_PRIMARY));
        loadBackground();
    }

    // ----- Setup -----
    private void loadBackground() {
        try {
            java.net.URL imageUrl = getClass().getResource("/images/WorldView.png");
            if (imageUrl == null) {
                showMissingImage();
                return;
            }
            background = new ImageIcon(imageUrl).getImage();
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            setToolTipText("Hospital | Workshop | (closed) | Market | School — or use the buttons above");
            addMouseListener(new WorldZoneClickListener(this));
        } catch (RuntimeException e) {
            // The map is optional art: fall back to the placeholder instead of
            // letting an unchecked image/URL failure abort the whole game screen.
            GameLog.error("loading world view", e);
            showMissingImage();
        }
    }

    private void showMissingImage() {
        setLayout(new GridBagLayout());
        setBackground(Color.BLACK);
        add(new JLabel("World view image not found."));
    }

    // ----- Click routing -----
    // Five equal zones: Hospital, Workshop, closed, Market, School.
    void openZone(int x) {
        int width = getWidth();
        if (width <= 0 || x < 0 || x >= width) {
            return;
        }
        int section = width / 5;
        if (x < section) {
            navigator.openHospital();
        } else if (x < section * 2) {
            navigator.openWorkshop();
        } else if (x < section * 3) {
            // Middle zone intentionally does nothing.
        } else if (x < section * 4) {
            navigator.openMarket();
        } else {
            navigator.openSchool();
        }
    }

    // ----- Painting -----
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (background != null) {
            g.drawImage(background, 0, 0, getWidth(), getHeight(), this);
        }
    }
}
