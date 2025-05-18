/**
 * Application entry point and bootstrap components.
 *
 * <p>This package contains the main application entry point and bootstrap classes
 * responsible for initializing the application, setting up dependencies, and launching
 * the user interface. It serves as the starting point for the application's execution flow.</p>
 *
 * <p>Key components include:</p>
 * <ul>
 *   <li>{@link main.App} - Main application class with the {@code main} method</li>
 * </ul>
 *
 * <p>Execution flow:</p>
 * <ul>
 *   <li>The {@code main} method in {@link main.App} serves as the application entry point</li>
 *   <li>Core services and database connections are initialized</li>
 *   <li>The main UI frame ({@link layout.MainLayout}) is instantiated</li>
 *   <li>Control is delegated to the Swing EDT (Event Dispatch Thread)</li>
 * </ul>
 *
 * <p>The main package sits at the top of the application's layered architecture,
 * orchestrating the initialization and coordination of UI components ({@link layout}),
 * service layer ({@link todo.impl}, {@link notes.impl}), and data access layer.</p>
 *
 * @version 1.0
 * @since 2025-04-23
 * @see layout.MainLayout
 * @see javax.swing.SwingUtilities#invokeLater(Runnable)
 */
package main;