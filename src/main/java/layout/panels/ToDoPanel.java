package layout.panels;

import todo.ToDo;
import todo.impl.ToDoService;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.Properties;
import org.jdatepicker.impl.*;

import config.*;

/**
 * A Swing panel component providing a user interface for managing todo tasks.
 * Extends GeneralPanel to inherit common CRUD functionality while implementing
 * task-specific features and behaviors.
 *
 * <p>Features:</p>
 * <ul>
 * <li>Task description input field with validation</li>
 * <li>Date picker for selecting task due dates with validation</li>
 * <li>List display of all tasks with selection capability</li>
 * <li>Buttons for saving, deleting, and editing tasks</li>
 * <li>Task completion functionality</li>
 * <li>Sorting options by description or due date</li>
 * </ul>
 *
 * <p>Layout Structure:</p>
 * <ul>
 * <li>Top: Input panel with description field and date picker</li>
 * <li>Center: Scrollable list of tasks</li>
 * <li>Bottom: Action buttons including complete task functionality</li>
 * </ul>
 *
 * <p>This panel follows the Model-View-Controller pattern where the ToDoPanel serves as the view,
 * the ToDoService as the controller, and ToDo objects as the model. It implements the template
 * method pattern by extending GeneralPanel to inherit common functionality while providing
 * task-specific implementations.</p>
 *
 * @version 1.0
 * @since 2025-04-23
 * @author Louis Bertrand Ntwali
 *
 * @see ToDoService
 * @see ToDo
 * @see GeneralPanel
 * @see org.jdatepicker.impl.JDatePickerImpl
 */
public class ToDoPanel extends GeneralPanel<ToDo> {

    /** Field for entering/editing task descriptions, limited to 50 characters */
    private JTextField taskDescriptionField;

    /** Date picker component for selecting task due dates (format: yyyy-MM-dd) */
    private JDatePickerImpl datePicker;

    /** Reference to the todo service for task-specific operations beyond basic CRUD */
    private ToDoService toDoManager;

    /** Model for the date picker component to store and retrieve selected dates */
    private SqlDateModel model;

    /**
     * Constructs a new ToDoPanel with the specified service.
     * Initializes the UI and sets up task management functionality.
     *
     * @param service The todo service for handling data operations, must not be null
     * @throws IllegalArgumentException if service is null (inherited from superclass)
     */
    public ToDoPanel(ToDoService service) {
        super(service);
        this.toDoManager = service;
    }

    /**
     * Initializes all UI components and sets up event listeners.
     * Creates text fields, date picker, and configures the task list
     * with sorting and completion handling.
     *
     * <p>Implementation details:</p>
     * <ul>
     * <li>Sets up the date picker with today's date highlighting</li>
     * <li>Configures sorting options for Description and Date</li>
     * <li>Adds the Complete button for marking tasks as finished</li>
     * <li>Attaches event listeners to interactive components</li>
     * </ul>
     */
    @Override
    public void initializeComponents() {
        toDoManager = (ToDoService) service;
        taskDescriptionField = new JTextField(15);
        model = new SqlDateModel();

        // Configure date picker properties
        Properties p = new Properties();
        p.put("text.today", "Today");
        p.put("text.month", "Month");
        p.put("text.year", "Year");

        // Set up date picker components
        JDatePanelImpl datePanel = new JDatePanelImpl(model, p);
        DateComponentFormatter formatter = new DateComponentFormatter();
        datePicker = new JDatePickerImpl(datePanel, formatter);

        // Configure sorting options
        sortOptions = new JComboBox<>(new String[] {
                "Description", "Date"
        });
        sortOptions.addActionListener(e -> onSort());

        // Add complete task button
        JButton completeTaskButton = new JButton("Complete");
        buttonPanel.add(completeTaskButton);
        completeTaskButton.addActionListener(e -> onComplete());
    }

    /**
     * Handles the task completion action.
     * Marks the selected task as completed and updates the UI.
     *
     * <p>This operation is permanent and changes the task's status in the database.
     * If no task is selected, an error message is displayed.</p>
     */
    private void onComplete() {
        int selected = itemList.getSelectedIndex();
        if (selected > 0) {
            ToDo toDo = toDoManager.getAll().get(selected);
            if (toDo.isCompleted()) {
                showError("This task is already completed.");
            } else {
                toDoManager.markTaskAsCompleted(toDo);
                displayItems();
            }
        }  else {
            showError("Please select a task to mark as completed.");
        }
    }

