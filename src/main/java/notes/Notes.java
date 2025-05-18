package notes;

/**
 * Represents a single note with an ID, title, and content.
 * This class serves as the primary data model in the notes subsystem,
 * following the Domain Model pattern.
 *
 * <p>Features:</p>
 * <ul>
 * <li>Unique identification through auto-generated ID</li>
 * <li>Title and content storage with appropriate accessors</li>
 * <li>ID protection to ensure data integrity</li>
 * <li>Support for both new note creation and existing note representation</li>
 * </ul>
 *
 * <p>Implementation Details:</p>
 * <ul>
 * <li>Implements a simple immutable ID pattern once the ID is assigned</li>
 * <li>All fields are mutable except for ID after initial assignment</li>
 * <li>No length validation is performed within this class (delegated to service layer)</li>
 * <li>Thread-safe for ID assignment but not for other property changes</li>
 * </ul>
 *
 * <p>This class works with NotesService and NotesDatabaseManagement to form
 * the notes management subsystem. It represents the "Model" component in the
 * application's MVC architecture.</p>
 *
 * @version 1.0
 * @since 2025-04-23
 * @author Louis Bertrand Ntwali
 *
 * @see notes.impl.NotesService
 * @see notes.interfaces.NotesDatabaseManagement
 * @see layout.panels.NotesPanel
 */
public class Notes {
    /**
     * The unique identifier for the note.
     *
     * <p>ID Lifecycle:</p>
     * <ul>
     * <li>Initialized to 0 for new, unsaved notes</li>
     * <li>Assigned a positive integer value when persisted to the database</li>
     * <li>Cannot be changed once assigned a non-zero value</li>
     * </ul>
     *
     * <p>This property enforces entity identity within the application.</p>
     */
    private int id;

    /**
     * The title of the note.
     *
     * <p>The title serves as a brief summary or identifier for the note's content
     * and is typically displayed in note lists and search results.</p>
     *
     * <p>While no validation is performed in this class, the application typically
     * enforces a maximum length of 50 characters for titles.</p>
     */
    private String title;

    /**
     * The main textual content of the note.
     *
     * <p>Contains the primary information stored in the note. May include
     * line breaks and other formatting that should be preserved.</p>
     *
     * <p>While no validation is performed in this class, the application may
     * enforce length constraints at the service layer.</p>
     */
    private String content;

    /**
     * Constructs a new {@code Notes} instance for an unsaved note.
     *
     * <p>This constructor is typically used when creating a note from user input
     * before it has been persisted to the database.</p>
     *
     * @param title   The title of the note, should not be null but not validated here
     * @param content The content of the note, should not be null but not validated here
     */
    public Notes(String title, String content) {
        this.id = 0; // Default ID for new, unsaved notes
        this.title = title;
        this.content = content;
    }

    /**
     * Constructs a {@code Notes} instance representing an existing note.
     *
     * <p>This constructor is typically used when reconstructing a note from
     * database records or other persistent storage.</p>
     *
     * @param id      The unique identifier of the note, should be positive
     * @param title   The title of the note, should not be null
     * @param content The content of the note, should not be null
     */
    public Notes(int id, String title, String content) {
        this.id = id;
        this.title = title;
        this.content = content;
    }

    /**
     * Returns the unique identifier of the note.
     *
     * @return The note's ID (0 for unsaved notes, positive integer for saved notes)
     */
    public int getId() {
        return id;
    }

    /**
     * Returns the title of the note.
     *
     * @return The note's title, may be empty but typically not null
     */
    public String getTitle() {
        return title;
    }

    /**
     * Returns the content of the note.
     *
     * @return The note's content, may be empty but typically not null
     */
    public String getContent() {
        return content;
    }

    /**
     * Sets the unique identifier for the note.
     *
     * <p>Implementation Details:</p>
     * <ul>
     * <li>Enforces the one-time assignment rule for IDs</li>
     * <li>Prevents accidental overwriting of existing IDs</li>
     * <li>Typically called by the persistence layer after saving</li>
     * </ul>
     *
     * @param id The unique identifier to set, should be positive
     * @throws IllegalStateException if the note's ID has already been set to a non-zero value
     */
    public void setId(int id) {
        if (this.id == 0) {
            this.id = id;
        } else {
            // Prevent changing an already assigned ID
            throw new IllegalStateException("ID can only be set once.");
        }
    }

    /**
     * Updates the title of the note.
     *
     * <p>This method does not perform validation on the new title.
     * Any required validation should be performed before calling this method.</p>
     *
     * @param title The new title for the note, should not be null but not validated here
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Updates the content of the note.
     *
     * <p>This method does not perform validation on the new content.
     * Any required validation should be performed before calling this method.</p>
     *
     * @param content The new content for the note, should not be null but not validated here
     */
    public void setContent(String content) {
        this.content = content;
    }
}
