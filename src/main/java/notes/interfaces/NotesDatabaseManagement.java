package notes.interfaces;

import notes.Notes;
import common.interfaces.DatabaseManagement;

import java.util.List;

/**
 * Data access interface for Notes entities.
 * This interface defines specialized data operations for Notes objects beyond the
 * standard CRUD operations inherited from the DatabaseManagement interface.
 *
 * <p>Features:</p>
 * <ul>
 * <li>Title-based search functionality</li>
 * <li>Notes sorting by title</li>
 * <li>Extends generic DatabaseManagement interface for standard operations</li>
 * </ul>
 *
 * <p>Architecture Context:</p>
 * <ul>
 * <li>Represents the Data Access Object (DAO) contract in the application's architecture</li>
 * <li>Implementations provide persistence mechanisms for Notes entities</li>
 * <li>Used by the NotesService to abstract database operations</li>
 * <li>Provides a clean separation between business logic and data persistence</li>
 * </ul>
 *
 * <p>This interface extends the generic DatabaseManagement interface with Notes-specific
 * operations. It enables searching for notes by title and retrieving notes in various
 * sorted arrangements, while inheriting standard CRUD functionality.</p>
 *
 * @version 1.0
 * @since 2025-04-23
 * @author Louis Bertrand Ntwali
 *
 * @see DatabaseManagement
 * @see Notes
 * @see notes.impl.NotesDatabaseManager
 * @see notes.impl.NotesService
 */
public interface NotesDatabaseManagement extends DatabaseManagement<Notes> {

    /**
     * Searches for notes with titles containing the specified text.
     * Implementations should perform a case-sensitive partial match search
     * on the title field of Notes entities.
     *
     * <p>Implementation Details:</p>
     * <ul>
     * <li>Should use SQL LIKE with wildcards or equivalent mechanism</li>
     * <li>Should handle empty search strings according to implementation needs</li>
     * <li>Should create fully populated Notes objects including IDs</li>
     * </ul>
     *
     * @param title The text to search for within note titles, may be partial
     * @return List of Notes with matching titles, empty list if none found, never null
     * @throws RuntimeException if a database error occurs during the search
     */
    List<Notes> findByTitle(String title);

    /**
     * Retrieves all notes sorted alphabetically by title.
     * Provides notes in lexicographical order based on their title field.
     *
     * <p>Implementation Details:</p>
     * <ul>
     * <li>Should use database-level sorting when possible for performance</li>
     * <li>Should handle case sensitivity according to database collation rules</li>
     * <li>Should create fully populated Notes objects including IDs</li>
     * </ul>
     *
     * @return List of Notes sorted by title, empty list if none exist, never null
     * @throws RuntimeException if a database error occurs during retrieval
     */
    List<Notes> getSortedByTitle();
}