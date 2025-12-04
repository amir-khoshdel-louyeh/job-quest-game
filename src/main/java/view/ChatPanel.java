package view;

import controller.contract.MessageSink;
import view.theme.AppTheme;

import javax.swing.*;
import java.awt.*;

// Chat + toast notifications at the bottom of the game screen.
// Owned by GamePanel; also the MessageSink the controllers write to.
public class ChatPanel extends JPanel implements MessageSink {

    private final JTextArea chatArea;
    private final Toast toast = new Toast();

    public ChatPanel() {
        super(new BorderLayout());
        chatArea = new JTextArea(5,50);
        chatArea.setEditable(false);
        chatArea.setFont(AppTheme.FONT_BODY);
        chatArea.setBackground(AppTheme.SURFACE_COLOR);
        chatArea.setForeground(AppTheme.TEXT_PRIMARY);
        chatArea.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(AppTheme.BORDER_COLOR, 1),
            BorderFactory.createEmptyBorder(6, 8, 6, 8)));
        JScrollPane chatScroll = new JScrollPane(chatArea);
        chatScroll.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(AppTheme.BORDER_COLOR, 1), "Chat / Notifications",
            javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.TOP,
            AppTheme.FONT_SUBHEADING, AppTheme.TEXT_PRIMARY));
        add(chatScroll, BorderLayout.CENTER);
    }

    // Auto-scrolls; success lines also show a toast so rewards are noticed.
    @Override
    public void addChatMessage(String msg) {
        chatArea.append(msg + "\n");
        chatArea.setCaretPosition(chatArea.getDocument().getLength());
        toast.showFor(this, msg);
    }

}
