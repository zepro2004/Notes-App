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
 *
 * @version 1.0
 * @since 2025-04-23
 * @author Louis Bertrand Ntwali
 *
 * @see todo.impl.ToDoService
 * @see todo.ToDo
 * @see javax.swing.JPanel
 * @see org.jdatepicker.impl.JDatePickerImpl
 * @see org.jdatepicker.impl.SqlDateModel
 *
 * @version 1.0
 * @author Louis Bertrand Ntwali
 * @since 2025-04-23
 */
public class ToDoPanel extends GeneralPanel<ToDo> {
    /**
     * Text field for entering the description of a new or edited task.
     */
    private JTextField taskDescriptionField;
    /**
     * Date picker component for selecting the end date of a task.
     * Uses {@link JDatePickerImpl}.
     */
    private JDatePickerImpl datePicker;

    /**
     * Button to mark the selected task as completed.
     */

    private ToDoService toDoManager;

    SqlDateModel model;

    public ToDoPanel(ToDoService service) {
        super(service);
        this.toDoManager = service;
    }

    @Override
    public void initializeComponents() {
        toDoManager = (ToDoService) service;
        taskDescriptionField = new JTextField(15);
        model = new SqlDateModel();

        Properties p = new Properties();
        p.put("text.today", "Today");
        p.put("text.month", "Month");
        p.put("text.year", "Year");

        JDatePanelImpl datePanel = new JDatePanelImpl(model, p);
        DateComponentFormatter formatter = new DateComponentFormatter();
        datePicker = new JDatePickerImpl(datePanel, formatter);

        JButton completeTaskButton = new JButton("Complete");
        buttonPanel.add(completeTaskButton);
        completeTaskButton.addActionListener(e -> onComplete());
    }

    private void onComplete() {
        int selected = itemList.getSelectedIndex();
        if (selected > 0) {
            ToDo toDo = toDoManager.getAll().get(selected);
            toDoManager.markTaskAsCompleted(toDo);
            displayItems();
        }  else {
            showError("Please select a task to mark as completed.");
        }
    }

    @Override
    protected JPanel createInputPanel() {
        JPanel inputPanel = new JPanel(new GridLayout(2, 1));

        JPanel taskPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        taskPanel.add(new JLabel("Task: "));
        taskPanel.add(taskDescriptionField);

        JPanel datePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        datePanel.add(new JLabel("End Date: "));
        datePanel.add(datePicker);

        inputPanel.add(taskPanel);
        inputPanel.add(datePanel);

        return inputPanel;
    }

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

    @Override
    protected ToDo createNewItem() {
        String taskDescription = taskDescriptionField.getText();
        String endDate = datePicker.getModel().getValue().toString();
        return new ToDo(taskDescription, endDate, false);
    }

    @Override
    protected void updateExistingItem(ToDo toDo) {
        toDo.setTaskDescription(taskDescriptionField.getText());
        toDo.setEndDate(datePicker.getModel().getValue().toString());
        service.update(toDo);
    }

    @Override
    protected void populateInputFields(ToDo toDo) {
        taskDescriptionField.setText(toDo.getTaskDescription());
        LocalDate endDate = LocalDate.parse(toDo.getEndDate());
        model.setValue(java.sql.Date.valueOf(endDate));
    }

    @Override
    protected void clearInputFields() {
        taskDescriptionField.setText("");
        model.setValue(null);
    }

}