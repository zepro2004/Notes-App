package ToDo;

import Database.DBHelper;

import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import java.sql.SQLException;

public class ToDoManager {
    private List<ToDo> toDoList;

    public ToDoManager() {
        toDoList = new ArrayList<>();
        refreshTasks();
    }

    public void addTask(String taskDescription, String endDate, boolean isCompleted) {
        ToDo newTask = new ToDo(taskDescription, endDate, isCompleted);
        toDoList.add(newTask);
        saveTaskToDatabase(newTask);
    }

    public void removeTask(ToDo task) {
        toDoList.remove(task);
        deleteTaskFromDatabase(task);
    }

    public void markTaskAsCompleted(ToDo task) {
        task.markAsCompleted();
        updateTaskInDatabase(task);
    }

    public List<ToDo> getTasks() {
        return toDoList;
    }

    public void saveTaskToDatabase(ToDo task) {
        String sql = "INSERT INTO todos (description, end_date, completed) VALUES (?, ?, ?)";
        try (Connection conn = DBHelper.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, task.getTaskDescription());
            stmt.setString(2, task.getEndDate());
            stmt.setBoolean(3, task.isCompleted());
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error saving task: " + e.getMessage());
        }
    }

    public void updateTaskInDatabase(ToDo task) {
        String sql = "UPDATE todos SET completed = ? WHERE description = ? AND end_date = ?";
        try (Connection conn = DBHelper.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setBoolean(1, task.isCompleted());
            stmt.setString(2, task.getTaskDescription());
            stmt.setString(3, task.getEndDate());
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error updating task: " + e.getMessage());
        }
    }

    public void deleteTaskFromDatabase(ToDo task) {
        String sql = "DELETE FROM todos WHERE description = ? AND end_date = ?";
        try (Connection conn = DBHelper.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, task.getTaskDescription());
            stmt.setString(2, task.getEndDate());
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error deleting task: " + e.getMessage());
        }
    }

    public void refreshTasks() {
        List<ToDo> tasks = new ArrayList<>();
        String sql = "SELECT * FROM todos";

        try (Connection conn = DBHelper.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String description = rs.getString("description");
                String endDate = rs.getString("end_date");
                boolean completed = rs.getBoolean("completed");
                ToDo task = new ToDo(description, endDate, completed);
                tasks.add(task);
            }
            toDoList = tasks;
        } catch (SQLException e) {
            System.err.println("Error loading tasks: " + e.getMessage());
        }
    }

    public List<String> getTaskSummaries() {
      List<String> summaries = new ArrayList<>();
      for (ToDo task : toDoList) {
          summaries.add(task.getTaskDescription() + " - Date: " + task.getEndDate() + " - " + (task.isCompleted() ? " (Completed)" : "(Not yet completed)"));
      }
      return summaries;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (ToDo task : toDoList) {
            sb.append(task.getTaskDescription()).append(" (").append(task.getEndDate()).append(")\n");
        }
        return sb.toString();
    }
  
}
