package common.interfaces;

import java.util.List;

public interface Services<T> {
    void add(T item);
    void delete(T item);
    void update(T item);
    void refresh();
    void clear();
    void sort(String option);
    List<String> getSummary();
    List <T> getAll();
}
