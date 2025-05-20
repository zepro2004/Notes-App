package layout.panels;

import notes.Notes;
import notes.impl.NotesService;
import config.*;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;

/**
 * A Swing panel component providing a user interface for managing notes.
 * Extends GeneralPanel to inherit common CRUD functionality while implementing
 * note-specific features and behaviors.
 *
 * <p>Features:</p>
 * <ul>
 * <li>Input field for note title.</li>
 * <li>Text area for note content input/editing.</li>
 * <li>List displaying titles of all available notes.</li>
 * <li>Text area for viewing the full content of the selected note.</li>
 * <li>Buttons for saving, deleting, editing, and clearing notes.</li>
 * <li>Sort functionality for organizing notes by title</li>
 * </ul>
 *
 * <p>Layout Structure:</p>
 * <ul>
 * <li>Top: Input panel with title field, content area, and sort options</li>
 * <li>Center: Split pane with notes list and content viewer</li>
 * <li>Bottom: Action buttons inherited from GeneralPanel</li>
 * </ul>
 *
 * <p>This panel implements a two-pane view pattern, with the left pane showing a list of note titles
 * and the right pane displaying the full content of the selected note. The input fields at the top
 * allow for creating new notes or editing existing ones.</p>
 *
 * @version 1.0
 * @since 2025-04-23
 * @author Louis Bertrand Ntwali
 *
 * @see NotesService
 * @see Notes
 * @see GeneralPanel
 */
public class NotesPanel extends GeneralPanel<Notes> {

    /** Field for entering/editing note titles, limited to 50 characters */
    private JTextField noteTitleField;

    /** Text area for entering/editing note content, limited to 1000 characters */
    private JTextArea noteContentArea;

    /** Read-only area for displaying selected note's full content */
    private JTextArea contentViewer;

    /** Reference to the notes service for additional note-specific operations */
    private final NotesService notesManager;

    /**
     * Constructs a new NotesPanel with the specified service.
     * Initializes the UI and sets up note management functionality.
     *
     * @param service The notes service for handling data operations, must not be null
     * @throws IllegalArgumentException if service is null
     */
    public NotesPanel(NotesService service) {
        super(service);
        this.notesManager = service;
    }

    /**
     * Initializes all UI components and sets up event listeners.
     * Creates text fields, content areas, and configures the notes list
     * with sorting and selection handling.
     *
     * <p>Implementation details:</p>
     * <ul>
     * <li>Configures the title field for single-line input</li>
     * <li>Sets up the content area with scrolling support</li>
     * <li>Makes the content viewer read-only</li>
     * <li>Adds the "Clear All" button to the button panel</li>
     * <li>Sets up selection listener to update content viewer</li>
     * </ul>
     */
    @Override
    protected void initializeComponents() {
        noteTitleField = new JTextField(15);
        noteContentArea = new JTextArea(5, 15);
        JButton clearNoteButton = new JButton("Clear All");
        contentViewer = new JTextArea();

        contentViewer.setEditable(false);
        buttonPanel.add(clearNoteButton);

        // Configure sorting options
        sortOptions = new JComboBox<>(new String[] {
                "Title"
        });
        sortOptions.addActionListener(e -> onSort());

        // Set up event listeners
        clearNoteButton.addActionListener(e -> onClear());
        itemList.addListSelectionListener(e -> onListSelection(e));
    }

