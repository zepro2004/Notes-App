package layout;

import notes.impl.NotesDatabaseManager;
import notes.impl.NotesService;
import notes.interfaces.NotesDatabaseManagement;
import todo.impl.ToDoDatabaseManager;
import todo.impl.ToDoService;
import todo.interfaces.ToDoDatabaseManagement;

import javax.swing.*;
import layout.panels.*;

import java.awt.*;

/**
 * Manages the main application window layout for the todo List and notes main.App.
 * This class serves as the primary container and entry point for the user interface,
 * implementing a tabbed interface design pattern to organize functionality.
 *
 * <p>Key Responsibilities:</p>
 * <ul>
 * <li>Initializing database connections and service layers</li>
 * <li>Setting up the main application frame and tabbed interface</li>
 * <li>Configuring the application look and feel</li>
 * <li>Organizing the Notes and ToDo panels within the UI</li>
 * </ul>
 *
 * <p>Application Structure:</p>
 * <ul>
 * <li>The application uses a layered architecture with separation of concerns</li>
 * <li>UI Layer: MainLayout (this class), NotesPanel, ToDoPanel</li>
 * <li>Service Layer: NotesService, ToDoService</li>
 * <li>Data Access Layer: NotesDatabaseManager, ToDoDatabaseManager</li>
 * </ul>
 *
 * <p>The main window contains a tabbed pane with panels for Notes and ToDo items,
 * each handling its own domain-specific functionality while sharing a common design pattern.</p>
 *
 * @version 1.0
 * @since 2025-04-23
 * @author Louis Bertrand Ntwali
 *
 * @see layout.panels.NotesPanel
 * @see layout.panels.ToDoPanel
 * @see NotesService
 * @see ToDoService
 * @see javax.swing.JFrame
 * @see javax.swing.JTabbedPane
 */
public class MainLayout extends JFrame{
    /**
     * Service layer component for notes functionality.
     * Manages the business logic for note operations and provides
     * an abstraction over the notes database. This service is instantiated
     * in {@link #initializeManagers()} and passed to the {@link NotesPanel}.
     */
    private NotesService notesService;

    /**
     * Service layer component for todo functionality.
     * Manages the business logic for task operations and provides
     * an abstraction over the todo database. This service is instantiated
     * in {@link #initializeManagers()} and passed to the {@link ToDoPanel}.
     */
    private ToDoService toDoManager;

    /**
     * Constructs the main layout of the application.
     * Performs the following initialization sequence:
     * <ol>
     * <li>Initializes the database managers and service components</li>
     * <li>Sets up the main application frame with tabbed interface</li>
     * <li>Attempts to apply the Nimbus look and feel for modern UI styling</li>
     * </ol>
     *
     * <p>If the Nimbus look and feel is not available, the application will
     * fall back to the system default look and feel.</p>
     */
    public MainLayout() {
        initializeManagers();
        setupMainFrame();
        // In main application class
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
            // Or for a more modern look:
            // UIManager.setLookAndFeel("com.formdev.flatlaf.FlatLightLaf");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Initializes the service layer components of the application.
     *
     * <p>This method performs the following steps:</p>
     * <ol>
     * <li>Creates database management instances for notes and todo items</li>
     * <li>Initializes service objects with their respective database managers</li>
     * </ol>
     *
     * <p>The initialized services are stored in instance variables and later
     * passed to their respective UI panels.</p>
     *
     * @see NotesDatabaseManager
     * @see ToDoDatabaseManager
     */
    private void initializeManagers() {
        NotesDatabaseManagement notesDatabaseManagement = new NotesDatabaseManager();
        ToDoDatabaseManagement todoDatabaseManagement = new ToDoDatabaseManager();
        notesService = new NotesService(notesDatabaseManagement);
        toDoManager = new ToDoService(todoDatabaseManagement);
    }

    /**
     * Creates and configures the main application window.
     *
     * <p>This method performs the following UI setup:</p>
     * <ol>
     *   <li>Creates a JFrame with title "todo List And notes main.App"</li>
     *   <li>Configures window properties (size 800x600, close operation)</li>
     *   <li>Creates a tabbed pane with two tabs:
     *     <ul>
     *       <li>Notes tab – Contains the NotesPanel for note management</li>
     *       <li>To Do List tab – Contains the ToDoPanel for task management</li>
     *     </ul>
     *   </li>
     *   <li>Adds the tabbed pane to the frame and makes it visible</li>
     * </ol>
     *
     * <p>The NotesPanel and ToDoPanel are initialized with their respective
     * service instances to provide data access and business logic.</p>
     *
     * @see JTabbedPane
     * @see NotesPanel
     * @see ToDoPanel
     */
    private void setupMainFrame() {
        /*
         * The main window frame for the application.
         */
        JFrame mainFrame = new JFrame("todo List And notes main.App");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(800, 600);

        Font appFont = new Font("Dialog", Font.PLAIN, 20);
        setGlobalFont(appFont);

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Notes", new NotesPanel(notesService));
        tabbedPane.addTab("To Do List", new ToDoPanel(toDoManager));

        mainFrame.add(tabbedPane);
        mainFrame.setVisible(true);
    }

     public static void setGlobalFont(Font font) {
        UIManager.put("Button.font", font);
        UIManager.put("Label.font", font);
        UIManager.put("TextField.font", font);
        UIManager.put("TextArea.font", font);
        UIManager.put("ComboBox.font", font);
        UIManager.put("Menu.font", font);
        UIManager.put("MenuItem.font", font);
        UIManager.put("CheckBox.font", font);
        UIManager.put("RadioButton.font", font);
        UIManager.put("Table.font", font);
        UIManager.put("Tree.font", font);
        UIManager.put("TabbedPane.font", font);
        // Add other components as needed
    }
}
