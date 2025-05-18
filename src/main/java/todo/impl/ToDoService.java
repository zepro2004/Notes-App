package todo.impl;

import todo.ToDo;
import todo.interfaces.ToDoDatabaseManagement;
import common.interfaces.Services;
import java.util.List;
import java.util.ArrayList;

/**
 * Service layer implementation for managing ToDo entities.
 * This class acts as an intermediary between the UI layer and the data access layer,
 * implementing the Services interface to provide standardized operations for ToDo items.
 *
 * <p>Features:</p>
 * <ul>
 * <li>In-memory caching of ToDo items for improved performance</li>
 * <li>CRUD operations delegated to the repository layer</li>
 * <li>Task completion status management</li>
 * <li>ToDo summarization functionality for list displays</li>
 * <li>Custom sorting capabilities (by description, by date)</li>
 * <li>String representation for debugging and logging</li>
 * </ul>
 *
 * <p>Implementation Details:</p>
 * <ul>
 * <li>Uses an injected repository (ToDoDatabaseManagement) for persistent storage operations</li>
 * <li>Maintains a cached list of ToDo items that is synchronized with the database</li>
 * <li>Follows the Service Layer pattern in a multi-tier architecture</li>
 * <li>Not explicitly thread-safe - concurrent modifications may cause inconsistencies</li>
 * </ul>
 *
 * <p>This class serves as part of the service layer in the application's architecture,
 * mediating between the presentation layer (UI components) and the data access layer.
 * It maintains a clean separation of concerns by isolating business logic from both UI
 * and data persistence details.</p>
 *
 * @version 1.0
 * @since 2025-04-23
 * @author Louis Bertrand Ntwali
 *
 * @see Services
 * @see ToDo
 * @see ToDoDatabaseManagement
 * @see layout.panels.ToDoPanel
 */
public class ToDoService implements Services<ToDo> {
    /**
     * The data access object for ToDo persistence.
     * Handles all direct database interactions, allowing this service to focus
     * on business logic and caching.
     */
    private final ToDoDatabaseManagement repository;

    /**
     * In-memory cache of ToDo items.
     * Provides faster access to ToDo objects without requiring database queries
     * for every operation. Must be kept in sync with database state.
     */
    private List<ToDo> toDoList;

    /**
     * Constructs a new ToDoService with the specified repository.
     * Initializes the ToDo cache and populates it with all ToDo items from the repository.
     *
     * <p>Implementation Details:</p>
     * <ul>
     * <li>Stores the repository reference for later use</li>
     * <li>Creates an empty ArrayList to hold cached ToDo items</li>
     * <li>Calls refresh() to populate the cache from the database</li>
     * </ul>
     *
     * @param repository The data access object for ToDo persistence, must not be null
     * @throws IllegalArgumentException if repository is null (implied, not explicitly thrown)
     */
    public ToDoService(ToDoDatabaseManagement repository) {
        this.repository = repository;
        this.toDoList = new ArrayList<>();
        refresh();
    }

    /**
     * Retrieves all ToDo items from the in-memory cache.
     * Returns a direct reference to the internal list, not a defensive copy.
     *
     * <p>Implementation Note: Callers should not modify the returned list directly
     * as this could cause inconsistencies between the cache and the database.</p>
     *
     * @return List of all cached ToDo items, may be empty but never null
     */
    @Override
    public List<ToDo> getAll() {
        return toDoList;
    }

    /**
     * Adds a new ToDo item to both the database and in-memory cache.
     * Delegates to the repository for persistence and then updates the cache.
     *
     * <p>Implementation Details:</p>
     * <ul>
     * <li>Persists the ToDo item to the database via the repository</li>
     * <li>Adds the ToDo item to the in-memory cache</li>
     * <li>The ToDo ID is set by the repository during save operation</li>
     * </ul>
     *
     * @param toDo The ToDo item to add, must not be null
     * @throws IllegalArgumentException if toDo is null (implied, not explicitly thrown)
     */
    @Override
    public void add(ToDo toDo) {
        repository.save(toDo);
        toDoList.add(toDo);
    }

    /**
     * Deletes a ToDo item from both the database and in-memory cache.
     * Delegates to the repository for persistence and then updates the cache.
     *
     * <p>Implementation Details:</p>
     * <ul>
     * <li>Removes the ToDo item from the database via the repository</li>
     * <li>Removes the ToDo item from the in-memory cache</li>
     * </ul>
     *
     * @param toDo The ToDo item to delete, must not be null and must exist in the database
     * @throws IllegalArgumentException if toDo is null (implied, not explicitly thrown)
     */
    @Override
    public void delete(ToDo toDo) {
        repository.delete(toDo);
        toDoList.remove(toDo);
    }

