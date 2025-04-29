package database;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Handles the initialization of the application's database schema.
 * <p>
 * This utility class provides functionality to ensure that the necessary tables
 * (e.g., for notes and tasks/todos) exist in the database defined by {@link DBHelper}.
 * It is typically invoked once during application startup to prepare the
 * database structure before any data operations are performed.
 * </p>
 * This class follows the utility pattern with only static members and is not
 * intended for instantiation.
 *
 * @version 1.0
 * @since 2025-04-23
 * @author Louis Bertrand Ntwali
 *
 * @see DBHelper
 * @see java.sql.Statement#execute(String)
 */
public class DBInitializer {

    /**
     * Initializes the database by creating the required application tables
     * ({@code notes} and {@code todos}) if they do not already exist.
     * <p>
     * This method defines the SQL statements for creating the tables
     * using the {@code CREATE TABLE IF NOT EXISTS} syntax. This syntax makes
     * the operation idempotent, meaning it is safe to run this method multiple times
     * without causing errors or unintended side effects after the initial creation.
     * </p>
     * It performs the following sequence:
     * <ol>
     * <li>Defines the SQL DDL (Data Definition Language) string for the 'notes' table.</li>
     * <li>Defines the SQL DDL string for the 'todos' table.</li>
     * <li>Obtains a database connection using {@link DBHelper#getConnection()}.</li>
     * <li>Creates an SQL {@link Statement} object from the connection.</li>
     * <li>Executes both CREATE TABLE statements using the Statement object.</li>
     * <li>Prints a confirmation message to standard output if successful.</li>
     * </ol>
     * database resources ({@link Connection} and {@link Statement}) are managed
     * automatically using a try-with-resources block, ensuring they are closed
     * even if exceptions occur.
     * <p>
     * If any {@link SQLException} occurs during the process (e.g., connection issues,
     * syntax errors in SQL, permissions problems), an error message containing
     * the exception details is printed to standard error (System.err).
     * </p>
     */
    public static void initializeDatabase() {
        String createNotesTable = """
              CREATE TABLE IF NOT EXISTS notes (
                  id INTEGER PRIMARY KEY AUTOINCREMENT,
                  title TEXT NOT NULL,
                  content TEXT
              );
          """;

        String createTasksTable = """
              CREATE TABLE IF NOT EXISTS todos (
                  id INTEGER PRIMARY KEY AUTOINCREMENT,
                  description TEXT NOT NULL,
                  end_date TEXT,
                  completed BOOLEAN NOT NULL DEFAULT 0 -- SQLite uses 0 for false, 1 for true
              );
          """;

        try (Connection conn = DBHelper.getConnection();
             Statement stmt = conn.createStatement()) {

            stmt.execute(createNotesTable);
            stmt.execute(createTasksTable);

            System.out.println("database tables 'notes' and 'todos' initialized successfully (or already exist).");

        } catch (SQLException e) {
            System.err.println("Error initializing database tables: " + e.getMessage());
            // Consider more robust error handling for production: logging, re-throwing specific exceptions, etc.
        }
    }

    /**
     * Private constructor to prevent instantiation of this utility class.
     * Throws an IllegalStateException if an attempt is made to instantiate.
     */
    private DBInitializer() {
        // Utility classes, containing only static methods, should not be instantiated.
        throw new IllegalStateException("Utility class DBInitializer should not be instantiated.");
    }
}
