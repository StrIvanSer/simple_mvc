package org.example.repo;

import org.example.web.dto.Book;

import java.util.List;

public interface ProjectRepository<T> {
    List<T> retrieveAll();
//    List<T> retrieveAllByFilter(String filter);

    void store(T book);

    boolean removeItemByField(T book);

    boolean removeItemById(Integer bookIdRemove);

    void addDefaultBook();
}
