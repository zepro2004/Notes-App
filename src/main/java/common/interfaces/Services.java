package common.interfaces;

import java.util.List;

/**
 * Generic interface defining core service operations for managing entities.
 * Acts as an intermediary layer between the UI and database management,
 * providing high-level business operations and data access methods.
 *
 * <p>Features:</p>
 * <ul>
 * <li>Basic CRUD operations (Create, Read, Update, Delete)</li>
 * <li>Data refresh and clear capabilities</li>
 * <li>Sorting functionality</li>
 * <li>Summary data retrieval</li>
 * <li>Complete data access</li>
 * </ul>
 *
 * <p>This interface follows the service layer pattern in the application architecture,
 * encapsulating business logic and transactional behavior while abstracting the underlying
 * data access mechanisms. Implementations typically delegate persistence operations to
 * {@link DatabaseManagement} implementations while adding domain-specific processing.</p>
 *
 * @param <T> The type of entity being managed by the service, typically a domain model class
 *
 * @version 1.0
 * @since 2025-04-23
 * @author Louis Bertrand Ntwali
 *
 * @see DatabaseManagement
 */
public interface Services<T> {

    /**
     * Adds a new entity to the system.
     * Implementations should validate the entity before persisting it
     * and may apply business rules during the addition process.
     *
     * @param item The entity to be added, must not be null
     * @throws IllegalArgumentException if item is null or invalid
     */
    void add(T item);

    /**
     * Removes an existing entity from the system.
     * If the entity doesn't exist, implementations may throw an exception or silently ignore.
     *
     * @param item The entity to be deleted, must not be null
     * @throws IllegalArgumentException if item is null
     */
    void delete(T item);

    /**
     * Updates an existing entity in the system.
     * Implementations should validate the updated entity and may apply
     * business rules before persisting changes.
     *
     * @param item The entity with updated values, must not be null
     * @throws IllegalArgumentException if item is null or invalid
     * @throws IllegalStateException if the entity doesn't exist in the system
     */
    void update(T item);

    /**
     * Refreshes the service's data from the underlying data source.
     * This ensures that any changes made directly to the data source
     * are reflected in the service's working data.
     */
    void refresh();

    /**
     * Removes all entities managed by this service.
     * This operation is typically irreversible and should be used with caution.
     */
    void clear();

    /**
     * Sorts the managed entities according to specified criteria.
     * The actual sorting logic depends on the implementation and entity type.
     *
     * @param option The sorting criteria to be applied (e.g., field name, sort direction)
     * @throws IllegalArgumentException if option contains invalid sorting criteria
     */
    void sort(String option);

    /**
     * Retrieves a summary list of the managed entities.
     * This typically provides a condensed representation of each entity,
     * suitable for display in list components or summary reports.
     *
     * @return List of string representations summarizing each entity, may be empty but never null
     */
    List<String> getSummary();

    /**
     * Retrieves all entities managed by this service.
     * This provides complete access to all entity data in their current state.
     *
     * @return Complete list of all managed entities, may be empty but never null
     */
    List<T> getAll();
}