package view;

import model.User;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class TaskDialog extends JDialog {

    private User currentUser;
    private JList<String> taskList;
    private JButton uploadButton, submitButton;
    private JLabel selectedFileLabel;
    private File uploadedFile = null;

    private String[] tasks = {
        "Design a Logo ($300)",
        "Write an Article ($150)",
        "Translate Document ($200)",
        "Create Website Mockup ($400)"
    };
    private int[] taskPayments = {300, 150, 200, 400};

    public TaskDialog(JFrame parent, User user) {
        super(parent, "Freelancer Tasks", true);
        this.currentUser = user;

        setSize(400, 300);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10,10));

        // ---------------- Task Selection ----------------
        taskList = new JList<>(tasks);
        taskList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        add(new JScrollPane(taskList), BorderLayout.CENTER);

        // ---------------- File Upload ----------------
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new GridLayout(3,1,5,5));

        selectedFileLabel = new JLabel("No file selected");
        uploadButton = new JButton("Upload File");
        submitButton = new JButton("Submit Task");

        bottomPanel.add(selectedFileLabel);
        bottomPanel.add(uploadButton);
        bottomPanel.add(submitButton);

        add(bottomPanel, BorderLayout.SOUTH);

        // ---------------- Actions ----------------
        uploadButton.addActionListener(e -> chooseFile());
        submitButton.addActionListener(e -> submitTask());
    }

    private void chooseFile() {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(this);
        if(result == JFileChooser.APPROVE_OPTION) {
            uploadedFile = fileChooser.getSelectedFile();
            selectedFileLabel.setText("Selected: " + uploadedFile.getName());
        }
    }

    private void submitTask() {
        int index = taskList.getSelectedIndex();
        if(index == -1) {
            JOptionPane.showMessageDialog(this, "Please select a task!");
            return;
        }
        if(uploadedFile == null) {
            JOptionPane.showMessageDialog(this, "Please upload a file!");
            return;
        }

        // پرداخت و انرژی
        int payment = taskPayments[index];
        currentUser.setBalance(currentUser.getBalance() + payment);
        currentUser.setEnergy(Math.min(100_000, currentUser.getEnergy() - 2000)); // کمی انرژی مصرف می‌شود
        JOptionPane.showMessageDialog(this, "Task submitted successfully! Earned $" + payment);
        dispose(); // بستن دیالوگ
    }
}