    /**
     * Handles the clear all action by removing all notes
     * and resetting the UI state.
     *
     * <p>This action permanently deletes all notes from the database.
     * No confirmation dialog is shown.</p>
     */
    private void onClear() {
        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to permanently delete all notes?",
                "Confirm Clear All",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );
        if(confirm == JOptionPane.YES_OPTION) {
            notesManager.clear();
            displayItems();
            clearInputFields();
            contentViewer.setText("");
        }
    }

    /**
     * Handles sorting of notes when user selects a sort option.
     * Currently supports sorting by title only.
     *
     * <p>This method triggers a re-render of the notes list
     * but maintains the current selection if possible.</p>
     */
    private void onSort() {
        String selected = (String) sortOptions.getSelectedItem();
        notesManager.sort(selected);
        updateListModel();
    }

    /**
     * Handles selection changes in the notes list.
     * Updates the content viewer to display the selected note's content.
     *
     * <p>When no note is selected, the content viewer is cleared.</p>
     *
     * @param e The list selection event, must not be null
     */
    private void onListSelection(ListSelectionEvent e) {
        if (!e.getValueIsAdjusting()) {
            int selected = itemList.getSelectedIndex();
            if (selected >= 0) {
                Notes n = notesManager.getAll().get(selected);
                contentViewer.setText(n.getContent());
            } else {
                contentViewer.setText("");
            }
        }
    }

    /**
     * Creates and configures the input panel layout.
     * Organizes title field, content area, and sort options
     * in a structured layout.
     *
     * <p>The layout consists of three main sections:</p>
     * <ul>
     * <li>Title panel (top): Contains the title label and input field</li>
     * <li>Content panel (middle): Contains the content label and scrollable text area</li>
     * <li>Sort panel (right): Contains sorting options</li>
     * </ul>
     *
     * @return The configured input panel, never null
     */
    @Override
    protected JPanel createInputPanel() {
        JPanel inputPanel = new JPanel(new BorderLayout());
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        titlePanel.add(new JLabel("Title: "));
        titlePanel.add(noteTitleField);

        JPanel contentPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        contentPanel.add(new JLabel("Content: "));
        contentPanel.add(new JScrollPane(noteContentArea));

        JPanel sortPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        sortPanel.add(new JLabel("Sort: "));
        sortPanel.add(sortOptions);
        sortPanel.setPreferredSize(new Dimension(450, (int)sortPanel.getPreferredSize().getHeight()));

        inputPanel.add(titlePanel, BorderLayout.NORTH);
        inputPanel.add(contentPanel, BorderLayout.CENTER);
        inputPanel.add(sortPanel, BorderLayout.EAST);

        return inputPanel;
    }

    /**
     * Creates the split pane layout for the center panel.
     * Divides space between notes list and content viewer.
     *
     * <p>The split pane allocates 30% of the width to the notes list
     * and 70% to the content viewer by default, but allows interactive resizing.</p>
     *
     * @return The configured split pane, never null
     */
    @Override
    protected JComponent createCenterPanel() {
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setResizeWeight(0.3);
        splitPane.setLeftComponent(new JScrollPane(itemList));
        splitPane.setRightComponent(new JScrollPane(contentViewer));

        return splitPane;
    }

    /**
     * Validates user input for both title and content.
     * Ensures title and content meet length requirements.
     *
     * <p>Validation rules:</p>
     * <ul>
     * <li>Title must be between 1 and 50 characters</li>
     * <li>Content must be between 1 and 1000 characters</li>
     * </ul>
     *
     * <p>Displays an appropriate error message if validation fails.</p>
     *
     * @return true if input is valid, false otherwise
     * @see config.Validation#isInvalidTitle(String)
     * @see config.Validation#isInvalidNoteContent(String)
     */
    @Override
    protected boolean validateInput() {
        String noteTitle = noteTitleField.getText();
        String noteContent = noteContentArea.getText();

        if (Validation.isInvalidTitle(noteTitle)) {
            showError("Title must be between 1 and 50 characters.");
            return false;
        }
        if (Validation.isInvalidNoteContent(noteContent)) {
            showError("Content must be between 1 and 1000 characters.");
            return false;
        }

        return true;
    }

    /**
     * Creates a new Notes instance from current input values.
     *
     * <p>This method assumes input validation has already been performed.</p>
     *
     * @return A new Notes object with title and content, never null
     */
    @Override
    protected Notes createNewItem() {
        return new Notes(noteTitleField.getText(), noteContentArea.getText());
    }

    /**
     * Updates an existing note with current input values.
     *
     * <p>This method assumes input validation has already been performed.
     * After updating the note properties, it calls the service to persist the changes.</p>
     *
     * @param editingItem The note to update, must not be null
     */
    @Override
    protected void updateExistingItem(Notes editingItem) {
        editingItem.setTitle(noteTitleField.getText());
        editingItem.setContent(noteContentArea.getText());
        notesManager.update(editingItem);
    }

    /**
     * Clears all input fields to their default state.
     *
     * <p>This removes all text from both the title field and content area.</p>
     */
    @Override
    protected void clearInputFields() {
        noteTitleField.setText("");
        noteContentArea.setText("");
    }

    /**
     * Populates input fields with values from existing note.
     *
     * <p>This method copies the title and content from the provided note
     * to their respective input components, preparing for editing.</p>
     *
     * @param note The note whose values should populate the inputs, must not be null
     */
    @Override
    protected void populateInputFields(Notes note) {
        noteTitleField.setText(note.getTitle());
        noteContentArea.setText(note.getContent());
    }

    @Override
    protected String getItemTypeName() {
        return "note";
    }

    @Override
    protected String getItemName(Notes note) {
        return note.getTitle();
    }
}