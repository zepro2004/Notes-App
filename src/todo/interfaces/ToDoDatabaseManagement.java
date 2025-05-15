package todo.interfaces;

import todo.ToDo;
import common.interfaces.DatabaseManagement;
import java.util.List;

/**
 * Data access interface for ToDo entities.
 * This interface defines specialized data operations for ToDo objects beyond the
 * standard CRUD operations inherited from the DatabaseManagement interface.
 *
 * <p>Features:</p>
 * <ul>
 * <li>Description-based search functionality</li>
 * <li>ToDo sorting by description</li>
 * <li>ToDo sorting by due date</li>
 * <li>Extends generic DatabaseManagement interface for standard operations</li>
 * </ul>
 *
 * <p>Architecture Context:</p>
 * <ul>
 * <li>Represents the Data Access Object (DAO) contract in the application's architecture</li>
 * <li>Implementations provide persistence mechanisms for ToDo entities</li>
 * <li>Used by the ToDoService to abstract database operations</li>
 * <li>Provides a clean separation between business logic and data persistence</li>
 * </ul>
 *
 * <p>This interface extends the generic DatabaseManagement interface with ToDo-specific
 * operations. It enables searching for tasks by description and retrieving tasks in various
 * sorted arrangements, while inheriting standard CRUD functionality.</p>
 *
 * @version 1.0
 * @since 2025-04-23
 * @author Louis Bertrand Ntwali
 *
 * @see DatabaseManagement
 * @see ToDo
 * @see todo.impl.ToDoDatabaseManager
 * @see todo.impl.ToDoService
 */
public interface ToDoDatabaseManagement extends DatabaseManagement<ToDo> {

    /**
     * Searches for ToDo items with descriptions containing the specified text.
     * Implementations should perform a case-sensitive partial match search
     * on the description field of ToDo entities.
     *
     * <p>Implementation Details:</p>
     * <ul>
     * <li>Should use SQL LIKE with wildcards or equivalent mechanism</li>
     * <li>Should handle empty search strings according to implementation needs</li>
     * <li>Should create fully populated ToDo objects including IDs</li>
     * </ul>
     *
     * @param taskDescription The text to search for within task descriptions, may be partial
     * @return List of ToDo items with matching descriptions, empty list if none found, never null
     * @throws RuntimeException if a database error occurs during the search
     */
    List<ToDo> findToDoByDescription(String taskDescription);

    /**
     * Retrieves all ToDo items sorted alphabetically by description.
     * Provides tasks in lexicographical order based on their description field.
     *
     * <p>Implementation Details:</p>
     * <ul>
     * <li>Should use database-level sorting when possible for performance</li>
     * <li>Should handle case sensitivity according to database collation rules</li>
     * <li>Should create fully populated ToDo objects including IDs</li>
     * </ul>
     *
     * @return List of ToDo items sorted by description, empty list if none exist, never null
     * @throws RuntimeException if a database error occurs during retrieval
     */
    List<ToDo> getSortedByDescription();

    /**
     * Retrieves all ToDo items sorted by end date.
     * Provides tasks in chronological order based on their due date.
     *
     * <p>Implementation Details:</p>
     * <ul>
     * <li>Should use database-level sorting when possible for performance</li>
     * <li>Should handle date format parsing according to the stored format</li>
     * <li>Should create fully populated ToDo objects including IDs</li>
     * </ul>
     *
     * @return List of ToDo items sorted by end date, empty list if none exist, never null
     * @throws RuntimeException if a database error occurs during retrieval
     */
    List<ToDo> getSortedByDate();
}