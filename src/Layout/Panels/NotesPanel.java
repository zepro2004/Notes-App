package Layout.Panels;


import Notes.Notes;
import Notes.NotesManager;
import config.*; // Assuming this import is necessary for Validation class

import javax.swing.*;
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
 * <p>The panel integrates with a {@link Notes.NotesManager} for handling
 * note data persistence and retrieval.</p>
 *
 * @see Notes.NotesManager
 * @see Notes.Notes
 * @see javax.swing.JPanel
 * @see javax.swing.JList
 * @see javax.swing.JTextArea
 * @see javax.swing.JTextField
 * @see javax.swing.JSplitPane
 */
public class NotesPanel extends JPanel {

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
    /**
     * The data model for the {@code notesList}, holding the titles of the notes.
     */
    private DefaultListModel<String> notesModel;
    /**
     * The list component displaying the titles of available notes. Selection changes trigger content display in {@code contentViewer}.
     */
    private JList<String> notesList;
    /**
     * Button to save a new note or the changes made to an existing note.
     */
    private JButton saveNoteButton;
    /**
     * Button to delete the currently selected note from the list and persistence.
     */
    private JButton deleteNoteButton;
    /**
     * Button to load the selected note's details into the input fields for editing.
     */
    private JButton editNoteButton;
    /**
     * Button intended to clear all notes via the NotesManager.
     */
    private JButton clearNoteButton;
    /**
     * Reference to the {@link Notes.NotesManager} instance used for managing note data.
     */
    private NotesManager notesManager;

    /**
     * Flag indicating whether the panel is currently in editing mode (modifying an existing note).
     * {@code true} if editing, {@code false} otherwise.
     */
    private boolean isEditting = false;
    /**
     * Holds the {@link Notes.Notes} object that is currently being edited.
     * {@code null} if not in editing mode or no note is selected for editing.
     */
    private Notes currentlyEditingNote = null;

    /**
     * Constructs a new {@code NotesPanel}.
     * Initializes the UI components, sets up the layout, configures event listeners,
     * and displays the initial list of notes retrieved via the provided {@code NotesManager}.
     *
     * @param notesManager The {@link Notes.NotesManager} instance to be used for note operations. Must not be null.
     */
    public NotesPanel(NotesManager notesManager) {
        this.notesManager = notesManager;
        initializeComponents();
        setupLayout();
        setupListeners();
    }

    /**
     * Initializes all the Swing components used in the panel.
     * This includes creating instances of text fields, text areas, list models, lists, buttons,
     * and configuring the read-only content viewer area.
     */
    private void initializeComponents() {
        noteTitleField = new JTextField(15);
        noteContentArea = new JTextArea(5, 15);
        notesModel = new DefaultListModel<>();
        notesList = new JList<>(notesModel);
        saveNoteButton = new JButton("Save");
        deleteNoteButton = new JButton("Delete");
        editNoteButton = new JButton("Edit");
        clearNoteButton = new JButton("Clear");
        contentViewer = new JTextArea();
        contentViewer.setEditable(false);
    }

    /**
     * Sets up the layout of the panel using {@link java.awt.BorderLayout} and {@link javax.swing.JSplitPane}.
     * An input panel containing title, content area, and buttons is placed at the NORTH.
     * A split pane is placed in the CENTER, dividing the area horizontally between the notes list
     * (left) and the content viewer (right). Finally, it calls {@link #displayNotes()} to populate the list.
     */
    private void setupLayout() {
        setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel(new BorderLayout());
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        titlePanel.add(new JLabel("Note Title: "));
        titlePanel.add(noteTitleField);

        JPanel contentPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        contentPanel.add(new JLabel("Note Content: "));
        contentPanel.add(new JScrollPane(noteContentArea));

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(saveNoteButton);
        buttonPanel.add(deleteNoteButton);
        buttonPanel.add(editNoteButton);
        buttonPanel.add(clearNoteButton);

        inputPanel.add(titlePanel, BorderLayout.NORTH);
        inputPanel.add(contentPanel, BorderLayout.CENTER);
        inputPanel.add(buttonPanel, BorderLayout.SOUTH);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setResizeWeight(0.3);
        splitPane.setLeftComponent(new JScrollPane(notesList));
        splitPane.setRightComponent(new JScrollPane(contentViewer));

        add(inputPanel, BorderLayout.NORTH);
        add(splitPane, BorderLayout.CENTER);

        displayNotes();
    }

    /**
     * Sets up the event listeners for the interactive components: the notes list and the buttons.
     * Defines behavior for list selection changes (updating the content viewer), saving notes
     * (handling both new and edited notes, including validation), deleting notes, editing notes
     * (populating input fields), and clearing all notes via the manager without confirmation.
     */
    private void setupListeners() {
        notesList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedIndex = notesList.getSelectedIndex();
                if (selectedIndex != -1) {
                    Notes selectedNote = notesManager.getNotes().get(selectedIndex);
                    contentViewer.setText(selectedNote.getContent());
                } else {
                    contentViewer.setText("");
                }
            }
        });

        saveNoteButton.addActionListener(e -> {
            String noteTitle = noteTitleField.getText();
            String noteContent = noteContentArea.getText();
            StringBuilder errorMsg = new StringBuilder();

            if (Validation.isInvalidTitle(noteTitle)) {
                errorMsg.append("Title must be between 1 and 50 characters.\n");
            }
            if (Validation.isInvalidNoteContent(noteContent)) {
                errorMsg.append("Content must be between 1 and 1000 characters.");
            }
            if (!errorMsg.isEmpty()) {
                JOptionPane.showMessageDialog(this, errorMsg.toString(),
                        "Invalid Input", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (isEditting && currentlyEditingNote != null) {
                currentlyEditingNote.setTitle(noteTitle);
                currentlyEditingNote.setContent(noteContent);
                notesManager.updateNoteInDatabase(currentlyEditingNote);
                isEditting = false;
                currentlyEditingNote = null;
                displayNotes();

            } else {
                notesManager.addNote(noteTitle, noteContent);
                displayNotes();
            }

            noteTitleField.setText("");
            noteContentArea.setText("");
        });


        deleteNoteButton.addActionListener( e -> {
            int selectedIndex = notesList.getSelectedIndex();
            if (selectedIndex != -1) {
                notesManager.removeNote(notesManager.getNotes().get(selectedIndex));
                displayNotes();
            }
        });


        editNoteButton.addActionListener(e -> {
            int selectedIndex = notesList.getSelectedIndex();
            if(selectedIndex != -1) {
                isEditting = true;
                currentlyEditingNote = notesManager.getNotes().get(selectedIndex);
                noteTitleField.setText(currentlyEditingNote.getTitle());
                noteContentArea.setText(currentlyEditingNote.getContent());
            }
        });


        clearNoteButton.addActionListener( e -> {
            notesManager.clearNotes(); // Original code called clearNotes directly
        } );
    }

    /**
     * Refreshes the list of note titles displayed in the UI.
     * It first tells the {@link NotesManager} to refresh its internal list (e.g., reload from database),
     * then clears the current {@code notesModel}, and finally repopulates the model
     * with the titles of the notes obtained from the {@code NotesManager}.
     * The original code did not explicitly manage the contentViewer state here.
     */
    public void displayNotes() {
        notesManager.refreshNotes();
        notesModel.clear();
        for (Notes note : notesManager.getNotes()) {
            notesModel.addElement(note.getTitle());
        }
    }
}