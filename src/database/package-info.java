/**
 * Database infrastructure for application persistence.
 *
 * <p>This package provides database connectivity, initialization, and operations
 * for the application's SQLite database. It serves as the foundational data persistence
 * layer, encapsulating all direct interactions with the underlying database.</p>
 *
 * <p>Key components include:</p>
 * <ul>
 *   <li>{@link database.DBHelper} - Database connection provider that manages the
 *       connection lifecycle and provides parameterized query execution methods</li>
 *   <li>{@link database.DBInitializer} - Schema creation and initialization logic
 *       responsible for setting up tables and maintaining database structure</li>
 * </ul>
 *
 * <p>Architectural role:</p>
 * <ul>
 *   <li>This package represents the Data Access Layer in the application's architecture</li>
 *   <li>It provides a foundation for the domain-specific database managers
 *       ({@link notes.impl.NotesDatabaseManager}, {@link todo.impl.ToDoDatabaseManager})</li>
 *   <li>Centralizes database connection management to prevent connection leaks and resource issues</li>
 *   <li>Abstracts SQLite-specific details from the rest of the application</li>
 * </ul>
 *
 * <p>Implementation details:</p>
 * <ul>
 *   <li>Uses JDBC with SQLite driver for lightweight embedded database storage</li>
 *   <li>Database file is stored in the user's home directory for persistence</li>
 *   <li>Connection pooling is implemented to optimize resource usage</li>
 *   <li>Parameterized queries are used throughout to prevent SQL injection</li>
 *   <li>Transactions are managed to ensure data integrity across operations</li>
 * </ul>
 *
 * <p>Design principles:</p>
 * <ul>
 *   <li>Single Responsibility: Each class has a focused purpose (connections vs. schema)</li>
 *   <li>Encapsulation: Database implementation details are hidden from consuming code</li>
 *   <li>Low coupling: Database layer is independent of business logic and presentation</li>
 *   <li>Thread safety: Connection management is designed for concurrent access</li>
 * </ul>
 *
 * @version 1.0
 * @since 2025-04-23
 * @see java.sql.Connection
 * @see java.sql.PreparedStatement
 * @see notes.impl.NotesDatabaseManager
 * @see todo.impl.ToDoDatabaseManager
 */
package database;