package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Database connection helper utility class that provides simplified access to database connections.
 * <p>
 * This class serves as a factory for database connections using configuration parameters
 * retrieved from the {@link DatabaseConfig} class. It follows the utility class pattern
 * with a private constructor to prevent instantiation.
 * <p>
 * Usage example:
 * <pre>
 * try (Connection conn = DBHelper.getConnection()) {
 *     // Use connection here
 * } catch (SQLException e) {
 *     // Handle exception
 * }
 * </pre>
 *
 * @see DatabaseConfig
 */
public class DBHelper {
    /**
     * Private constructor to prevent instantiation of this utility class.
     *
     * @throws IllegalStateException if an attempt is made to instantiate this class
     */
    private DBHelper() { throw new IllegalStateException("Utility class"); }

    /**
     * Establishes and returns a database connection using configuration from {@link DatabaseConfig}.
     * <p>
     * This method uses connection parameters (URL, username, password) from the DatabaseConfig
     * utility to create a new database connection.
     *
     * @return a Connection object representing the connection to the database
     * @throws SQLException if a database access error occurs or the URL is null
     * @see DatabaseConfig#getUrl()
     * @see DatabaseConfig#getUsername()
     * @see DatabaseConfig#getPassword()
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(
                DatabaseConfig.getUrl(),
                DatabaseConfig.getUsername(),
                DatabaseConfig.getPassword()
        );
    }
}