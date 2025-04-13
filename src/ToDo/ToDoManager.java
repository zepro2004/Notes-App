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
    }

    public void addTask(String taskDescription, String endDate) {
        ToDo newTask = new ToDo(taskDescription, endDate);
        toDoList.add(newTask);
    }

    public void removeTask(ToDo task) {
        toDoList.remove(task);
    }

    public List<ToDo> getTasks() {
      loadTasksFromDatabase();
      return toDoList;
    }

    public void markTaskAsCompleted(ToDo task) {
        task.markAsCompleted();
    }

    public void loadTasksFromDatabase() {
        List<ToDo> tasks = new ArrayList<>();
        String sql = "SELECT * FROM todos";

        try (Connection conn = DBHelper.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String description = rs.getString("description");
                String endDate = rs.getString("end_date");
                ToDo task = new ToDo(description, endDate);
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
          summaries.add(task.getTaskDescription() + " - Due: " + task.getEndDate());
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
