package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DBInitializer {
    private DBInitializer() { throw new IllegalStateException("Utility class"); }

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