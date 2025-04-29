package common.interfaces;

import java.util.List;

public interface Services<T> {
    void add(T item);
    void delete(T item);
    void update(T item);
    void refresh();
    void clear();
    List<String> getSummary();
    List <T> getAll();
}
