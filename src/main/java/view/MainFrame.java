package view;

import javax.swing.*;

public class MainFrame extends JFrame {
    public MainFrame() {
        setTitle("Job-Quest-Game");

        // حالت تمام صفحه
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        // ❌ بدون undecorated → دکمه‌های Min/Max/Close فعال هستند
        setUndecorated(false);

        // جلوگیری از تغییر اندازه پنجره توسط کاربر
        setResizable(false);

        // بستن کامل برنامه
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Pass this MainFrame to LoginPanel for navigation
        setContentPane(new LoginPanel(this));

        setVisible(true);
    }

    // Centralized navigation method
    public void showPanel(JPanel panel) {
        setContentPane(panel);
        revalidate();
        repaint();
    }
}
