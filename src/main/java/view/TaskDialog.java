package view;

import controller.UserController;
import controller.GameController;
import model.Task;
import model.TaskProvider;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.List;

public class TaskDialog extends JDialog {

    private final UserController userController;
    private final GamePanel gamePanel;
    private JList<Task> taskList;
    private JButton uploadButton, submitButton;
    private JLabel selectedFileLabel;
    private File uploadedFile = null;

    public TaskDialog(JFrame parent, UserController userController, GamePanel gamePanel) {
        super(parent, "Freelancer Tasks", true);
        this.userController = userController;
        this.gamePanel = gamePanel;

        setSize(400, 300);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10, 10));
        getRootPane().setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // ---------------- Task Selection ----------------
        List<Task> availableTasks = TaskProvider.getAvailableTasks();
        taskList = new JList<>(availableTasks.toArray(new Task[0]));
        taskList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        // Use a cell renderer to display the task name and payment nicely
        taskList.setCellRenderer(new TaskCellRenderer());
        add(new JScrollPane(taskList), BorderLayout.CENTER);

        // ---------------- File Upload ----------------
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.Y_AXIS));

        selectedFileLabel = new JLabel("No file selected");
        uploadButton = new JButton("Upload File");
        submitButton = new JButton("Submit Task");

        // Center align components in the bottom panel
        selectedFileLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        uploadButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        submitButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        bottomPanel.add(selectedFileLabel);
        bottomPanel.add(Box.createVerticalStrut(5));
        bottomPanel.add(uploadButton);
        bottomPanel.add(Box.createVerticalStrut(5));
        bottomPanel.add(submitButton);

        add(bottomPanel, BorderLayout.SOUTH);

        // ---------------- Actions ----------------
        uploadButton.addActionListener(e -> chooseFile());
        submitButton.addActionListener(e -> submitTask());
    }

    private void chooseFile() {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            uploadedFile = fileChooser.getSelectedFile();
            selectedFileLabel.setText("Selected: " + uploadedFile.getName());
        }
    }

    private void submitTask() {
        Task selectedTask = taskList.getSelectedValue();
        if (selectedTask == null) {
            JOptionPane.showMessageDialog(this, "Please select a task!");
            return;
        }
        if (uploadedFile == null) {
            JOptionPane.showMessageDialog(this, "Please upload a file!");
            return;
        }

        // Delegate the entire operation to the controller
        boolean success = userController.completeTask(selectedTask);

        if (success) {
            String message = String.format("✅ Completed task '%s' and earned $%d.", selectedTask.getName(), selectedTask.getPayment());
            gamePanel.addChatMessage(message);
            JOptionPane.showMessageDialog(this, message, "Task Submitted", JOptionPane.INFORMATION_MESSAGE);
            dispose(); // Close the dialog
        } else {
            JOptionPane.showMessageDialog(this, "Not enough energy to complete this task!");
        }
    }

    /**
     * A custom renderer to display Task objects in a JList.
     */
    private static class TaskCellRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            if (value instanceof Task) {
                Task task = (Task) value;
                setText(String.format("%s ($%d, Energy: -%d)", task.getName(), task.getPayment(), task.getEnergyCost()));
            }
            return this;
        }
    }
}
