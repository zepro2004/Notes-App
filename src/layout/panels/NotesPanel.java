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
 * This panel allows users to create, view, edit, and delete notes.
 * It features input fields for title and content, a list displaying note titles,
 * and a separate area to view the full content of the selected note.
 *
 * <p>Features:</p>
 * <ul>
 * <li>Input field for note title.</li>
 * <li>Text area for note content input/editing.</li>
 * <li>List displaying titles of all available notes.</li>
 * <li>Text area for viewing the full content of the selected note.</li>
 * <li>Buttons for saving, deleting, editing, and clearing notes.</li>
 * </ul>
 *
 * <p>The panel integrates with a {@link NotesService} for handling
 * note data persistence and retrieval.</p>
 *
 *
 * @version 1.0
 * @since 2025-04-23
 * @author Louis Bertrand Ntwali
 *
 * @see NotesService
 * @see Notes
 * @see JPanel
 * @see JList
 * @see JTextArea
 * @see JTextField
 * @see JSplitPane
 */
public class NotesPanel extends GeneralPanel<Notes> {

    /**
     * Text field for entering or editing the title of a note.
     */
    private JTextField noteTitleField;
    /**
     * Text area for entering or editing the content of a note.
     */
    private JTextArea noteContentArea;
    /**
     * Text area dedicated to displaying the full content of the note selected in {@code notesList}. This area is read-only.
     */
    private JTextArea contentViewer;

    private final NotesService notesManager;

    /**
     * Constructs a new {@code NotesPanel}.
     * Initializes the UI components, sets up the layout, configures event listeners,
     * and displays the initial list of notes retrieved via the provided {@code NotesManager}.
     *
     * @param service The {@link NotesService} instance to be used for note operations. Must not be null.
     */
    public NotesPanel(NotesService service) {
        super(service);
        this.notesManager = service;
    }

    /**
     * Initializes all the Swing components used in the panel.
     * This includes creating instances of text fields, text areas, list models, lists, buttons,
     * and configuring the read-only content viewer area.
     */
    @Override
    protected void initializeComponents() {
        noteTitleField = new JTextField(15);
        noteContentArea = new JTextArea(5, 15);
        JButton clearNoteButton = new JButton("Clear All");
        contentViewer = new JTextArea();

        contentViewer.setEditable(false);
        buttonPanel.add(clearNoteButton);

        sortOptions = new JComboBox<>(new String[] {
                "Title"
        });

        clearNoteButton.addActionListener(e -> onClear());
        itemList.addListSelectionListener(e -> onListSelection(e));
    }

    private void onClear() {
        notesManager.clear();
        displayItems();
        clearInputFields();
        contentViewer.setText("");
    }

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

    @Override
    protected JComponent createCenterPanel() {
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setResizeWeight(0.3);
        splitPane.setLeftComponent(new JScrollPane(itemList));
        splitPane.setRightComponent(new JScrollPane(contentViewer));

        return splitPane;
    }


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

    @Override
    protected Notes createNewItem() {
        return new Notes(noteTitleField.getText(), noteContentArea.getText());
    }

    @Override
    protected void updateExistingItem(Notes editingItem) {
        editingItem.setTitle(noteTitleField.getText());
        editingItem.setContent(noteContentArea.getText());
        notesManager.update(editingItem);
    }

    @Override
    protected void clearInputFields() {
        noteTitleField.setText("");
        noteContentArea.setText("");
    }


    @Override
    protected void populateInputFields(Notes note) {
        noteTitleField.setText(note.getTitle());
        noteContentArea.setText(note.getContent());
    }

}