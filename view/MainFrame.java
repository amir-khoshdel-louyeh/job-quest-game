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

        // نمایش اولین پنل = Login
        setContentPane(new LoginPanel());

        setVisible(true);
    }
}
