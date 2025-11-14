package view.theme;

// Shared colors, fonts, and button styles used by every screen.
import javax.swing.*;
import java.awt.*;

public class AppTheme {
    
    public static final Color PRIMARY_COLOR = new Color(63, 81, 181);        // Indigo
    public static final Color PRIMARY_DARK = new Color(48, 63, 159);         // Dark Indigo
    public static final Color PRIMARY_LIGHT = new Color(92, 107, 192);       // Light Indigo
    
    public static final Color ACCENT_COLOR = new Color(255, 64, 129);        // Pink Accent
    public static final Color SUCCESS_COLOR = new Color(76, 175, 80);        // Green
    public static final Color WARNING_COLOR = new Color(255, 152, 0);        // Orange
    public static final Color ERROR_COLOR = new Color(244, 67, 54);          // Red
    public static final Color INFO_COLOR = new Color(33, 150, 243);          // Blue
    
    public static final Color BACKGROUND_COLOR = new Color(250, 250, 250);   // Light Gray
    public static final Color SURFACE_COLOR = Color.WHITE;
    public static final Color CARD_BACKGROUND = new Color(255, 255, 255);
    
    public static final Color TEXT_PRIMARY = new Color(33, 33, 33);          // Dark Gray
    public static final Color TEXT_SECONDARY = new Color(117, 117, 117);     // Medium Gray
    public static final Color TEXT_HINT = new Color(189, 189, 189);          // Light Gray
    
    public static final Color BORDER_COLOR = new Color(224, 224, 224);
    public static final Color DIVIDER_COLOR = new Color(238, 238, 238);
    
    public static final Color HEALTH_COLOR = new Color(244, 67, 54);         // Red
    public static final Color ENERGY_COLOR = new Color(255, 193, 7);         // Amber
    public static final Color MONEY_COLOR = new Color(76, 175, 80);          // Green
    
    public static final Font FONT_TITLE = new Font("Segoe UI", Font.BOLD, 28);
    public static final Font FONT_HEADING = new Font("Segoe UI", Font.BOLD, 20);
    public static final Font FONT_SUBHEADING = new Font("Segoe UI", Font.BOLD, 16);
    public static final Font FONT_BODY = new Font("Segoe UI", Font.PLAIN, 14);
    public static final Font FONT_SMALL = new Font("Segoe UI", Font.PLAIN, 12);
    public static final Font FONT_BUTTON = new Font("Segoe UI", Font.BOLD, 14);
    
    public static final int BORDER_RADIUS = 8;
    public static final int PADDING_SMALL = 8;
    public static final int PADDING_MEDIUM = 16;
    public static final int PADDING_LARGE = 24;
    public static final int BUTTON_HEIGHT = 40;
    public static final int INPUT_HEIGHT = 36;
    
    public static void initialize() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            customizeDefaults();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private static void customizeDefaults() {
        UIManager.put("Button.font", FONT_BUTTON);
        UIManager.put("Button.background", PRIMARY_COLOR);
        UIManager.put("Button.foreground", Color.WHITE);
        UIManager.put("Button.select", PRIMARY_DARK);
        UIManager.put("Button.arc", BORDER_RADIUS);
        
        UIManager.put("TextField.font", FONT_BODY);
        UIManager.put("TextField.border", BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR, 1),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        
        UIManager.put("Label.font", FONT_BODY);
        UIManager.put("Label.foreground", TEXT_PRIMARY);
        
        UIManager.put("Panel.background", BACKGROUND_COLOR);
        
        UIManager.put("OptionPane.messageFont", FONT_BODY);
        UIManager.put("OptionPane.buttonFont", FONT_BUTTON);
    }
    
    public static JButton createPrimaryButton(String text) {
        JButton button = new JButton(text);
        button.setFont(FONT_BUTTON);
        button.setBackground(PRIMARY_COLOR);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(button.getPreferredSize().width, BUTTON_HEIGHT));
        addHoverEffect(button, PRIMARY_COLOR, PRIMARY_DARK);
        
