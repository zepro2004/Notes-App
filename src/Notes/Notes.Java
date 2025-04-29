package notes;

/**
 * Represents a single note with an ID, title, and content.
 * This class serves as a data model for notes within the application.
 * It provides constructors for creating new notes (without an initial ID)
 * and for representing existing notes retrieved from storage (with an ID).
 *
 *
 * @version 1.0
 * @since 2025-04-23
 * @author Louis Bertrand Ntwali
 *
 */
public class Notes {
    /**
     * The unique identifier for the note. Typically assigned when the note is saved.
     * A value of 0 may indicate a note that has not yet been persisted.
     */
    private int id;
    /**
     * The title of the note.
     */
    private String title;
    /**
     * The main textual content of the note.
     */
    private String content;

    /**
     * Constructs a new {@code notes} instance, typically used for notes that haven't been saved yet.
     * The ID is initialized to 0.
     *
     * @param title   The title of the note.
     * @param content The content of the note.
     */
    public Notes(String title, String content) {
        this.id = 0; // Default ID for new, unsaved notes
        this.title = title;
        this.content = content;
    }

    /**
     * Constructs a {@code notes} instance with a specific ID, title, and content.
     * Typically used when loading existing notes from storage.
     *
     * @param id      The unique identifier of the note.
     * @param title   The title of the note.
     * @param content The content of the note.
     */
    public Notes(int id, String title, String content) {
        this.id = id;
        this.title = title;
        this.content = content;
    }

    /**
     * Returns the unique identifier of the note.
     *
     * @return The note's ID.
     */
    public int getId() {
        return id;
    }

    /**
     * Returns the title of the note.
     *
     * @return The note's title.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Returns the content of the note.
     *
     * @return The note's content.
     */
    public String getContent() {
        return content;
    }

    /**
     * Sets the unique identifier for the note.
     * This method is intended to be used only once, typically after the note
     * has been saved for the first time and assigned an ID by the persistence layer.
     * It prevents the ID from being changed once set (if the initial ID was 0).
     *
     * @param id The unique identifier to set.
     * @throws IllegalStateException if the note's ID has already been set (i.e., is not 0).
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
     * @param title The new title for the note.
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Updates the content of the note.
     *
     * @param content The new content for the note.
     */
    public void setContent(String content) {
        this.content = content;
    }

}