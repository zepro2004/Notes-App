package Layout;

import Layout.Panels.*;
import Notes.NotesManager;
import ToDo.ToDoManager;
import javax.swing.*;

/**
 * Manages the main application window layout for the ToDo List and Notes App.
 * This class initializes the necessary managers (for notes and ToDo items)
 * and sets up the main JFrame, organizing the ToDo and Notes panels within a
 * JTabbedPane.
 *
 * @see Layout.Panels.NotesPanel
 * @see Layout.Panels.ToDoPanel
 * @see Notes.NotesManager
 * @see ToDo.ToDoManager
 * @see javax.swing.JFrame
 * @see javax.swing.JTabbedPane
 */
public class MainLayout {
    /**
     * Manages the notes data and operations. Passed to the {@link NotesPanel}.
     */
    private NotesManager notesManager;
    /**
     * Manages the ToDo items data and operations. Passed to the {@link ToDoPanel}.
     */
    private ToDoManager toDoManager;
    /**
     * The main window frame for the application.
     */
    private JFrame mainFrame;

    /**
     * Constructs the main layout of the application.
     * Initializes the data managers and sets up the main application frame with its components.
     */
    public MainLayout() {
        initializeManagers();
        setupMainFrame();
    }

    /**
     * Initializes the {@link NotesManager} and {@link ToDoManager} instances
     * required by the panels.
     */
    private void initializeManagers() {
        notesManager = new NotesManager();
        toDoManager = new ToDoManager();
    }

    /**
     * Creates and configures the main application window ({@link JFrame}).
     * Sets the title, default close operation, and size. It then creates a
     * {@link JTabbedPane} to hold the {@link NotesPanel} and {@link ToDoPanel},
     * adds the tabbed pane to the frame, and makes the frame visible.
     */
    private void setupMainFrame() {
        mainFrame = new JFrame("ToDo List And Notes App");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(800, 600);

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Notes", new NotesPanel(notesManager));
        tabbedPane.addTab("To Do List", new ToDoPanel(toDoManager));

        mainFrame.add(tabbedPane);
        mainFrame.setVisible(true);
    }
}