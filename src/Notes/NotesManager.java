package Notes;

import java.util.List;
import java.util.ArrayList;
import Database.DBHelper;
import ToDo.ToDo;

import java.util.Iterator;

import java.sql.*;

public class NotesManager {
    private List<Notes> notesList;

    public NotesManager() {
        notesList = new ArrayList<>();
        refreshNotes();
    }

    public void addNote(String title, String content) {
        Notes newNote = new Notes(title, content);
        notesList.add(newNote);
        saveNoteToDatabase(newNote);
    }

    public void removeNote(Notes note) {
        notesList.remove(note);
        deleteNoteFromDatabase(note);
    }

    public List<Notes> getNotes() {
        return notesList;
    }

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

    public void saveNoteToDatabase(Notes note) {
        String sql = "INSERT INTO notes (title, content) VALUES (?, ?)";
        try (Connection conn = DBHelper.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, note.getTitle());
            stmt.setString(2, note.getContent());
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    note.setId(rs.getInt(1)); // SET THe generated ID to the note object
                }
            }
        } catch (SQLException e) {
            System.err.println("Error saving note: " + e.getMessage());
        }
    }

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

    public void updateNoteFromDatabase(Notes note) {
        String sql = "UPDATE notes SET title = ?, content = ? WHERE id = ?";
        try(Connection conn = DBHelper.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, note.getTitle());
            stmt.setString(2, note.getContent());
            stmt.setInt(3, note.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error Updating The Note: " + e.getMessage());
        }
    }

    public void clearNotes() {
        Iterator<Notes> iterator = notesList.iterator();
        while (iterator.hasNext()) {
            Notes note = iterator.next();
            deleteNoteFromDatabase(note);
            iterator.remove();
        }
    }
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Notes note : notesList) {
            sb.append(note.getTitle()).append(" (").append(note.getContent()).append(")\n");
        }
        return sb.toString();
    }

    public List<String> getNotesSummaries() {
        List<String> summaries = new ArrayList<>();
        for (Notes note : notesList) {
            summaries.add(note.getTitle() + " - Content: " + note.getContent());
        }
        return summaries;
    }
}