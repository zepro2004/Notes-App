package Layout.Panels;

import Notes.Notes;
import Notes.NotesManager;
import config.Validation;

import javax.swing.*;
import java.awt.*;

public class NotesPanel extends JPanel {
    private JTextField noteTitleField;
    private JTextArea noteContentArea;
    private JTextArea contentViewer;
    private DefaultListModel<String> notesModel;
    private JList<String> notesList;
    private JButton saveNoteButton;
    private JButton deleteNoteButton;
    private JButton clearNoteButton;
    private NotesManager notesManager;

    public NotesPanel(NotesManager notesManager) {
        this.notesManager = notesManager;
        initializeComponents();
        setupLayout();
        setupListeners();
    }

    private void initializeComponents() {
        noteTitleField = new JTextField(15);
        noteContentArea = new JTextArea(5, 15);
        notesModel = new DefaultListModel<>();
        notesList = new JList<>(notesModel);
        saveNoteButton = new JButton("Save");
        deleteNoteButton = new JButton("Delete");
        clearNoteButton = new JButton("Clear");
        contentViewer = new JTextArea();
        contentViewer.setEditable(false);
        contentViewer.setWrapStyleWord(true);
        contentViewer.setLineWrap(true);
    }

    private void setupLayout() {
        setLayout(new BorderLayout());

        // Input Panel
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
        buttonPanel.add(clearNoteButton);

        inputPanel.add(titlePanel, BorderLayout.NORTH);
        inputPanel.add(contentPanel, BorderLayout.CENTER);
        inputPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Split Pane
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setResizeWeight(0.3);
        splitPane.setLeftComponent(new JScrollPane(notesList));
        splitPane.setRightComponent(new JScrollPane(contentViewer));

        add(inputPanel, BorderLayout.NORTH);
        add(splitPane, BorderLayout.CENTER);

        displayNotes();
    }

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

            if (!Validation.isValidNoteTitle(noteTitle)) {
                errorMsg.append("Title must be between 1 and 50 characters.\n");
            }
            if (!Validation.isValidNoteContent(noteContent)) {
                errorMsg.append("Content must be between 1 and 1000 characters.");
            }
            if (errorMsg.length() > 0) {
                JOptionPane.showMessageDialog(this, errorMsg.toString(),
                        "Invalid Input", JOptionPane.ERROR_MESSAGE);
                return;
            }
            notesManager.addNote(noteTitle, noteContent);
            displayNotes();

            notesList.setSelectedIndex(0);
            notesList.ensureIndexIsVisible(0);

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

        clearNoteButton.addActionListener( e -> {
            notesManager.clearNotes();
        } );

    }

    public void displayNotes() {
        notesManager.refreshNotes();
        notesModel.clear();
        for (Notes note : notesManager.getNotes()) {
            notesModel.addElement(note.getTitle());
        }
    }
}
