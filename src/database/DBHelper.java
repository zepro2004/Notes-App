package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * A utility class providing helper methods for database interactions,
 * primarily focused on establishing database connections.
 * <p>
 * This class centralizes the database connection logic, ensuring that
 * components use a consistent method to obtain a connection to the
 * predefined application database (SQLite in this case).
 * </p>
 * Contains only static members and is not intended for instantiation.
 *
 * @version 1.0
 * @since 2025-04-23
 * @author Louis Bertrand Ntwali
 *
 * @see java.sql.Connection
 * @see java.sql.DriverManager
 */
public class DBHelper {

    /**
     * The JDBC connection URL for the application's SQLite database.
     * Specifies the driver type (jdbc:sqlite) and the relative path to the
     * database file (resources/notes_todo.db) from the application's
     * working directory.
     */
    private static final String DB_URL = "jdbc:sqlite:resources/notes_todo.db";

    /**
     * Establishes and returns a new connection to the database specified by {@link #DB_URL}.
     * <p>
     * This method utilizes the {@link java.sql.DriverManager} to obtain a connection.
     * It is crucial that the caller manages the lifecycle of the returned connection,
     * ensuring it is properly closed (e.g., using a try-with-resources statement)
     * to release database resources and avoid leaks. Failure to close connections
     * can lead to resource exhaustion.
     * </p>
     *
     * @return A new, open {@link Connection} to the application database.
     * @throws SQLException if the SQLite JDBC driver cannot be found, the database file
     * specified by the URL cannot be opened or created, or another
     * database access error occurs during the connection attempt.
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }

    /**
     * Private constructor to prevent instantiation of this utility class.
     * Throws an IllegalStateException if an attempt is made to instantiate.
     */
    private DBHelper() {
        // Utility classes should not be instantiated.
        throw new IllegalStateException("Utility class should not be instantiated.");
    }
}