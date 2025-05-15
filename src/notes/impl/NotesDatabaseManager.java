package notes.impl;

import notes.Notes;
import notes.interfaces.NotesDatabaseManagement;
import database.DBHelper;
import todo.ToDo;

import java.sql.*;
import java.util.List;
import java.util.ArrayList;

/**
 * Database access implementation for Notes entities.
 * This class provides the data access layer functionality for persisting and retrieving Notes objects,
 * implementing the NotesDatabaseManagement interface.
 *
 * <p>Features:</p>
 * <ul>
 * <li>CRUD operations for Notes entities (Create, Read, Update, Delete)</li>
 * <li>Title-based search functionality</li>
 * <li>Batch operations (clear all)</li>
 * <li>Custom sorting capabilities</li>
 * <li>Exception handling with appropriate error reporting</li>
 * </ul>
 *
 * <p>Implementation Details:</p>
 * <ul>
 * <li>Uses JDBC for database connectivity via DBHelper</li>
 * <li>Implements connection pooling with try-with-resources for automatic resource management</li>
 * <li>Uses PreparedStatements to prevent SQL injection attacks</li>
 * <li>Maps between Java objects and database records</li>
 * </ul>
 *
 * <p>This class serves as the persistence layer in the application's architecture,
 * directly interacting with the database while isolating higher layers from SQL specifics.
 * It follows the Data Access Object (DAO) pattern to provide a clean separation between
 * business logic and data persistence.</p>
 *
 * @version 1.0
 * @since 2025-04-23
 * @author Louis Bertrand Ntwali
 *
 * @see NotesDatabaseManagement
 * @see Notes
 * @see database.DBHelper
 */
public class NotesDatabaseManager implements NotesDatabaseManagement {

