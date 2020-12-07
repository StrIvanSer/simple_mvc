package org.example.repo;

import org.apache.log4j.Logger;
import org.example.app.services.IdProvider;
import org.example.web.dto.Book;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;


@Repository
public class BookRepository implements ProjectRepository<Book>, ApplicationContextAware {

    private final Logger logger = Logger.getLogger(BookRepository.class);
    private final List<Book> repo = new ArrayList<>();
    private ApplicationContext context;

    @Override
    public boolean removeItemById(String bookIdRemove) {
        for (Book book : retrieveAll()) {
            if (book.getId().equals(bookIdRemove)) {
                logger.info("remove book completed: " + book);
                return repo.remove(book);
            }
        }
        return false;
    }

    @Override
    public List<Book> retrieveAll() {
        return new ArrayList<>(repo);
    }

    @Override
    public void store(Book book) {
        book.setId(context.getBean(IdProvider.class).provideId(book));
        logger.info("store new book:" + book);
        repo.add(book);
    }

    @Override
    public void addDefaultBook() {
        repo.add(new Book("1", "Ivanov", "mist", 244));
        repo.add(new Book("2", "Ivanov", "1984", 555));
        repo.add(new Book("3", "Ivanov", "arch", 1243));
        repo.add(new Book("4", "Petrov", "lesson", 666));
        repo.add(new Book("5", "Petrov", "arcade", 777));
        repo.add(new Book("6", "Sidorov", "test", 888));
        repo.add(new Book("7", "Sidorov", "test2", 999));

    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }

    private void defaultInit() {
        logger.info("default INIT in BookRepository bean");
    }

    private void defaultDestroy() {
        logger.info("default DESTROY in BookRepository bean");
    }
}
