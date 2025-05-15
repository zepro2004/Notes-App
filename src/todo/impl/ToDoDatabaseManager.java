package todo.impl;

import todo.ToDo;
import todo.interfaces.ToDoDatabaseManagement;
import database.DBHelper;
import java.sql.*;
import java.util.List;
import java.util.ArrayList;

public class ToDoDatabaseManager implements ToDoDatabaseManagement {
    @Override
    public void save(ToDo toDo) {
        String sql = "INSERT INTO todos (description, end_date, completed) VALUES (?, ?, ?)";
        try (Connection conn = DBHelper.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, toDo.getTaskDescription());
            stmt.setString(2, toDo.getEndDate());
            stmt.setBoolean(3, toDo.isCompleted());
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if(rs.next()) {
                    toDo.setId(rs.getInt(1)); // Set the generated ID on the task object
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error saving the todo: " + e.getMessage(), e);
        }
    }

    @Override
    public void delete(ToDo toDo) {
        String sql = "DELETE FROM todos WHERE id = ?";
        try (Connection conn = DBHelper.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, toDo.getId());
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error deleting the todo: " + e.getMessage(), e);
        }
    }

    @Override
    public void update(ToDo toDo) {
        String sql = "UPDATE todos SET description = ?, end_date = ?, completed = ? WHERE id = ?";
        try (Connection conn = DBHelper.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, toDo.getTaskDescription());
            stmt.setString(2, toDo.getEndDate());
            stmt.setBoolean(3, toDo.isCompleted());
            stmt.setInt(4, toDo.getId());
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error updating the todo: " + e.getMessage(), e);
        }
    }

    @Override
    public List<ToDo> findToDoByTitle(String taskDescription) {
        List<ToDo> toDos = new ArrayList<>();
        String sql = "SELECT * FROM todos WHERE title LIKE ?";
        try (Connection conn = DBHelper.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, "%" + taskDescription + "%");
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    ToDo toDo = new ToDo(rs.getString("description"), rs.getString("end_date"), rs.getBoolean("isCompleted"));
                    toDo.setId(rs.getInt("id"));
                    toDos.add(toDo);
                }
            }
            return toDos;
        }   catch (SQLException e) {
            throw new RuntimeException("Error finding todos by title: " + e.getMessage(), e);
        }
    }

    @Override
    public List<ToDo> refresh() {
        List<ToDo> toDos = new ArrayList<>();
        String sql = "SELECT * FROM todos";
        try (Connection conn = DBHelper.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String description = rs.getString("description");
                String endDate = rs.getString("end_date");
                boolean completed = rs.getBoolean("completed");
                ToDo task = new ToDo(description, endDate, completed);
                task.setId(id); // Set the actual ID
                toDos.add(task);
            }
            return toDos;
        } catch (SQLException e) {
            throw new RuntimeException("Error loading todos: " + e.getMessage(), e);
        }
    }

    @Override
    public void clear() {
        String sql = "DELETE FROM todos";
        try(Connection conn = DBHelper.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql))
        {
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting all todos: " + e.getMessage(), e);
        }
    }

    public List<ToDo> getSortedByDescription() {
        return sortedGet("SELECT * FROM todos ORDER BY description");
    }

    public List<ToDo> getSortedByDate() {
        return sortedGet("SELECT * FROM todos ORDER BY end_date");
    }

    public List<ToDo> sortedGet(String sql) {
        List<ToDo> todos = new ArrayList<>();
        try(Connection conn = DBHelper.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery()) {

            while(rs.next()) {
                int id = rs.getInt("id");
                String description = rs.getString("description");
                String endDate = rs.getString("end_date");
                boolean completed = rs.getBoolean("completed");
                ToDo task = new ToDo(description, endDate, completed);
                task.setId(id);
                todos.add(task);
            }
            return todos;
        }  catch (SQLException e) {
            throw new RuntimeException("Error getting sorted todos: " + e.getMessage(), e);
        }
    }

}
