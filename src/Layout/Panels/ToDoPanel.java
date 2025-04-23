package Layout.Panels;

import javax.swing.*;
import ToDo.ToDoManager;
import java.awt.*;

public class ToDoPanel extends JPanel {
    private JTextField taskDescriptionField;
    private JTextField taskEndDateField;
    private DefaultListModel<String> taskListModel;
    private JList<String> taskList;
    private JButton saveTaskButton;
    private JButton deleteTaskButton;
    private JButton completeTaskButton;
    private ToDoManager toDoManager;

    public ToDoPanel(ToDoManager toDoManager) {
        this.toDoManager = toDoManager;
        initializeComponents();
        setupLayout();
        setupListeners();
    }

    public void initializeComponents() {
        taskDescriptionField = new JTextField(15);
        taskEndDateField = new JTextField(15);
        taskListModel = new DefaultListModel<>();
        taskList = new JList<>(taskListModel);
        saveTaskButton = new JButton("Save");
        deleteTaskButton = new JButton("Delete");
        completeTaskButton = new JButton("Complete");
    }

    private void setupLayout() {
        setLayout(new BorderLayout());

        // Input Panel
        JPanel inputPanel = new JPanel(new GridLayout(2, 1));
        JPanel taskPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        taskPanel.add(new JLabel("Task Description: "));
        taskPanel.add(taskDescriptionField);

        JPanel datePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        datePanel.add(new JLabel("End Date: "));
        datePanel.add(taskEndDateField);

        inputPanel.add(taskPanel);
        inputPanel.add(datePanel);

        // Button Panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(saveTaskButton);
        buttonPanel.add(deleteTaskButton);
        buttonPanel.add(completeTaskButton);

        add(inputPanel, BorderLayout.NORTH);
        add(new JScrollPane(taskList), BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        displayTasks();
    }


    private void setupListeners() {
        saveTaskButton.addActionListener(e -> {
            String taskDescription = taskDescriptionField.getText();
            String endDate = taskEndDateField.getText();
            boolean isCompleted = false; // Default value for new tasks
            if (!taskDescription.isEmpty() && !endDate.isEmpty()) {
                toDoManager.addTask(taskDescription, endDate, isCompleted);
                taskListModel.addElement(taskDescription + " - Due: " + endDate);
                taskDescriptionField.setText("");
                taskEndDateField.setText("");
            } else {
                JOptionPane.showMessageDialog(null, "Please fill in both fields.");
            }
        });

        deleteTaskButton.addActionListener(e -> {
            int selectedIndex = taskList.getSelectedIndex();
            if (selectedIndex != -1) {
                taskListModel.remove(selectedIndex);
                toDoManager.removeTask(toDoManager.getTasks().get(selectedIndex));
            } else {
                JOptionPane.showMessageDialog(null, "Please select a task to delete.");
            }
        });

        completeTaskButton.addActionListener(e -> {
            int selectedIndex = taskList.getSelectedIndex();
            if (selectedIndex != -1) {
                toDoManager.markTaskAsCompleted(toDoManager.getTasks().get(selectedIndex));
                displayTasks();
            } else {
                JOptionPane.showMessageDialog(null, "Please select a task to mark as completed.");
            }
        });
    }

    public void displayTasks() {
        toDoManager.refreshTasks();
        taskListModel.clear();
        for (String summary : toDoManager.getTaskSummaries()) {
            taskListModel.addElement(summary);
        }
    }
}
