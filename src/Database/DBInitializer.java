package Database;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DBInitializer {
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
                end_date TEXT
            );
        """;
        try (Connection conn = DBHelper.getConnection(); Statement stmt = conn.createStatement()) {
            stmt.execute(createNotesTable);
            stmt.execute(createTasksTable);
            System.out.println("Tables created successfully.");
        } catch (SQLException e) {
            System.err.println("Error creating tables: " + e.getMessage());
        }
  }
}
