package common.interfaces;

import java.util.List;

public interface DatabaseManagement<T>{
    void save(T item);
    void delete(T item);
    void update(T item);
    void clear();
    List<T> refresh();
}
