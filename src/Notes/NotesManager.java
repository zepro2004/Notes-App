package Notes;

import java.util.List;
import java.util.ArrayList;
import Database.DBHelper;
import ToDo.ToDo;
import java.util.Iterator;
import java.sql.*;

/**
 * Manages a collection of {@link Notes} objects.
 * This class handles the creation, deletion, updating, and retrieval of notes,
 * interacting with a database via {@link Database.DBHelper}. It maintains an in-memory list
 * of notes which is initially loaded and can be refreshed from the database.
 *
 * @version 1.0
 * @since 2025-04-23
 * @author Louis Bertrand Ntwali
 *
 * @see Notes
 * @see Database.DBHelper
 */
public class NotesManager {
    /**
     * The in-memory list holding the {@link Notes} objects.
     */
    private List<Notes> notesList;

    /**
     * Constructs a new {@code NotesManager}.
     * Initializes the internal list of notes and performs an initial load
     * of notes from the database by calling {@link #refreshNotes()}.
     */
    public NotesManager() {
        notesList = new ArrayList<>();
        refreshNotes();
    }

    /**
     * Creates a new note with the given title and content, adds it to the
     * internal list, and attempts to save it to the database using
     * {@link #saveNoteToDatabase(Notes)}.
     *
     * @param title   The title for the new note.
     * @param content The content for the new note.
     */
    public void addNote(String title, String content) {
        Notes newNote = new Notes(title, content);
        notesList.add(newNote);
        saveNoteToDatabase(newNote);
    }

    /**
     * Removes the specified note from the internal list and attempts to delete
     * it from the database using {@link #deleteNoteFromDatabase(Notes)}.
     *
     * @param note The {@link Notes} object to remove.
     */
    public void removeNote(Notes note) {
        notesList.remove(note);
        deleteNoteFromDatabase(note);
    }

    /**
     * Returns the current in-memory list of notes.
     * Direct modification of the returned list will not affect the database
     * unless changes are persisted through manager methods like {@code updateNoteInDatabase}.
     *
     * @return The {@link List} containing the current {@link Notes} objects.
     */
    public List<Notes> getNotes() {
        return notesList;
    }

    /**
     * Reloads the notes from the database, replacing the current in-memory list.
     * Queries the 'notes' table using `SELECT *`, orders by ID descending,
     * creates {@link Notes} objects using the (title, content) constructor, sets their IDs using `setId`,
     * and populates the internal {@code notesList}. Errors during database access are printed to standard error.
     */
    public void refreshNotes() {
        List<Notes> notes = new ArrayList<>();
        String sql = "SELECT * FROM notes ORDER BY id DESC";
        try (Connection conn = DBHelper.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String title = rs.getString("title");
                String content = rs.getString("content");
                Notes note = new Notes (title, content);
                note.setId(id);
                notes.add(note);
            }
            notesList = notes;

        } catch (SQLException e) {
            System.err.println("Error loading notes: " + e.getMessage());
        }
    }

    /**
     * Saves a new note to the 'notes' table in the database.
     * Executes an INSERT statement and attempts to retrieve the generated ID,
     * which is then set on the provided {@link Notes} object using {@link Notes#setId(int)}.
     * Errors during database access or ID retrieval are printed to standard error.
     *
     * @param note The {@link Notes} object to save. Its ID should be 0 initially and will be updated.
     */
    public void saveNoteToDatabase(Notes note) {
        String sql = "INSERT INTO notes (title, content) VALUES (?, ?)";
        try (Connection conn = DBHelper.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, note.getTitle());
            stmt.setString(2, note.getContent());
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    note.setId(rs.getInt(1));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error saving note: " + e.getMessage());
        }
    }

    /**
     * Deletes the specified note from the 'notes' table in the database using its ID.
     * Errors during database access are printed to standard error.
     *
     * @param note The {@link Notes} object to delete. The deletion is based on {@code note.getId()}.
     */
    public void deleteNoteFromDatabase(Notes note) {
        String sql = "DELETE FROM notes WHERE id = ?";
        try (Connection conn = DBHelper.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, note.getId());
            stmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error Deleting The Note: " + e.getMessage());
        }
    }

    /**
     * Updates the title and content of an existing note in the 'notes' table, identified by its ID.
     * Errors during database access are printed to standard error.
     *
     * @param note The {@link Notes} object containing the updated title and content, and the ID of the note to update.
     */
    public void updateNoteInDatabase(Notes note) {
        String sql = "UPDATE notes SET title = ?, content = ? WHERE id = ?";
        try (Connection conn = DBHelper.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, note.getTitle());
            stmt.setString(2, note.getContent());
            stmt.setInt(3, note.getId());
            stmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error Updating The Note: " + e.getMessage());
        }
    }

    /**
     * Removes all notes from the in-memory list and attempts to delete them
     * from the database one by one using an iterator.
     * This method iterates through the list, calling {@link #deleteNoteFromDatabase(Notes)}
     * for each note before removing it from the list using the iterator's remove method.
     */
    public void clearNotes() {
        Iterator<Notes> iterator = notesList.iterator();
        while (iterator.hasNext()) {
            Notes note = iterator.next();
            deleteNoteFromDatabase(note);
            iterator.remove();
        }
    }

    /**
     * Returns a string representation of all notes in the manager.
     * Each note is represented by its title and content (within parentheses) on a new line.
     *
     * @return A multi-line string summarizing all notes, or an empty string if no notes are present.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Notes note : notesList) {
            sb.append(note.getTitle()).append(" (").append(note.getContent()).append(")\n");
        }
        return sb.toString();
    }

    /**
     * Generates a list of simple string summaries for each note.
     * Each summary consists of the note's title, followed by " - Content: ", and then the note's content.
     *
     * @return A {@link List} of strings, where each string is a summary of a note.
     */
    public List<String> getNotesSummaries() {
        List<String> summaries = new ArrayList<>();
        for (Notes note : notesList) {
            summaries.add(note.getTitle() + " - Content: " + note.getContent());
        }
        return summaries;
    }
}