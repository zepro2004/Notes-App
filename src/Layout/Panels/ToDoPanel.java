package Layout.Panels;

import javax.swing.*;
import ToDo.ToDo;
import ToDo.ToDoManager;
import java.awt.*;
import java.time.LocalDate;
import java.util.Properties;

import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.DateComponentFormatter;
import org.jdatepicker.impl.SqlDateModel;

import config.*;

/**
 * A Swing panel component that provides a user interface for managing todo tasks.
 * This panel includes functionality for creating, editing, deleting, and completing tasks,
 * as well as displaying a list of existing tasks.
 *
 * <p>Features:</p>
 * <ul>
 * <li>Task description input field</li>
 * <li>Date picker for selecting task end dates</li>
 * <li>List display of all tasks</li>
 * <li>Buttons for saving, deleting, editing, and marking tasks as complete</li>
 * </ul>
 *
 * <p>The panel uses {@code JDatePickerImpl} for date selection and integrates with a {@link ToDo.ToDoManager}
 * for persistence operations (adding, removing, updating, retrieving tasks).</p>
 *
 * @see ToDo.ToDoManager
 * @see ToDo.ToDo
 * @see javax.swing.JPanel
 * @see org.jdatepicker.impl.JDatePickerImpl
 * @see org.jdatepicker.impl.SqlDateModel
 *
 * @version 1.0
 * @author Louis Bertrand Ntwali
 * @since 2025-04-23
 */
public class ToDoPanel extends JPanel {
    /**
     * Text field for entering the description of a new or edited task.
     */
    private JTextField taskDescriptionField;
    /**
     * Date picker component for selecting the end date of a task.
     * Uses {@link org.jdatepicker.impl.JDatePickerImpl}.
     */
    private JDatePickerImpl datePicker;
    /**
     * The data model for the task list ({@code taskList}). Holds the string representations of tasks.
     */
    private DefaultListModel<String> taskListModel;
    /**
     * The list component that displays the tasks. Uses {@code taskListModel} as its data source.
     */
    private JList<String> taskList;
    /**
     * Button to save a new task or the changes to an existing task.
     */
    private JButton saveTaskButton;
    /**
     * Button to delete the selected task from the list and persistence.
     */
    private JButton deleteTaskButton;
    /**
     * Button to load the selected task's details into the input fields for editing.
     */
    private JButton editTaskButton;
    /**
     * Button to mark the selected task as completed.
     */
    private JButton completeTaskButton;
    /**
     * Reference to the {@link ToDo.ToDoManager} instance used for managing task data.
     */
    private ToDoManager toDoManager;

    /**
     * Flag indicating whether the panel is currently in editing mode (modifying an existing task).
     * {@code true} if editing, {@code false} otherwise.
     */
    private boolean isEditting = false;
    /**
     * Holds the {@link ToDo.ToDo} object that is currently being edited.
     * {@code null} if not in editing mode or no task is selected for editing.
     */
    private ToDo currentlyEditingToDo = null;

    /**
     * Constructs a new {@code ToDoPanel}.
     * Initializes the UI components, sets up the layout, configures event listeners,
     * and displays the initial list of tasks retrieved via the provided {@code ToDoManager}.
     *
     * @param toDoManager The {@link ToDo.ToDoManager} instance to be used for task operations. Must not be null.
     */
    public ToDoPanel(ToDoManager toDoManager) {
        this.toDoManager = toDoManager;
        initializeComponents();
        setupLayout();
        setupListeners();
    }

    /**
     * Initializes all the Swing components used in the panel.
     * This includes creating instances of text fields, date pickers, list models, lists, and buttons.
     * The date picker is configured with specific properties for display text.
     */
    public void initializeComponents() {
        taskDescriptionField = new JTextField(15);

        SqlDateModel model = new SqlDateModel();

        Properties p = new Properties();
        p.put("text.today", "Today");
        p.put("text.month", "Month");
        p.put("text.year", "Year");

        JDatePanelImpl datePanel = new JDatePanelImpl(model, p);
        DateComponentFormatter formatter = new DateComponentFormatter();
        datePicker = new JDatePickerImpl(datePanel, formatter);

        taskListModel = new DefaultListModel<>();
        taskList = new JList<>(taskListModel);
        saveTaskButton = new JButton("Save");
        deleteTaskButton = new JButton("Delete");
        editTaskButton = new JButton("Edit");
        completeTaskButton = new JButton("Complete");
    }

