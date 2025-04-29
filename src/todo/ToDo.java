package todo;

/**
 * Represents a single todo task item with an ID, description, end date, and completion status.
 * This class serves as the data model for individual tasks managed by the application.
 * It provides constructors for creating new tasks (without an initial ID)
 * and for representing existing tasks retrieved from storage (with an ID).
 *
 * @version 1.0
 * @since 2025-04-23
 * @author Louis Bertrand Ntwali
 *
 */
public class ToDo {
    /**
     * The unique identifier for the todo task. Assigned when the task is saved.
     * A value of 0 typically indicates a task that has not yet been persisted.
     */
    private int id;
    /**
     * The description of the task detailing what needs to be done.
     */
    private String taskDescription;
    /**
     * The target completion date for the task, stored as a String.
     * The expected format (e.g., "yyyy-MM-dd") should be handled by interacting classes.
     */
    private String endDate;
    /**
     * The completion status of the task. {@code true} if the task is completed, {@code false} otherwise.
     */
    private boolean isCompleted;

    /**
     * Constructs a new {@code todo} instance, typically used for tasks that haven't been saved yet.
     * The ID is initialized to 0, and the completion status is set based on the provided parameter.
     *
     * @param taskDescription The description of the task.
     * @param endDate         The target end date for the task (as a String).
     * @param isCompleted     The initial completion status of the task.
     */
    public ToDo(String taskDescription, String endDate, boolean isCompleted) {
        this.id = 0;
        this.taskDescription = taskDescription;
        this.endDate = endDate;
        this.isCompleted = isCompleted;
    }

    /**
     * Constructs a {@code todo} instance with a specific ID, description, end date, and completion status.
     * Typically used when loading existing tasks from storage.
     *
     * @param id              The unique identifier of the task.
     * @param taskDescription The description of the task.
     * @param endDate         The target end date for the task (as a String).
     * @param isCompleted     The completion status of the task.
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
     * @return The task's ID.
     */
    public int getId() {
        return id;
    }

    /**
     * Returns the description of the task.
     *
     * @return The task's description.
     */
    public String getTaskDescription() {
        return taskDescription;
    }

    /**
     * Returns the end date of the task as a String.
     *
     * @return The task's end date string.
     */
    public String getEndDate() {
        return endDate;
    }

    /**
     * Returns the completion status of the task.
     *
     * @return {@code true} if the task is marked as completed, {@code false} otherwise.
     */
    public boolean isCompleted() {
        return isCompleted;
    }

    /**
     * Sets the unique identifier for the task.
     * This method is intended to be used only once, typically after the task
     * has been saved for the first time and assigned an ID by the persistence layer.
     * It prevents the ID from being changed once set (if the initial ID was 0).
     *
     * @param id The unique identifier to set.
     * @throws IllegalStateException if the task's ID has already been set (i.e., is not 0).
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
     * @param taskDescription The new description for the task.
     */
    public void setTaskDescription(String taskDescription) {
        this.taskDescription = taskDescription;
    }

    /**
     * Updates the end date of the task.
     *
     * @param endDate The new end date string for the task.
     */
    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    /**
     * Marks the task as completed by setting its completion status to {@code true}.
     */
    public void markAsCompleted() {
        this.isCompleted = true;
    }
}