        return button;
    }
    
    public static JButton createSecondaryButton(String text) {
        JButton button = new JButton(text);
        button.setFont(FONT_BUTTON);
        button.setBackground(SURFACE_COLOR);
        button.setForeground(PRIMARY_COLOR);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(PRIMARY_COLOR, 2),
            BorderFactory.createEmptyBorder(8, 16, 8, 16)
        ));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(button.getPreferredSize().width, BUTTON_HEIGHT));
        addHoverEffect(button, SURFACE_COLOR, PRIMARY_LIGHT, Color.WHITE, PRIMARY_COLOR);
        
        return button;
    }
    
    public static JButton createSuccessButton(String text) {
        JButton button = createPrimaryButton(text);
        button.setBackground(SUCCESS_COLOR);
        addHoverEffect(button, SUCCESS_COLOR, SUCCESS_COLOR.darker());
        
        return button;
    }
    
    public static JButton createDangerButton(String text) {
        JButton button = createPrimaryButton(text);
        button.setBackground(ERROR_COLOR);
        addHoverEffect(button, ERROR_COLOR, ERROR_COLOR.darker());
        
        return button;
    }
    
    public static JTextField createTextField(int columns) {
        JTextField field = new JTextField(columns);
        field.setFont(FONT_BODY);
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR, 1),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        field.setPreferredSize(new Dimension(field.getPreferredSize().width, INPUT_HEIGHT));
        return field;
    }
    
    public static JPasswordField createPasswordField(int columns) {
        JPasswordField field = new JPasswordField(columns);
        field.setFont(FONT_BODY);
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR, 1),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        field.setPreferredSize(new Dimension(field.getPreferredSize().width, INPUT_HEIGHT));
        return field;
    }

    private static void addHoverEffect(JButton button, Color base, Color hover) {
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(hover);
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(base);
            }
        });
    }

    private static void addHoverEffect(JButton button, Color base, Color hover, Color hoverForeground, Color baseForeground) {
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(hover);
                button.setForeground(hoverForeground);
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(base);
                button.setForeground(baseForeground);
            }
        });
    }
    
    public static JPanel createCard() {
        JPanel panel = new JPanel();
        panel.setBackground(CARD_BACKGROUND);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR, 1),
            BorderFactory.createEmptyBorder(PADDING_MEDIUM, PADDING_MEDIUM, PADDING_MEDIUM, PADDING_MEDIUM)
        ));
        return panel;
    }

    public static JPanel createSectionPanel(String title) {
        JPanel panel = new JPanel();
        panel.setLayout(new javax.swing.BoxLayout(panel, javax.swing.BoxLayout.Y_AXIS));
        panel.setBackground(CARD_BACKGROUND);
        javax.swing.border.TitledBorder border = BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(BORDER_COLOR, 1), title);
        border.setTitleFont(FONT_SUBHEADING);
        border.setTitleColor(TEXT_PRIMARY);
        panel.setBorder(BorderFactory.createCompoundBorder(border,
            BorderFactory.createEmptyBorder(PADDING_SMALL, PADDING_SMALL, PADDING_SMALL, PADDING_SMALL)));
        return panel;
    }

    public static void styleDialog(JDialog dialog, int width, int height) {
        dialog.getContentPane().setBackground(BACKGROUND_COLOR);
        dialog.setSize(width, height);
        dialog.setLocationRelativeTo(dialog.getParent());
    }

    public static JPanel createItemCard() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(CARD_BACKGROUND);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR, 1),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        return panel;
    }

    public static JLabel createCardTitle(String text) {
        JLabel label = new JLabel(text);
        label.setFont(FONT_SUBHEADING);
        label.setForeground(TEXT_PRIMARY);
        return label;
    }

    public static JLabel createCardDescription(String htmlText) {
        JLabel label = new JLabel("<html>" + htmlText + "</html>");
        label.setFont(FONT_SMALL);
        label.setForeground(TEXT_SECONDARY);
        return label;
    }
}
