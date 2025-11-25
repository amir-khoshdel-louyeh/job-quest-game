package view;

import view.theme.AppTheme;
import javax.swing.*;
import java.awt.*;

// The help screen: one short tab — how to play, nothing more.
public class AboutDialog extends JDialog {

    public AboutDialog(JFrame parent) {
        super(parent, "About Job Quest", true);
        setSize(520, 560);
        setLocationRelativeTo(parent);
        setResizable(false);

        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(AppTheme.BACKGROUND_COLOR);

        JLabel title = new JLabel("Job Quest — Career Simulation", SwingConstants.CENTER);
        title.setFont(AppTheme.FONT_TITLE);
        title.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));
        add(title, BorderLayout.NORTH);

        String content = """
            <html>
            <body style='font-family: Segoe UI; padding: 12px;'>
            <h3>How to play</h3>
            <ul>
                <li>Pick a career, then do jobs from the <b>Workshop</b>.</li>
                <li>Every job plays a short <b>quiz</b> — score 50+ to pass and get paid.</li>
                <li>Learn skills at the <b>School</b> to unlock better jobs.</li>
                <li>Buy health at the <b>Hospital</b> and energy at the <b>Market</b>.</li>
            </ul>
            <h3>Pay &amp; level</h3>
            <ul>
                <li>Pay grows with <b>level</b> (+5%/level), <b>reputation</b> and <b>job bonus</b> (+2% per 10 jobs).</li>
                <li>Level-up <b>refills</b> health &amp; energy. XP need = level x 200.</li>
                <li>Zero health or energy knocks you out for 5 minutes.</li>
            </ul>
            <h3>Keys</h3>
            <ul>
                <li><b>ESC</b> exit (asks first) &nbsp; <b>F11</b> fullscreen toggle</li>
            </ul>
            <p style='color:#666;'>By Amir Khoshdel Louyeh — Java 17, Swing, Maven.</p>
            </body>
            </html>
            """;
        JEditorPane text = new JEditorPane("text/html", content);
        text.setEditable(false);
        text.setCaretPosition(0);
        add(new JScrollPane(text), BorderLayout.CENTER);

        JButton closeButton = AppTheme.createPrimaryButton("Close");
        closeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                dispose();
            }
        });
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottom.setBackground(AppTheme.BACKGROUND_COLOR);
        bottom.add(closeButton);
        add(bottom, BorderLayout.SOUTH);
    }
}
