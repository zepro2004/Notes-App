package todo.impl;

import todo.ToDo;
import todo.interfaces.ToDoDatabaseManagement;
import database.DBHelper;
import java.sql.*;
import java.util.List;
import java.util.ArrayList;

/**
 * Database access implementation for ToDo entities.
 * This class provides the data access layer functionality for persisting and retrieving ToDo objects,
 * implementing the ToDoDatabaseManagement interface.
 *
 * <p>Features:</p>
 * <ul>
 * <li>CRUD operations for ToDo entities (Create, Read, Update, Delete)</li>
 * <li>Description-based search functionality</li>
 * <li>Batch operations (clear all)</li>
 * <li>Custom sorting capabilities (by description, by date)</li>
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
 * @see ToDoDatabaseManagement
 * @see ToDo
 * @see database.DBHelper
 */
public class ToDoDatabaseManager implements ToDoDatabaseManagement {
    /**
     * Constructs a new ToDoDatabaseManager instance.
     *
     * <p>Creates a database manager that can perform CRUD operations
     * and other database management functions for ToDo entities. The manager
     * uses the {@link database.DBHelper} to establish database connections as needed.</p>
     *
     * <p>No initialization is required as connections are obtained on demand
     * for each database operation.</p>
     */
    public ToDoDatabaseManager() {
        // Default constructor
    }

    /**
     * Persists a new ToDo item to the database.
     * Executes an SQL INSERT statement and updates the ToDo's ID with the generated key.
     *
     * <p>Implementation Details:</p>
     * <ul>
     * <li>Uses a prepared statement with parameter binding to prevent SQL injection</li>
     * <li>Retrieves and sets the database-generated ID on the ToDo object</li>
     * <li>Handles SQLExceptions by wrapping them in RuntimeException</li>
     * </ul>
     *
     * @param toDo The ToDo item to save, must not be null and should have description and end date set
     * @throws RuntimeException if a database error occurs during the save operation
     * @throws IllegalArgumentException if toDo is null (implied, not explicitly thrown)
     */
    @Override
    public void save(ToDo toDo) {
        String sql = "INSERT INTO todos (description, end_date, completed) VALUES (?, ?, ?)";
        try (Connection conn = DBHelper.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, toDo.getTaskDescription());
            stmt.setString(2, toDo.getEndDate());
            stmt.setBoolean(3, toDo.isCompleted());
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if(rs.next()) {
                    toDo.setId(rs.getInt(1)); // Set the generated ID on the task object
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error saving the todo: " + e.getMessage(), e);
        }
    }