    /**
     * Sets up the layout of the panel using {@link java.awt.BorderLayout}.
     * It arranges the input fields (description, date), the task list, and the action buttons
     * within nested panels (inputPanel, buttonPanel) positioned in the NORTH, CENTER, and SOUTH
     * regions respectively. Finally, it calls {@link #displayTasks()} to populate the list initially.
     */
    private void setupLayout() {
        setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel(new GridLayout(2, 1));

        JPanel taskPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        taskPanel.add(new JLabel("Task Description: "));
        taskPanel.add(taskDescriptionField);

        JPanel datePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        datePanel.add(new JLabel("End Date: "));
        datePanel.add(datePicker);

        inputPanel.add(taskPanel);
        inputPanel.add(datePanel);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(saveTaskButton);
        buttonPanel.add(deleteTaskButton);
        buttonPanel.add(editTaskButton);
        buttonPanel.add(completeTaskButton);

        add(inputPanel, BorderLayout.NORTH);
        add(new JScrollPane(taskList), BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        displayTasks();
    }


    /**
     * Sets up the action listeners for the buttons (Save, Delete, Edit, Complete).
     * Defines the behavior that occurs when each button is clicked, including input validation,
     * interaction with the {@link ToDoManager}, UI updates, and error handling.
     */
    private void setupListeners() {
        saveTaskButton.addActionListener(e -> {
            String taskDescription = taskDescriptionField.getText();
            Object endDate = datePicker.getModel().getValue();

            StringBuilder errorMsg = new StringBuilder();
            if (Validation.isInvalidTitle(taskDescription)) {
                errorMsg.append("Description must be between 1 and 50 characters.\n");
            }
            if (endDate == null || !Validation.isValidEndDate(endDate.toString())) {
                errorMsg.append("Invalid Date Format! The format should be 'yyyy-MM-dd' and not be in the past.");
            }

            if (!errorMsg.isEmpty()) {
                JOptionPane.showMessageDialog(this, errorMsg.toString(),
                        "Invalid Input", JOptionPane.ERROR_MESSAGE);
                return;
            }

            boolean isCompleted = false;

            if (isEditting && currentlyEditingToDo != null){
                currentlyEditingToDo.setTaskDescription(taskDescription);
                currentlyEditingToDo.setEndDate(endDate.toString());
                toDoManager.updateTaskInDatabase(currentlyEditingToDo);

                isEditting = false;
                currentlyEditingToDo = null;
                displayTasks();

                int editedIndex = -1;
                for(int i = 0; i < taskListModel.size(); i++) {
                    // Note: This comparison might be fragile if display format changes
                    if(taskListModel.getElementAt(i).startsWith(taskDescription)) {
                        editedIndex = i;
                        break;
                    }
                }
                if(editedIndex != -1) {
                    taskList.setSelectedIndex(editedIndex);
                    taskList.ensureIndexIsVisible(editedIndex);
                }

            } else {
                toDoManager.addTask(taskDescription, endDate.toString(), isCompleted);
                displayTasks();
                if (!taskListModel.isEmpty()) {
                    taskList.setSelectedIndex(0);
                    taskList.ensureIndexIsVisible(0);
                }
            }

            taskDescriptionField.setText("");
            datePicker.getModel().setValue(null);
        });

        deleteTaskButton.addActionListener(e -> {
            int selectedIndex = taskList.getSelectedIndex();
            if (selectedIndex != -1) {
                ToDo taskToRemove = toDoManager.getTasks().get(selectedIndex);
                toDoManager.removeTask(taskToRemove);
                taskListModel.remove(selectedIndex);
            } else {
                JOptionPane.showMessageDialog(this, "Please select a task to delete.",
                        "No Task Selected", JOptionPane.WARNING_MESSAGE);
            }
        });

        editTaskButton.addActionListener(e -> {
            int selectedIndex = taskList.getSelectedIndex();
            if (selectedIndex != -1) {
                isEditting = true;
                currentlyEditingToDo = toDoManager.getTasks().get(selectedIndex);

                taskDescriptionField.setText(currentlyEditingToDo.getTaskDescription());

                LocalDate endDate = LocalDate.parse(currentlyEditingToDo.getEndDate());
                SqlDateModel model = (SqlDateModel) datePicker.getModel();
                model.setValue(java.sql.Date.valueOf(endDate));
            } else {
                JOptionPane.showMessageDialog(this, "Please select a task to edit.",
                        "No Task Selected", JOptionPane.WARNING_MESSAGE);
            }
        });

        completeTaskButton.addActionListener(e -> {
            int selectedIndex = taskList.getSelectedIndex();
            if (selectedIndex != -1) {
                toDoManager.markTaskAsCompleted(toDoManager.getTasks().get(selectedIndex));
                displayTasks();
            } else {
                JOptionPane.showMessageDialog(this, "Please select a task to mark as completed.",
                        "No Task Selected", JOptionPane.WARNING_MESSAGE);
            }
        });
    }

    /**
     * Refreshes the task list displayed in the UI.
     * It first tells the {@link ToDoManager} to refresh its internal list (e.g., reload from database),
     * then clears the current {@code taskListModel}, and finally repopulates the model
     * with the task summaries obtained from the {@code ToDoManager}.
     */
    public void displayTasks() {
        toDoManager.refreshTasks();
        taskListModel.clear();
        for (String summary : toDoManager.getTaskSummaries()) {
            taskListModel.addElement(summary);
        }
    }
}