package common.interfaces;

import java.util.List;

/**
 * Generic interface defining core database operations for managing entities.
 * Provides a consistent contract for implementing classes to handle basic
 * CRUD operations and data retrieval in a database context.
 *
 * <p>Features:</p>
 * <ul>
 * <li>Basic CRUD operations (Create, Read, Update, Delete)</li>
 * <li>Bulk operations (clear all)</li>
 * <li>Data refresh capabilities</li>
 * <li>Sorted data retrieval</li>
 * </ul>
 *
 * <p>This interface serves as the data access layer within the application architecture,
 * typically implemented by concrete database handlers that connect to specific storage systems.</p>
 *
 * @param <T> The type of entity being managed in the database, typically a domain model class
 *
 * @version 1.0
 * @since 2025-04-23
 * @author Louis Bertrand Ntwali
 * @see common.interfaces.Services
 */
public interface DatabaseManagement<T> {

    /**
     * Saves a new entity to the database.
     * If an entity with identical key properties already exists,
     * implementations may throw an exception or override the existing entity.
     *
     * @param item The entity to be saved, must not be null
     * @throws IllegalArgumentException if item is null or invalid
     */
    void save(T item);

    /**
     * Removes an existing entity from the database.
     * If the entity doesn't exist, implementations typically take no action.
     *
     * @param item The entity to be deleted, must not be null
     * @throws IllegalArgumentException if item is null
     */
    void delete(T item);

    /**
     * Updates an existing entity in the database.
     * If the entity doesn't exist, implementations may throw an exception
     * or create a new entity depending on implementation.
     *
     * @param item The entity with updated values, must not be null
     * @throws IllegalArgumentException if item is null
     */
    void update(T item);

    /**
     * Removes all entities from the database.
     * This operation is typically irreversible.
     */
    void clear();

    /**
     * Retrieves a fresh copy of all entities from the database.
     * This method ensures the returned data reflects the current state of the database.
     *
     * @return List of all entities in their current state, may be empty but never null
     */
    List<T> refresh();

    /**
     * Retrieves all entities sorted according to specified criteria.
     * The exact sorting behavior depends on the implementation.
     *
     * @param options The sorting criteria to be applied (e.g., field name, "asc"/"desc")
     * @return Sorted list of entities, may be empty but never null
     * @throws IllegalArgumentException if options contains invalid sorting criteria
     */
    List<T> sortedGet(String options);
}