    /**
     * Refreshes the in-memory cache with the current state from the database.
     * This operation discards the current cache and replaces it with fresh data.
     *
     * <p>Implementation Details:</p>
     * <ul>
     * <li>Requests a fresh list of ToDo items from the repository</li>
     * <li>Replaces the entire in-memory cache with this new list</li>
     * </ul>
     *
     * <p>This method should be called when there might be external changes
     * to the database that need to be reflected in the application.</p>
     */
    @Override
    public void refresh() {
        toDoList = repository.refresh();
    }

    /**
     * Updates an existing ToDo item in both the database and refreshes the in-memory cache.
     * Delegates to the repository for persistence and refreshes the entire cache.
     *
     * <p>Implementation Details:</p>
     * <ul>
     * <li>Updates the ToDo item in the database via the repository</li>
     * <li>Refreshes the entire in-memory cache to ensure consistency</li>
     * </ul>
     *
     * @param toDo The ToDo item to update, must not be null and must exist in the database
     * @throws IllegalArgumentException if toDo is null (implied, not explicitly thrown)
     */
    @Override
    public void update(ToDo toDo) {
        repository.update(toDo);
        refresh();
    }

    /**
     * Removes all ToDo items from both the database and in-memory cache.
     * This is a permanent operation that cannot be undone.
     *
     * <p>Implementation Details:</p>
     * <ul>
     * <li>Deletes all ToDo items from the database via the repository</li>
     * <li>Clears the in-memory cache</li>
     * </ul>
     */
    @Override
    public void clear() {
        repository.clear();
        toDoList.clear();
    }

    /**
     * Marks a ToDo item as completed and updates it in the database.
     * This is a convenience method that combines status change and persistence.
     *
     * <p>Implementation Details:</p>
     * <ul>
     * <li>Updates the ToDo's completion status via its markAsCompleted() method</li>
     * <li>Persists the updated ToDo to the database</li>
     * <li>Refreshes the cache to ensure consistency</li>
     * </ul>
     *
     * @param task The ToDo item to mark as completed, must not be null
     * @throws IllegalArgumentException if task is null (implied, not explicitly thrown)
     */
    public void markTaskAsCompleted(ToDo task) {
        task.markAsCompleted();
        repository.update(task);
        refresh();
    }

    /**
     * Creates a list of ToDo summaries for display purposes.
     * Each summary includes the task description, end date, and completion status.
     *
     * <p>Implementation Details:</p>
     * <ul>
     * <li>Creates a new list to hold the summaries</li>
     * <li>Iterates through all cached ToDo items, creating formatted summary strings</li>
     * <li>Format: "[Description] - Date: [EndDate] - ([Completion Status])"</li>
     * </ul>
     *
     * <p>This method is typically used by UI components that need to display
     * a simple list of ToDo items with key information.</p>
     *
     * @return List of formatted ToDo summary strings in the same order as the cache, never null
     */
    public List<String> getSummary() {
        List<String> summaries = new ArrayList<>();
        for (ToDo task : toDoList) {
            summaries.add(task.getTaskDescription() + " - Date: " + task.getEndDate() + " - " + (task.isCompleted() ? "(Completed)" : "(Not yet completed)"));
        }
        return summaries;
    }

    /**
     * Creates a string representation of all ToDo items in the cache.
     * Formats each ToDo as "description (endDate)" with items separated by newlines.
     *
     * <p>Implementation Details:</p>
     * <ul>
     * <li>Uses StringBuilder for efficient string concatenation</li>
     * <li>Formats each ToDo as "description (endDate)"</li>
     * <li>Separates each ToDo entry with a newline character</li>
     * </ul>
     *
     * <p>This method is primarily used for debugging and logging purposes.</p>
     *
     * @return A formatted string containing all ToDo items, never null
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (ToDo task : toDoList) {
            sb.append(task.getTaskDescription()).append(" (").append(task.getEndDate()).append(")\n");
        }
        return sb.toString();
    }

    /**
     * Sorts the ToDo list based on the specified sort option.
     * Currently supports sorting by description and by date, with fallback to default ordering.
     *
     * <p>Implementation Details:</p>
     * <ul>
     * <li>If options is null, refreshes from database in default order</li>
     * <li>For "Description" option, requests description-sorted list from repository</li>
     * <li>For "Date" option, requests date-sorted list from repository</li>
     * <li>For any other option, refreshes from database in default order</li>
     * </ul>
     *
     * @param options The sort option to apply ("Description", "Date", or null for default order)
     */
    public void sort(String options) {
        if(options == null) {
            toDoList = repository.refresh();
            return;
        }
        switch(options) {
            case "Description" -> toDoList = repository.getSortedByDescription();
            case "Date" -> toDoList = repository.getSortedByDate();
            default -> toDoList = repository.refresh();
        }
    }
}