    /**
     * Handles sorting of tasks when user selects a sort option.
     * Supports sorting by description (alphabetical) or date (chronological).
     *
     * <p>This method delegates to the service layer for actual sorting implementation,
     * then updates the UI to reflect the new order. The current selection is not preserved
     * after sorting.</p>
     */
    private void onSort() {
        String selected = (String) sortOptions.getSelectedItem();
        toDoManager.sort(selected);
        updateListModel();
    }

    /**
     * Creates and configures the input panel layout.
     * Organizes task description field, date picker, and sort options
     * in a structured layout using GridLayout and FlowLayout.
     *
     * <p>The panel consists of three rows:</p>
     * <ul>
     * <li>Task description field with label</li>
     * <li>Date picker with label</li>
     * <li>Sort options with label</li>
     * </ul>
     *
     * @return The configured input panel, never null
     */
    @Override
    protected JPanel createInputPanel() {
        JPanel inputPanel = new JPanel(new GridLayout(3, 1));

        JPanel taskPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        taskPanel.add(new JLabel("Task: "));
        taskPanel.add(taskDescriptionField);

        JPanel datePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        datePanel.add(new JLabel("End Date: "));
        datePanel.add(datePicker);

        Font font = new Font("SansSerif", Font.PLAIN, 16);
        datePicker.getJFormattedTextField().setFont(font);
        datePicker.getComponent(1).setFont(font);

        JPanel sortPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        sortPanel.add(new JLabel("Sort: "));
        sortPanel.add(sortOptions);

        inputPanel.add(taskPanel);
        inputPanel.add(datePanel);
        inputPanel.add(sortPanel);

        return inputPanel;
    }

    /**
     * Validates user input for both task description and end date.
     * Ensures description meets length requirements and date is valid.
     *
     * <p>Validation rules:</p>
     * <ul>
     * <li>Description must be between 1 and 50 characters</li>
     * <li>End date must be in yyyy-MM-dd format</li>
     * <li>End date must not be in the past</li>
     * </ul>
     *
     * <p>Displays appropriate error messages if validation fails.</p>
     *
     * @return true if input is valid, false otherwise
     * @see config.Validation#isInvalidTitle(String)
     * @see config.Validation#isValidEndDate(String)
     */
    @Override
    protected boolean validateInput() {
        String taskDescription = taskDescriptionField.getText();
        Object endDate = datePicker.getModel().getValue();

        if (Validation.isInvalidTitle(taskDescription)) {
            showError("Description must be between 1 and 50 characters.");
            return false;
        }
        if (endDate == null || !Validation.isValidEndDate(endDate.toString())) {
            showError("Invalid Date Format! The format should be 'yyyy-MM-dd' and not be in the past.");
            return false;
        }
        return true;
    }

    /**
     * Creates a new ToDo instance from current input values.
     * The newly created task is marked as not completed by default.
     *
     * <p>This method assumes input validation has already been performed.</p>
     *
     * @return A new ToDo object with description, end date, and incomplete status, never null
     */
    @Override
    protected ToDo createNewItem() {
        String taskDescription = taskDescriptionField.getText();
        String endDate = datePicker.getModel().getValue().toString();
        return new ToDo(taskDescription, endDate, false);
    }

    /**
     * Updates an existing task with current input values.
     * This preserves the task's completion status while updating its description and due date.
     *
     * <p>This method assumes input validation has already been performed.
     * After updating the task properties, it calls the service to persist the changes.</p>
     *
     * @param toDo The task to update, must not be null
     */
    @Override
    protected void updateExistingItem(ToDo toDo) {
        toDo.setTaskDescription(taskDescriptionField.getText());
        toDo.setEndDate(datePicker.getModel().getValue().toString());
        service.update(toDo);
    }

    /**
     * Populates input fields with values from an existing task.
     * Sets the description field text and configures the date picker with the task's end date.
     *
     * <p>This method is called when editing an existing task. It converts the task's
     * string-based date representation to a LocalDate and then to a SQL Date for the picker.</p>
     *
     * @param toDo The task whose values should populate the inputs, must not be null
     * @throws IllegalArgumentException if the task's date cannot be parsed
     */
    @Override
    protected void populateInputFields(ToDo toDo) {
        taskDescriptionField.setText(toDo.getTaskDescription());
        LocalDate endDate = LocalDate.parse(toDo.getEndDate());
        model.setValue(java.sql.Date.valueOf(endDate));
    }

    /**
     * Clears all input fields to their default state.
     * Removes text from the description field and resets the date picker.
     *
     * <p>This method is called after a save operation or when canceling an edit.</p>
     */
    @Override
    protected void clearInputFields() {
        taskDescriptionField.setText("");
        model.setValue(null);
    }

    @Override
    protected String getItemTypeName() {
        return "task";
    }

    @Override
    protected String getItemName(ToDo toDo) {
        return toDo.getTaskDescription();
    }
}