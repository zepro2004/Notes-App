package Layout;

import ToDo.ToDoManager;
import java.awt.Dimension;
import javax.swing.*;

public class Layout {
    // Buttons
    private JButton addTaskButton;
    private JButton deleteTaskButton;
    private JButton saveTaskButton;
    private JButton addNoteButton;
    private JButton saveNoteButton;
    private JButton deleteNoteButton;
    private JButton clearNoteButton;

    // Task fields
    private JTextField taskDescriptionField;
    private JTextField taskEndDateField;
    private DefaultListModel<String> taskListModel;
    private JList<String> taskList;

    // Note fields
    private JTextField noteTitleField;
    private JTextArea noteContentArea;
    private DefaultListModel<String> notesModel;
    private JList<String> notesList;

    private ToDoManager manager = new ToDoManager();

    public Layout() {
        setupUI();
        setupListeners();
    }

    private void setupUI() {
        JFrame frame = new JFrame("ToDo List And Notes App");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setResizable(true);

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);

        // Notes Panel
        JPanel notesPanel = new JPanel();
        notesPanel.setLayout(new BoxLayout(notesPanel, BoxLayout.Y_AXIS));

        JPanel notesInputPanel = new JPanel();
        noteTitleField = new JTextField(15);
        notesInputPanel.add(new JLabel("Note Title: "));
        notesInputPanel.add(noteTitleField);

        JPanel contentInputPanel = new JPanel();
        noteContentArea = new JTextArea(5, 15);
        contentInputPanel.add(new JLabel("Note Content: "));
        contentInputPanel.add(new JScrollPane(noteContentArea));

        JPanel buttonPanel = new JPanel();
        addNoteButton = new JButton("Add Note");
        saveNoteButton = new JButton("Save");
        deleteNoteButton = new JButton("Delete");
        clearNoteButton = new JButton("Clear");
        buttonPanel.add(addNoteButton);
        buttonPanel.add(saveNoteButton);
        buttonPanel.add(deleteNoteButton);
        buttonPanel.add(clearNoteButton);

        notesPanel.add(notesInputPanel);
        notesPanel.add(contentInputPanel);
        notesPanel.add(buttonPanel);

        JPanel savedNotesPanel = new JPanel();
        notesModel = new DefaultListModel<>();
        notesList = new JList<>(notesModel);
        savedNotesPanel.add(new JScrollPane(notesList));

        // To-Do Panel
        JPanel toDoListPanel = new JPanel();
        toDoListPanel.setLayout(new BoxLayout(toDoListPanel, BoxLayout.Y_AXIS));

        JPanel taskInputPanel = new JPanel();
        taskDescriptionField = new JTextField(15);
        taskInputPanel.add(new JLabel("Task Description: "));
        taskInputPanel.add(taskDescriptionField);

        JPanel endDatePanel = new JPanel();
        taskEndDateField = new JTextField(15);
        endDatePanel.add(new JLabel("End Date: "));
        endDatePanel.add(taskEndDateField);

        taskListModel = new DefaultListModel<>();
        taskList = new JList<>(taskListModel);
        JScrollPane taskScrollPane = new JScrollPane(taskList);
        taskScrollPane.setPreferredSize(new Dimension(300, 150));

        JPanel taskButtonPanel = new JPanel();
        addTaskButton = new JButton("Add Task");
        deleteTaskButton = new JButton("Delete");
        saveTaskButton = new JButton("Save");
        taskButtonPanel.add(addTaskButton);
        taskButtonPanel.add(deleteTaskButton);
        taskButtonPanel.add(saveTaskButton);

        toDoListPanel.add(taskInputPanel);
        toDoListPanel.add(endDatePanel);
        toDoListPanel.add(taskScrollPane);
        toDoListPanel.add(taskButtonPanel);

        // Load tasks from DB
        manager.loadTasksFromDatabase();
        for (String summary : manager.getTaskSummaries()) {
            taskListModel.addElement(summary);
        }

        tabbedPane.addTab("Notes", notesPanel);
        tabbedPane.addTab("To Do List", toDoListPanel);

        frame.add(tabbedPane);
        frame.setVisible(true);
    }

    private void setupListeners() {
      addNoteButton.addActionListener(e -> {
        String noteTitle = noteTitleField.getText();
        String noteContent = noteContentArea.getText();
        if (!noteTitle.isEmpty() && !noteContent.isEmpty()) {
            notesModel.addElement(noteTitle + ": " + noteContent);
            noteTitleField.setText("");
            noteContentArea.setText("");
        } else {
            JOptionPane.showMessageDialog(null, "Please fill in both fields.");
        }
        });
      addTaskButton.addActionListener(e -> {
        String taskDescription = taskDescriptionField.getText();
        String endDate = taskEndDateField.getText();
        if (!taskDescription.isEmpty() && !endDate.isEmpty()) {
            manager.addTask(taskDescription, endDate);
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
            String selectedTask = taskListModel.getElementAt(selectedIndex);
            taskListModel.remove(selectedIndex);
            manager.removeTask(manager.getTasks().get(selectedIndex));
        } else {
            JOptionPane.showMessageDialog(null, "Please select a task to delete.");
        }
      });
    }
}
