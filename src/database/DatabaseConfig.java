package database;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

/**
 * Database configuration utility class that provides access to database connection parameters.
 * <p>
 * This class loads database configuration from a properties file and provides static methods
 * to access connection details such as URL, username, and password. The configuration is
 * loaded only once when the class is initialized.
 * <p>
 * The properties file must be located at "src/database/db.properties" and contain the following keys:
 * <ul>
 *     <li>jdbc.url - The JDBC connection URL</li>
 *     <li>jdbc.username - Database username</li>
 *     <li>jdbc.password - Database password</li>
 * </ul>
 */
public class DatabaseConfig {
    /**
     * Properties object that stores the database configuration values.
     * Loaded once during class initialization from the db.properties file.
     */
    private static final Properties props = new Properties();

    /**
     * Static initializer that loads the database properties when the class is loaded.
     * Throws ExceptionInInitializerError if the properties file cannot be loaded,
     * which will prevent the class from being used.
     */
    static {
        try (InputStream in = Files.newInputStream(Paths.get("src/database/db.properties"))) {
            props.load(in);
        } catch (IOException e) {
            throw new ExceptionInInitializerError("Failed to load database properties: " + e.getMessage());
        }
    }

    /**
     * Retrieves the database connection URL from the properties file.
     *
     * @return the JDBC URL string for database connection
     */
    public static String getUrl() {
        return props.getProperty("jdbc.url");
    }

    /**
     * Retrieves the database username from the properties file.
     *
     * @return the username for database authentication
     */
    public static String getUsername() {
        return props.getProperty("jdbc.username");
    }

    /**
     * Retrieves the database password from the properties file.
     *
     * @return the password for database authentication
     */
    public static String getPassword() {
        return props.getProperty("jdbc.password");
    }
}