package todo.impl;

import todo.ToDo;
import todo.interfaces.ToDoDatabaseManagement;
import common.interfaces.Services;
import java.util.List;
import java.util.ArrayList;

public class ToDoService implements Services<ToDo> {
    private final ToDoDatabaseManagement repository;
    private List<ToDo> toDoList;

    public ToDoService(ToDoDatabaseManagement repository) {
        this.repository = repository;
        this.toDoList = new ArrayList<>();
        refresh();
    }

    @Override
    public List<ToDo> getAll() {
        return toDoList;
    }

    @Override
    public void add(ToDo toDo) {
        repository.save(toDo);
        toDoList.add(toDo);
    }

    @Override
    public void delete(ToDo toDo) {
        repository.delete(toDo);
        toDoList.remove(toDo);
    }

    @Override
    public void refresh() {
        toDoList = repository.refresh();
    }

    @Override
    public void update(ToDo toDo) {
        repository.update(toDo);
        refresh();
    }

    @Override
    public void clear() {
        repository.clear();
        toDoList.clear();
    }

     public void markTaskAsCompleted(ToDo task) {
        task.markAsCompleted();
        repository.update(task);
        refresh();
    }

    public List<String> getSummary() {
        List<String> summaries = new ArrayList<>();
        for (ToDo task : toDoList) {
            summaries.add(task.getTaskDescription() + " - Date: " + task.getEndDate() + " - " + (task.isCompleted() ? "(Completed)" : "(Not yet completed)"));
        }
        return summaries;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (ToDo task : toDoList) {
            sb.append(task.getTaskDescription()).append(" (").append(task.getEndDate()).append(")\n");
        }
        return sb.toString();
    }

    public void sort() {
        this.toDoList = repository.sortedGet();
    }

}
