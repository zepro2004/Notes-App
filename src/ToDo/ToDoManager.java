package ToDo;

import Database.DBHelper;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Manages a collection of {@link ToDo} task items.
 * This class handles the creation, deletion, updating (including marking as complete),
 * and retrieval of tasks, persisting changes to a database via {@link Database.DBHelper}.
 * It maintains an in-memory list of tasks which can be refreshed from the database.
 *
 * @see ToDo
 * @see Database.DBHelper
 */
public class ToDoManager {
    /**
     * The in-memory list holding the {@link ToDo} objects currently managed.
     */
    private List<ToDo> toDoList;

    /**
     * Constructs a new {@code ToDoManager}.
     * Initializes the internal list of tasks and performs an initial load
     * of tasks from the database by calling {@link #refreshTasks()}.
     */
    public ToDoManager() {
        toDoList = new ArrayList<>();
        refreshTasks(); // Load tasks from database on initialization
    }

    /**
     * Creates a new task with the given details, adds it to the internal list,
     * and attempts to save it to the database using {@link #saveTaskToDatabase(ToDo)}.
     * The task's ID is set after successful insertion into the database.
     *
     * @param taskDescription The description of the new task.
     * @param endDate         The target end date string for the new task.
     * @param isCompleted     The initial completion status of the new task.
     */
    public void addTask(String taskDescription, String endDate, boolean isCompleted) {
        ToDo newTask = new ToDo(taskDescription, endDate, isCompleted);
        toDoList.add(newTask);
        saveTaskToDatabase(newTask); // Persist the new task and update its ID
    }

    /**
     * Removes the specified task from the internal list and attempts to delete
     * it from the database using {@link #deleteTaskFromDatabase(ToDo)}.
     *
     * @param task The {@link ToDo} object to remove.
     */
    public void removeTask(ToDo task) {
        toDoList.remove(task);
        deleteTaskFromDatabase(task); // Persist the deletion
    }

    /**
     * Marks the specified task as completed in memory using {@link ToDo#markAsCompleted()}
     * and then attempts to update the task's status in the database using
     * {@link #updateTaskInDatabase(ToDo)}.
     *
     * @param task The {@link ToDo} object to mark as completed.
     */
    public void markTaskAsCompleted(ToDo task) {
        task.markAsCompleted(); // Mark as complete in the object
        updateTaskInDatabase(task); // Persist the change to the database
    }

    /**
     * Returns the current in-memory list of tasks.
     * Direct modification of the returned list will not affect the database
     * unless changes are persisted through manager methods.
     *
     * @return The {@link List} containing the current {@link ToDo} objects.
     */
    public List<ToDo> getTasks() {
        return toDoList;
    }

    /**
     * Saves a new task to the 'todos' table in the database.
     * Executes an INSERT statement and attempts to retrieve the generated ID,
     * which is then set on the provided {@link ToDo} object using {@link ToDo#setId(int)}.
     * Errors during database access or ID retrieval are printed to standard error.
     *
     * @param task The {@link ToDo} object to save. Its ID should be 0 initially and will be updated.
     */
    public void saveTaskToDatabase(ToDo task) {
        String sql = "INSERT INTO todos (description, end_date, completed) VALUES (?, ?, ?)";
        try (Connection conn = DBHelper.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, task.getTaskDescription());
            stmt.setString(2, task.getEndDate());
            stmt.setBoolean(3, task.isCompleted());
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if(rs.next()) {
                    task.setId(rs.getInt(1)); // Set the generated ID on the task object
                }
            }
        } catch (SQLException e) {
            System.err.println("Error saving task: " + e.getMessage());
        }
    }

    /**
     * Updates the description, end date, and completion status of an existing task
     * in the 'todos' table, identified by its ID.
     * Errors during database access are printed to standard error.
     *
     * @param task The {@link ToDo} object containing the updated details and the ID of the task to update.
     */
    public void updateTaskInDatabase(ToDo task) {
        String sql = "UPDATE todos SET description = ?, end_date = ?, completed = ? WHERE id = ?";
        try (Connection conn = DBHelper.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, task.getTaskDescription());
            stmt.setString(2, task.getEndDate());
            stmt.setBoolean(3, task.isCompleted());
            stmt.setInt(4, task.getId());
            stmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error updating task: " + e.getMessage());
        }
    }

    /**
     * Deletes the specified task from the 'todos' table in the database using its ID.
     * Errors during database access are printed to standard error.
     *
     * @param task The {@link ToDo} object to delete. The deletion is based on {@code task.getId()}.
     */
    public void deleteTaskFromDatabase(ToDo task) {
        String sql = "DELETE FROM todos WHERE id = ?";
        try (Connection conn = DBHelper.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, task.getId());
            stmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error deleting task: " + e.getMessage());
        }
    }

    /**
     * Reloads the tasks from the database, replacing the current in-memory list.
     * Queries the 'todos' table using `SELECT *`, creates {@link ToDo} objects
     * using the appropriate constructor, sets their IDs, and populates the internal {@code toDoList}.
     * Errors during database access are printed to standard error. Note: The original query
     * does not specify an order, so task order may depend on the database.
     */
    public void refreshTasks() {
        List<ToDo> tasks = new ArrayList<>();
        String sql = "SELECT * FROM todos"; // No ORDER BY clause in original code

        try (Connection conn = DBHelper.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql); // Semicolon removed, was likely typo
             ResultSet rs = stmt.executeQuery()) { // Corrected: executeQuery() on PreparedStatement

            while (rs.next()) {
                int id = rs.getInt("id");
                String description = rs.getString("description");
                String endDate = rs.getString("end_date");
                boolean completed = rs.getBoolean("completed");
                ToDo task = new ToDo(description, endDate, completed); // Create task with ID 0
                task.setId(id); // Set the actual ID
                tasks.add(task);
            }
            toDoList = tasks; // Replace the old list

        } catch (SQLException e) {
            System.err.println("Error loading tasks: " + e.getMessage());
        }
    }

    /**
     * Generates a list of string summaries for each task, suitable for display.
     * Each summary includes the task description, end date, and completion status.
     *
     * @return A {@link List} of strings, where each string is a summary of a task.
     */
    public List<String> getTaskSummaries() {
        List<String> summaries = new ArrayList<>();
        for (ToDo task : toDoList) {
            summaries.add(task.getTaskDescription() + " - Date: " + task.getEndDate() + " - " + (task.isCompleted() ? "(Completed)" : "(Not yet completed)"));
        }
        return summaries;
    }

    /**
     * Returns a string representation of all tasks in the manager.
     * Each task is represented by its description and end date (within parentheses) on a new line.
     *
     * @return A multi-line string summarizing all tasks, or an empty string if no tasks are present.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (ToDo task : toDoList) {
            sb.append(task.getTaskDescription()).append(" (").append(task.getEndDate()).append(")\n");
        }
        return sb.toString();
    }

}
