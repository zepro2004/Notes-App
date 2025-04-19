package Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBHelper {
  private static final String DB_URL = "jdbc:sqlite:resources/notes_todo.db";

  public static Connection getConnection() throws SQLException {
      return DriverManager.getConnection(DB_URL);
  }
}
