package todo;

/**
 * Represents a single todo task item with an ID, description, end date, and completion status.
 * This class serves as the primary data model in the task management subsystem,
 * following the Domain Model pattern.
 *
 * <p>Features:</p>
 * <ul>
 * <li>Unique identification through ID management</li>
 * <li>Task description and end date storage</li>
 * <li>Completion status tracking</li>
 * <li>ID protection to ensure data integrity</li>
 * <li>Support for both new task creation and existing task representation</li>
 * </ul>
 *
 * <p>Implementation Details:</p>
 * <ul>
 * <li>Implements a simple immutable ID pattern once the ID is assigned</li>
 * <li>Stores dates as strings, with format interpretation delegated to service layer</li>
 * <li>No validation is performed on string fields within this class</li>
 * <li>Thread-safe for ID assignment but not for other property changes</li>
 * </ul>
 *
 * <p>This class works with ToDoService and ToDoDatabaseManagement to form
 * the task management subsystem. It represents the "Model" component in the
 * application's architecture.</p>
 *
 * @version 1.0
 * @since 2025-04-23
 * @author Louis Bertrand Ntwali
 *
 * @see todo.impl.ToDoService
 * @see todo.interfaces.ToDoDatabaseManagement
 * @see todo.impl.ToDoDatabaseManager
 */
public class ToDo {
    /**
     * The unique identifier for the todo task.
     *
     * <p>ID Lifecycle:</p>
     * <ul>
     * <li>Initialized to 0 for new, unsaved tasks</li>
     * <li>Assigned a positive integer value when persisted to the database</li>
     * <li>Cannot be changed once assigned a non-zero value</li>
     * </ul>
     *
     * <p>This property enforces entity identity within the application.</p>
     */
    private int id;

    /**
     * The description of the task detailing what needs to be done.
     *
     * <p>This field should typically not be null, though no validation
     * is performed in this class. Empty strings are technically allowed
     * but generally not meaningful.</p>
     *
     * <p>The description is used in UI displays and may be searched for
     * using the findToDoByDescription method in the data access layer.</p>
     */
    private String taskDescription;

    /**
     * The target completion date for the task, stored as a String.
     *
     * <p>Date format interpretation is delegated to the service layer,
     * but typically follows ISO 8601 format (yyyy-MM-dd) for consistency.
     * No validation is performed on this string in this class.</p>
     *
     * <p>This field should typically not be null, though no validation
     * is enforced here.</p>
     */
    private String endDate;

    /**
     * The completion status of the task.
     *
     * <p>{@code true} if the task is completed, {@code false} otherwise.
     * This boolean flag is used to track whether a task has been finished,
     * affecting how it's displayed in the UI and potentially filtered in lists.</p>
     */
    private boolean isCompleted;

    /**
     * Constructs a new {@code ToDo} instance, typically used for tasks that haven't been saved yet.
     * The ID is initialized to 0, and the completion status is set based on the provided parameter.
     *
     * <p>Implementation Details:</p>
     * <ul>
     * <li>Sets ID to 0 to indicate an unsaved task</li>
     * <li>Accepts the parameters without validation</li>
     * </ul>
     *
     * @param taskDescription The description of the task, should not be null but not validated here
     * @param endDate The target end date for the task (as a String), should not be null but not validated here
     * @param isCompleted The initial completion status of the task
     */
    public ToDo(String taskDescription, String endDate, boolean isCompleted) {
        this.id = 0;
        this.taskDescription = taskDescription;
        this.endDate = endDate;
        this.isCompleted = isCompleted;
    }

    /**
     * Constructs a {@code ToDo} instance with a specific ID, description, end date, and completion status.
     * Typically used when loading existing tasks from storage.
     *
     * <p>Implementation Details:</p>
     * <ul>
     * <li>Accepts an existing ID for database-sourced tasks</li>
     * <li>No validation is performed on the ID or other parameters</li>
     * <li>Primarily used by data access classes</li>
     * </ul>
     *
     * @param id The unique identifier of the task, should be positive
     * @param taskDescription The description of the task, should not be null but not validated here
     * @param endDate The target end date for the task (as a String), should not be null but not validated here
     * @param isCompleted The completion status of the task
     */
    public ToDo(int id, String taskDescription, String endDate, boolean isCompleted) {
        this.id = id;
        this.taskDescription = taskDescription;
        this.endDate = endDate;
        this.isCompleted = isCompleted;
    }

    /**
     * Returns the unique identifier of the task.
     *
     * @return The task's ID (0 for unsaved tasks, positive integer for saved tasks)
     */
    public int getId() {
        return id;
    }

    /**
     * Returns the description of the task.
     *
     * @return The task's description, may be empty but typically not null
     */
    public String getTaskDescription() {
        return taskDescription;
    }

    /**
     * Returns the end date of the task as a String.
     *
     * @return The task's end date string, may be empty but typically not null
     */
    public String getEndDate() {
        return endDate;
    }

    /**
     * Returns the completion status of the task.
     *
     * @return {@code true} if the task is marked as completed, {@code false} otherwise
     */
    public boolean isCompleted() {
        return isCompleted;
    }

    /**
     * Sets the unique identifier for the task.
     * This method enforces the one-time assignment rule for IDs to maintain data integrity.
     *
     * <p>Implementation Details:</p>
     * <ul>
     * <li>Enforces the one-time assignment rule for IDs</li>
     * <li>Prevents accidental overwriting of existing IDs</li>
     * <li>Typically called by the persistence layer after saving</li>
     * </ul>
     *
     * @param id The unique identifier to set, should be positive
     * @throws IllegalStateException if the task's ID has already been set to a non-zero value
     */
    public void setId(int id) {
        if (this.id == 0) {
            this.id = id;
        } else {
            throw new IllegalStateException("ID can only be set once.");
        }
    }

    /**
     * Updates the description of the task.
     *
     * <p>This method does not perform validation on the new description.
     * Any required validation should be performed before calling this method.</p>
     *
     * @param taskDescription The new description for the task, should not be null but not validated here
     */
    public void setTaskDescription(String taskDescription) {
        this.taskDescription = taskDescription;
    }

    /**
     * Updates the end date of the task.
     *
     * <p>This method does not perform validation on the new end date.
     * Any required date format validation should be performed before calling this method.</p>
     *
     * @param endDate The new end date string for the task, should not be null but not validated here
     */
    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    /**
     * Marks the task as completed by setting its completion status to {@code true}.
     *
     * <p>Implementation Details:</p>
     * <ul>
     * <li>Sets isCompleted to true, regardless of its previous value</li>
     * <li>This is a one-way operation; there is no direct method to mark a task as incomplete</li>
     * <li>To mark a task as incomplete, use setCompleted(false) from the service layer</li>
     * </ul>
     */
    public void markAsCompleted() {
        this.isCompleted = true;
    }
}