    /**
     * Deletes an existing ToDo item from the database.
     * Executes an SQL DELETE statement based on the ToDo's ID.
     *
     * <p>Implementation Details:</p>
     * <ul>
     * <li>Uses a prepared statement with parameter binding to prevent SQL injection</li>
     * <li>Silently ignores if the ToDo item doesn't exist in the database</li>
     * <li>Handles SQLExceptions by wrapping them in RuntimeException</li>
     * </ul>
     *
     * @param toDo The ToDo item to delete, must not be null and must have a valid ID
     * @throws RuntimeException if a database error occurs during the delete operation
     * @throws IllegalArgumentException if toDo is null or has invalid ID (implied, not explicitly thrown)
     */
    @Override
    public void delete(ToDo toDo) {
        String sql = "DELETE FROM todos WHERE id = ?";
        try (Connection conn = DBHelper.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, toDo.getId());
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error deleting the todo: " + e.getMessage(), e);
        }
    }

    /**
     * Updates an existing ToDo item in the database.
     * Executes an SQL UPDATE statement to modify the description, end date, and completion status.
     *
     * <p>Implementation Details:</p>
     * <ul>
     * <li>Uses a prepared statement with parameter binding to prevent SQL injection</li>
     * <li>Updates based on the ToDo's ID field</li>
     * <li>Silently ignores if the ToDo item doesn't exist in the database</li>
     * <li>Handles SQLExceptions by wrapping them in RuntimeException</li>
     * </ul>
     *
     * @param toDo The ToDo item to update, must not be null, must have valid ID, description, end date and completion status
     * @throws RuntimeException if a database error occurs during the update operation
     * @throws IllegalArgumentException if toDo is null or has invalid ID (implied, not explicitly thrown)
     */
    @Override
    public void update(ToDo toDo) {
        String sql = "UPDATE todos SET description = ?, end_date = ?, completed = ? WHERE id = ?";
        try (Connection conn = DBHelper.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, toDo.getTaskDescription());
            stmt.setString(2, toDo.getEndDate());
            stmt.setBoolean(3, toDo.isCompleted());
            stmt.setInt(4, toDo.getId());
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error updating the todo: " + e.getMessage(), e);
        }
    }

    /**
     * Searches for ToDo items with descriptions containing the specified text.
     * Executes an SQL SELECT statement with a LIKE clause for partial matching.
     *
     * <p>Implementation Details:</p>
     * <ul>
     * <li>Uses a prepared statement with parameter binding to prevent SQL injection</li>
     * <li>Performs case-sensitive search using SQL LIKE with wildcards</li>
     * <li>Creates and populates ToDo objects from the ResultSet</li>
     * </ul>
     *
     * @param taskDescription The text to search for within ToDo descriptions, may be partial
     * @return List of ToDo items with matching descriptions, empty list if none found, never null
     * @throws RuntimeException if a database error occurs during the search operation
     */
    @Override
    public List<ToDo> findToDoByDescription(String taskDescription) {
        List<ToDo> toDos = new ArrayList<>();
        String sql = "SELECT * FROM todos WHERE description LIKE ?";
        try (Connection conn = DBHelper.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, "%" + taskDescription + "%");
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    ToDo toDo = new ToDo(rs.getString("description"), rs.getString("end_date"), rs.getBoolean("isCompleted"));
                    toDo.setId(rs.getInt("id"));
                    toDos.add(toDo);
                }
            }
            return toDos;
        }   catch (SQLException e) {
            throw new RuntimeException("Error finding todos by title: " + e.getMessage(), e);
        }
    }

    /**
     * Retrieves all ToDo items from the database.
     * Executes an SQL SELECT statement to fetch all records from the todos table.
     *
     * <p>Implementation Details:</p>
     * <ul>
     * <li>Uses a prepared statement for consistent query execution</li>
     * <li>Creates and populates ToDo objects from the ResultSet</li>
     * <li>Sets all properties including ID, description, end date and completion status</li>
     * </ul>
     *
     * @return List of all ToDo items in the database, empty list if none exist, never null
     * @throws RuntimeException if a database error occurs during retrieval
     */
    @Override
    public List<ToDo> refresh() {
        List<ToDo> toDos = new ArrayList<>();
        String sql = "SELECT * FROM todos";
        try (Connection conn = DBHelper.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String description = rs.getString("description");
                String endDate = rs.getString("end_date");
                boolean completed = rs.getBoolean("completed");
                ToDo task = new ToDo(description, endDate, completed);
                task.setId(id); // Set the actual ID
                toDos.add(task);
            }
            return toDos;
        } catch (SQLException e) {
            throw new RuntimeException("Error loading todos: " + e.getMessage(), e);
        }
    }

    /**
     * Deletes all ToDo items from the database.
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
        String sql = "DELETE FROM todos";
        try(Connection conn = DBHelper.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql))
        {
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting all todos: " + e.getMessage(), e);
        }
    }

    /**
     * Retrieves all ToDo items sorted alphabetically by description.
     * Delegates to {@link #sortedGet(String)} with appropriate SQL.
     *
     * <p>Implementation Details:</p>
     * <ul>
     * <li>Uses SQL ORDER BY clause for efficient server-side sorting</li>
     * <li>Sorting is based on the description field's lexicographical order</li>
     * </ul>
     *
     * @return List of ToDo items sorted by description, empty list if none exist, never null
     * @throws RuntimeException if a database error occurs during retrieval
     */
    public List<ToDo> getSortedByDescription() {
        return sortedGet("SELECT * FROM todos ORDER BY description");
    }

    /**
     * Retrieves all ToDo items sorted by end date.
     * Delegates to {@link #sortedGet(String)} with appropriate SQL.
     *
     * <p>Implementation Details:</p>
     * <ul>
     * <li>Uses SQL ORDER BY clause for efficient server-side sorting</li>
     * <li>Sorting is based on the end_date field's chronological order</li>
     * <li>Date ordering depends on the date format stored in the database</li>
     * </ul>
     *
     * @return List of ToDo items sorted by end date, empty list if none exist, never null
     * @throws RuntimeException if a database error occurs during retrieval
     */
    public List<ToDo> getSortedByDate() {
        return sortedGet("SELECT * FROM todos ORDER BY end_date");
    }

    /**
     * Helper method for retrieving ToDo items based on a custom SQL query.
     * Provides a flexible way to fetch sorted or filtered ToDo items.
     *
     * <p>Implementation Details:</p>
     * <ul>
     * <li>Uses a prepared statement to execute the provided SQL</li>
     * <li>Creates and populates ToDo objects from the ResultSet</li>
     * <li>Expects the SQL to select from a table with id, description, end_date, and completed columns</li>
     * </ul>
     *
     * @param sql The complete SQL query to execute, must not be null
     * @return List of ToDo items based on query results, empty list if none found, never null
     * @throws RuntimeException if a database error occurs during execution
     * @throws IllegalArgumentException if sql is null or invalid (implied, not explicitly thrown)
     */
    public List<ToDo> sortedGet(String sql) {
        List<ToDo> todos = new ArrayList<>();
        try(Connection conn = DBHelper.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery()) {

            while(rs.next()) {
                int id = rs.getInt("id");
                String description = rs.getString("description");
                String endDate = rs.getString("end_date");
                boolean completed = rs.getBoolean("completed");
                ToDo task = new ToDo(description, endDate, completed);
                task.setId(id);
                todos.add(task);
            }
            return todos;
        }  catch (SQLException e) {
            throw new RuntimeException("Error getting sorted todos: " + e.getMessage(), e);
        }
    }
}