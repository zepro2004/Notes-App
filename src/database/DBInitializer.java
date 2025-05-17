package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Database initialization utility class for creating the required database schema.
 * <p>
 * This class is responsible for ensuring that the database and necessary tables exist
 * before the application attempts to use them. It creates the database if it doesn't
 * exist and initializes the table structure for the Notes and ToDo application.
 * <p>
 * The class follows the utility pattern with a private constructor to prevent instantiation.
 * It creates two essential tables:
 * <ul>
 *     <li>notes - Stores note entries with title and content</li>
 *     <li>todos - Stores task entries with description, end date and completion status</li>
 * </ul>
 *
 * @see DBHelper
 * @see DatabaseConfig
 */
public class DBInitializer {
    /**
     * Private constructor to prevent instantiation of this utility class.
     *
     * @throws IllegalStateException if an attempt is made to instantiate this class
     */
    private DBInitializer() { throw new IllegalStateException("Utility class"); }

    /**
     * Initializes the database by creating required tables if they don't exist.
     * <p>
     * This method ensures the database exists by calling {@link #createDatabaseIfNotExists()},
     * then creates the following tables:
     * <ul>
     *     <li>notes - For storing note entries</li>
     *     <li>todos - For storing task entries</li>
     * </ul>
     *
     * @throws SQLException if any database access errors occur during initialization
     */
    public static void initializeDatabase() throws SQLException {
        createDatabaseIfNotExists();
        String createNotesTable =  """
            CREATE TABLE IF NOT EXISTS notes (
                id INT PRIMARY KEY AUTO_INCREMENT,
                title VARCHAR(255) NOT NULL,
                content TEXT
            );
        """;
        String createTasksTable = """
            CREATE TABLE IF NOT EXISTS todos (
                id INT PRIMARY KEY AUTO_INCREMENT,
                description VARCHAR(255) NOT NULL,
                end_date VARCHAR(255),
                completed BOOLEAN NOT NULL DEFAULT 0
            );
        """;
        try (Connection conn = DBHelper.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(createNotesTable);
            stmt.execute(createTasksTable);
        } catch (SQLException e) {
            throw new SQLException("Issue with creating the tables and accessing the database", e);
        }
    }

    /**
     * Creates the application database if it doesn't already exist.
     * <p>
     * This method connects to the database server using the configuration from
     * {@link DatabaseConfig} and creates the 'notes-todo' database if it doesn't
     * already exist.
     *
     * @throws SQLException if a database access error occurs or the database
     *                      cannot be created for any reason
     * @see DatabaseConfig#getUrl()
     * @see DatabaseConfig#getUsername()
     * @see DatabaseConfig#getPassword()
     */
    private static void createDatabaseIfNotExists() throws SQLException {
        try {
            String url = DatabaseConfig.getUrl();
            String username = DatabaseConfig.getUsername();
            String password = DatabaseConfig.getPassword();
            String serverUrl = url.replaceAll("/[^/?]+(\\?.*)?$", "/");
            try (Connection conn = DriverManager.getConnection(serverUrl, username, password);
                 Statement stmt = conn.createStatement()) {
                stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS `notes-todo`");
            }
        } catch (SQLException e) {
            throw new SQLException("Couldn't create the database", e);
        }
    }
}