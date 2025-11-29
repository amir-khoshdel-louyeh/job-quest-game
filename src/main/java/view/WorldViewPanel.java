package view;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;

import view.theme.AppTheme;

// The world map in the middle of the game screen.
// One responsibility: draw the image and turn a click into a navigation call.
public class WorldViewPanel extends JPanel {

    // What the map can open. NavBar implements this.
    public interface Navigator {
        void openHospital();

        void openWorkshop();

        void openMarket();

        void openSchool();
    }

    private final Navigator navigator;
    private Image background;

    public WorldViewPanel(Navigator navigator) {
        this.navigator = utils.Require.notNull(navigator, "navigator");
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
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent event) {
                    openZone(event.getX());
                }
            });
        } catch (Exception e) {
            utils.GameLog.error("loading world view", e);
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
    private void openZone(int x) {
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