    /**
     * Persists a new note to the database.
     * Executes an SQL INSERT statement and updates the note's ID with the generated key.
     *
     * <p>Implementation Details:</p>
     * <ul>
     * <li>Uses a prepared statement with parameter binding to prevent SQL injection</li>
     * <li>Retrieves and sets the database-generated ID on the note object</li>
     * <li>Handles SQLExceptions internally and logs error messages</li>
     * </ul>
     *
     * @param note The note to save, must not be null and should have title and content set
     * @throws IllegalArgumentException if note is null (implied, not explicitly thrown)
     */
    @Override
    public void save(Notes note) {
        String sql = "INSERT INTO notes (title, content) VALUES (?, ?)";
        try (Connection conn = DBHelper.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, note.getTitle());
            stmt.setString(2, note.getContent());
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    note.setId(rs.getInt(1));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error saving note: " + e.getMessage());
        }
    }

    /**
     * Deletes an existing note from the database.
     * Executes an SQL DELETE statement based on the note's ID.
     *
     * <p>Implementation Details:</p>
     * <ul>
     * <li>Uses a prepared statement with parameter binding to prevent SQL injection</li>
     * <li>Silently ignores if the note doesn't exist in the database</li>
     * <li>Handles SQLExceptions internally and logs error messages</li>
     * </ul>
     *
     * @param note The note to delete, must not be null and must have a valid ID
     * @throws IllegalArgumentException if note is null or has invalid ID (implied, not explicitly thrown)
     */
    @Override
    public void delete(Notes note) {
        String sql = "DELETE FROM notes WHERE id = ?";
        try (Connection conn = DBHelper.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, note.getId());
            stmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error Deleting The Note: " + e.getMessage());
        }
    }

    /**
     * Updates an existing note in the database.
     * Executes an SQL UPDATE statement to modify the title and content of a note.
     *
     * <p>Implementation Details:</p>
     * <ul>
     * <li>Uses a prepared statement with parameter binding to prevent SQL injection</li>
     * <li>Updates based on the note's ID field</li>
     * <li>Silently ignores if the note doesn't exist in the database</li>
     * <li>Handles SQLExceptions internally and logs error messages</li>
     * </ul>
     *
     * @param note The note to update, must not be null, must have valid ID, title and content
     * @throws IllegalArgumentException if note is null or has invalid ID (implied, not explicitly thrown)
     */
    @Override
    public void update(Notes note) {
        String sql = "UPDATE notes SET title = ?, content = ? WHERE id = ?";
        try (Connection conn = DBHelper.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, note.getTitle());
            stmt.setString(2, note.getContent());
            stmt.setInt(3, note.getId());
            stmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error Updating The Note: " + e.getMessage());
        }
    }

    /**
     * Searches for notes with titles containing the specified text.
     * Executes an SQL SELECT statement with a LIKE clause for partial matching.
     *
     * <p>Implementation Details:</p>
     * <ul>
     * <li>Uses a prepared statement with parameter binding to prevent SQL injection</li>
     * <li>Performs case-sensitive search using SQL LIKE with wildcards</li>
     * <li>Creates and populates Notes objects from the ResultSet</li>
     * </ul>
     *
     * @param title The text to search for within note titles, may be partial
     * @return List of Notes with matching titles, empty list if none found, never null
     * @throws RuntimeException if a database error occurs during the search
     */
    @Override
    public List<Notes> findByTitle(String title) {
        List<Notes> notes = new ArrayList<>();
        String sql = "SELECT * FROM notes WHERE title LIKE ?";
        try (Connection conn = DBHelper.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, "%" + title + "%");
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Notes note = new Notes(rs.getString("title"), rs.getString("content"));
                    note.setId(rs.getInt("id"));
                    notes.add(note);
                }
            }
            return notes;
        } catch (SQLException e) {
            throw new RuntimeException("Error finding notes by title: " + e.getMessage(), e);
        }
    }

    /**
     * Retrieves all notes from the database in reverse chronological order (newest first).
     * Executes an SQL SELECT statement ordering by ID in descending order.
     *
     * <p>Implementation Details:</p>
     * <ul>
     * <li>Uses a prepared statement for consistent query execution</li>
     * <li>Orders results by ID descending, assuming IDs increase chronologically</li>
     * <li>Creates and populates Notes objects from the ResultSet</li>
     * </ul>
     *
     * @return List of all Notes in the database, empty list if none exist, never null
     * @throws RuntimeException if a database error occurs during retrieval
     */
    @Override
    public List<Notes> refresh() {
        List<Notes> notes = new ArrayList<>();
        String sql = "SELECT * FROM notes ORDER BY id DESC";
        try (Connection conn = DBHelper.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Notes note = new Notes(rs.getString("title"), rs.getString("content"));
                note.setId(rs.getInt("id"));
                notes.add(note);
            }
            return notes;
        } catch (SQLException e) {
            throw new RuntimeException("Error loading notes: " + e.getMessage(), e);
        }
    }

    /**
     * Deletes all notes from the database.
     * Executes an SQL DELETE statement without a WHERE clause to remove all records.
     *
     * <p>Implementation Details:</p>
     * <ul>
     * <li>Uses a prepared statement for consistent query execution</li>
     * <li>This is a permanent operation that cannot be undone</li>
     * </ul>
     *
     * @throws RuntimeException if a database error occurs during deletion
     */
    @Override
    public void clear() {
        String sql = "DELETE FROM notes";
        try (Connection conn = DBHelper.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting all notes: " + e.getMessage(), e);
        }
    }

    /**
     * Retrieves all notes sorted alphabetically by title.
     * Delegates to {@link #sortedGet(String)} with appropriate SQL.
     *
     * <p>Implementation Note: There appears to be a SQL syntax issue in the implementation
     * as it references the 'todos' table instead of 'notes' table.</p>
     *
     * @return List of Notes sorted by title, empty list if none exist, never null
     * @throws RuntimeException if a database error occurs during retrieval
     */
    public List<Notes> getSortedByTitle() {
        return sortedGet("SELECT * FROM todos ORDER BY title");
    }

    /**
     * Helper method for retrieving notes based on a custom SQL query.
     * Provides a flexible way to fetch sorted or filtered notes.
     *
     * <p>Implementation Details:</p>
     * <ul>
     * <li>Uses a prepared statement to execute the provided SQL</li>
     * <li>Creates and populates Notes objects from the ResultSet</li>
     * <li>Expects the SQL to select from a table with id, title, and content columns</li>
     * </ul>
     *
     * @param sql The complete SQL query to execute, must not be null
     * @return List of Notes based on query results, empty list if none found, never null
     * @throws RuntimeException if a database error occurs during execution
     * @throws IllegalArgumentException if sql is null or invalid (implied, not explicitly thrown)
     */
    public List<Notes> sortedGet(String sql) {
        List<Notes> notes = new ArrayList<>();
        try(Connection conn = DBHelper.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery()) {

            while(rs.next()) {
                int id = rs.getInt("id");
                String title = rs.getString("title");
                String content = rs.getString("content");
                Notes note = new Notes(title, content);
                note.setId(id);
                notes.add(note);
            }
            return notes;
        }  catch (SQLException e) {
            throw new RuntimeException("Error getting sorted notes: " + e.getMessage(), e);
        }
    }
}