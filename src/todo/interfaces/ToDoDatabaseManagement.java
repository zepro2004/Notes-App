package todo.interfaces;

import todo.ToDo;
import common.interfaces.DatabaseManagement;
import java.util.List;

public interface ToDoDatabaseManagement extends DatabaseManagement<ToDo> {
    List<ToDo> findToDoByTitle(String taskDescription);
}
