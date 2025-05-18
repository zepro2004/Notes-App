package main;

import database.DBInitializer;
import layout.MainLayout;

/**
 * The main entry point for the application.
 * <p>
 * This class serves as the starting point when the application is launched.
 * Its primary responsibility is to orchestrate the initial setup sequence,
 * which involves initializing the necessary database components and then
 * launching the main user interface (UI) layout.
 * </p>
 * <p> The application follows a simple startup procedure: </p>
 * <ol>
 * <li>Initialize the database using {@link database.DBInitializer}.</li>
 * <li>Create and display the main application window using {@link layout.MainLayout}.</li>
 * </ol>
 * <p> Any exceptions thrown during this critical startup phase are propagated upwards,
 * potentially terminating the application if not handled by the JVM environment.
 * </p>
 *
 * @version 1.0
 * @since 2025-04-23
 * @author Louis Bertrand Ntwali
 *
 * @see database.DBInitializer
 * @see layout.MainLayout
 */
public class App {

    /**
     * The main execution method that serves as the application's entry point.
     * <p>
     * This method is called by the Java Virtual Machine (JVM) when the application
     * starts. It performs the essential initial setup steps required for the
     * application to run.
     * </p>
     * <p> The sequence of operations is: </p>
     * <ol>
     * <li>Calls {@link database.DBInitializer#initializeDatabase()} to ensure the
     * database is ready for use. This might involve establishing connections,
     * creating schemas if they don't exist, or running initial data scripts.</li>
     * <li>Instantiates the {@link layout.MainLayout} class. The constructor of
     * {@code MainLayout} is expected to build and display the primary
     * user interface of the application.</li>
     * </ol>
     *
     * @param args Command-line arguments passed to the application at startup.
     * These arguments are not explicitly used in this basic setup
     * but are available if needed for configuration or specific modes.
     *
     * @throws Exception If any unrecoverable error occurs during the database
     * initialization (e.g., connection failure, SQL errors) or
     * during the instantiation and display of the main layout
     * (e.g., UI resource loading issues, configuration errors).
     * The {@code throws Exception} clause indicates that checked
     * exceptions from the called methods are not caught here
     * and will be propagated.
     */
    public static void main(String[] args) throws Exception {
        DBInitializer.initializeDatabase();
        new MainLayout();
    }
}