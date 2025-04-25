package Layout;


import Notes.impl.NotesDatabaseManager;
import Layout.Panels.*;
import Notes.impl.NotesService;
import Notes.interfaces.NotesDatabaseManagement;
import ToDo.ToDoManager;
import javax.swing.*;

/**
 * Manages the main application window layout for the ToDo List and Notes Main.App.
 * This class initializes the necessary managers (for notes and ToDo items)
 * and sets up the main JFrame, organizing the ToDo and Notes panels within a
 * JTabbedPane.
 *
 * @version 1.0
 * @since 2025-04-23
 * @author Louis Bertrand Ntwali
 *
 * @see Layout.Panels.NotesPanel
 * @see Layout.Panels.ToDoPanel
 * @see NotesService
 * @see ToDo.ToDoManager
 * @see javax.swing.JFrame
 * @see javax.swing.JTabbedPane
 */
public class MainLayout extends JFrame{
    /**
     * Manages the notes data and operations. Passed to the {@link NotesPanel}.
     */
    private NotesService notesService;
    /**
     * Manages the ToDo items data and operations. Passed to the {@link ToDoPanel}.
     */
    private ToDoManager toDoManager;

    /**
     * Constructs the main layout of the application.
     * Initializes the data managers and sets up the main application frame with its components.
     */
    public MainLayout() {
        initializeManagers();
        setupMainFrame();
    }

    /**
     * Initializes the {@link NotesService} and {@link ToDoManager} instances
     * required by the panels.
     */
    private void initializeManagers() {
        NotesDatabaseManagement databaseManagement = new NotesDatabaseManager();
        notesService = new NotesService(databaseManagement);
        toDoManager = new ToDoManager();
    }

    /**
     * Creates and configures the main application window ({@link JFrame}).
     * Sets the title, default close operation, and size. It then creates a
     * {@link JTabbedPane} to hold the {@link NotesPanel} and {@link ToDoPanel},
     * adds the tabbed pane to the frame, and makes the frame visible.
     */
    private void setupMainFrame() {
        /**
         * The main window frame for the application.
         */
        JFrame mainFrame = new JFrame("ToDo List And Notes Main.App");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(800, 600);

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Notes", new NotesPanel(notesService));
        tabbedPane.addTab("To Do List", new ToDoPanel(toDoManager));

        mainFrame.add(tabbedPane);
        mainFrame.setVisible(true);
    }
}