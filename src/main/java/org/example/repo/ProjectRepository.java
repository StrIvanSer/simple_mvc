package org.example.repo;

import java.util.List;

public interface ProjectRepository<T> {
    List<T> retrieveAll();
//    List<T> retrieveAllByFilter(String filter);

    void store(T book);

    boolean removeItemById(Integer bookIdRemove);

    //    boolean removeItemsByAuthor(String bookAuthorRemove);
//    boolean removeItemsBySize(Integer bookSizeRemove);
//    boolean removeItemsByTitle(String bookTitleRemove);
    void addDefaultBook();
}
