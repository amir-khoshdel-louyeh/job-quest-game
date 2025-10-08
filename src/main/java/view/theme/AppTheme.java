package view.theme;

import javax.swing.*;
import java.awt.*;

/**
 * Professional theme configuration for the application.
 * Provides consistent colors, fonts, and styling across all UI components.
 */
public class AppTheme {
    
    // Color Palette - Modern Professional Colors
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
    
    // Health and Energy Colors
    public static final Color HEALTH_COLOR = new Color(244, 67, 54);         // Red
    public static final Color ENERGY_COLOR = new Color(255, 193, 7);         // Amber
    public static final Color MONEY_COLOR = new Color(76, 175, 80);          // Green
    
    // Fonts
    public static final Font FONT_TITLE = new Font("Segoe UI", Font.BOLD, 28);
    public static final Font FONT_HEADING = new Font("Segoe UI", Font.BOLD, 20);
    public static final Font FONT_SUBHEADING = new Font("Segoe UI", Font.BOLD, 16);
    public static final Font FONT_BODY = new Font("Segoe UI", Font.PLAIN, 14);
    public static final Font FONT_SMALL = new Font("Segoe UI", Font.PLAIN, 12);
    public static final Font FONT_BUTTON = new Font("Segoe UI", Font.BOLD, 14);
    
    // Dimensions
    public static final int BORDER_RADIUS = 8;
    public static final int PADDING_SMALL = 8;
    public static final int PADDING_MEDIUM = 16;
    public static final int PADDING_LARGE = 24;
    public static final int BUTTON_HEIGHT = 40;
    public static final int INPUT_HEIGHT = 36;
    
    /**
     * Initialize the application theme.
     * Should be called before creating any UI components.
     */
    public static void initialize() {
        try {
            // Set system look and feel as fallback
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            
            // Customize UIManager defaults
            customizeDefaults();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Customize UIManager defaults for consistent styling.
     */
    private static void customizeDefaults() {
        // Button styling
        UIManager.put("Button.font", FONT_BUTTON);
        UIManager.put("Button.background", PRIMARY_COLOR);
        UIManager.put("Button.foreground", Color.WHITE);
        UIManager.put("Button.select", PRIMARY_DARK);
        UIManager.put("Button.arc", BORDER_RADIUS);
        
        // Text field styling
        UIManager.put("TextField.font", FONT_BODY);
        UIManager.put("TextField.border", BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR, 1),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        
        // Label styling
        UIManager.put("Label.font", FONT_BODY);
        UIManager.put("Label.foreground", TEXT_PRIMARY);
        
        // Panel styling
        UIManager.put("Panel.background", BACKGROUND_COLOR);
        
        // OptionPane styling
        UIManager.put("OptionPane.messageFont", FONT_BODY);
        UIManager.put("OptionPane.buttonFont", FONT_BUTTON);
    }
    
    /**
     * Creates a styled primary button.
     */
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
        
        // Hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(PRIMARY_DARK);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(PRIMARY_COLOR);
            }
        });
        
        return button;
    }
    
    /**
     * Creates a styled secondary button.
     */
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
        
        // Hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(PRIMARY_LIGHT);
                button.setForeground(Color.WHITE);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(SURFACE_COLOR);
                button.setForeground(PRIMARY_COLOR);
            }
        });
        
        return button;
    }
    
    /**
     * Creates a styled success button.
     */
    public static JButton createSuccessButton(String text) {
        JButton button = createPrimaryButton(text);
        button.setBackground(SUCCESS_COLOR);
        
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(SUCCESS_COLOR.darker());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(SUCCESS_COLOR);
            }
        });
        
        return button;
    }
    
    /**
     * Creates a styled danger button.
     */
    public static JButton createDangerButton(String text) {
        JButton button = createPrimaryButton(text);
        button.setBackground(ERROR_COLOR);
        
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(ERROR_COLOR.darker());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(ERROR_COLOR);
            }
        });
        
        return button;
    }
    
    /**
     * Creates a styled text field.
     */
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
    
    /**
     * Creates a styled password field.
     */
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
    
    /**
     * Creates a styled card panel.
     */
    public static JPanel createCard() {
        JPanel panel = new JPanel();
        panel.setBackground(CARD_BACKGROUND);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR, 1),
            BorderFactory.createEmptyBorder(PADDING_MEDIUM, PADDING_MEDIUM, PADDING_MEDIUM, PADDING_MEDIUM)
        ));
        return panel;
    }
    
    /**
     * Creates a styled label with icon.
     */
    public static JLabel createStatLabel(String text, Color color) {
        JLabel label = new JLabel(text);
        label.setFont(FONT_BODY);
        label.setForeground(color);
        label.setBorder(BorderFactory.createEmptyBorder(4, 0, 4, 0));
        return label;
    }
}
