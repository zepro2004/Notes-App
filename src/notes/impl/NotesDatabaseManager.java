package notes.impl;

import notes.Notes;
import notes.interfaces.NotesDatabaseManagement;
import database.DBHelper;
import java.sql.*;
import java.util.List;
import java.util.ArrayList;

public class NotesDatabaseManager implements NotesDatabaseManagement {
    @Override
    public void save(Notes note) {
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

    @Override
    public void delete(Notes note) {
        String sql = "DELETE FROM notes WHERE id = ?";
        try (Connection conn = DBHelper.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, note.getId());
            stmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error Deleting The Note: " + e.getMessage());
        }
    }

    @Override
    public void update(Notes note) {
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

    @Override
    public List<Notes> findByTitle(String title) {
        List<Notes> notes = new ArrayList<>();
        String sql = "SELECT * FROM notes WHERE title LIKE ?";
        try (Connection conn = DBHelper.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, "%" + title + "%");
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Notes note = new Notes(rs.getString("title"), rs.getString("content"));
                    note.setId(rs.getInt("id"));
                    notes.add(note);
                }
            }
            return notes;
        } catch (SQLException e) {
            throw new RuntimeException("Error finding notes by title: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Notes> refresh() {
        List<Notes> notes = new ArrayList<>();
        String sql = "SELECT * FROM notes ORDER BY id DESC";
        try (Connection conn = DBHelper.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Notes note = new Notes(rs.getString("title"), rs.getString("content"));
                note.setId(rs.getInt("id"));
                notes.add(note);
            }
            return notes;
        } catch (SQLException e) {
            throw new RuntimeException("Error loading notes: " + e.getMessage(), e);
        }
    }

    @Override
    public void clear() {
        String sql = "DELETE FROM notes";
        try (Connection conn = DBHelper.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting all notes: " + e.getMessage(), e);
        }
    }
}
