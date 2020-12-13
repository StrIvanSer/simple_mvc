package org.example.app.services;

import org.apache.log4j.Logger;
import org.example.repo.BookRepository;
import org.example.repo.ProjectRepository;
import org.example.web.dto.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {

    private final Logger logger = Logger.getLogger(BookService.class);

    private final ProjectRepository<Book> bookRepo;

    @Autowired
    public BookService(ProjectRepository<Book> bookRepo) {
        this.bookRepo = bookRepo;
    }

    public List<Book> getAllBooks() {
        return bookRepo.retrieveAll();
    }

    public void saveBook(Book book) {
        bookRepo.store(book);
    }

    public boolean removeBookById(Integer bookIdRemove) {
        return bookRepo.removeItemById(bookIdRemove);
    }

    public void addBook() { bookRepo.addDefaultBook();
    }

    private void defaultInit() {
        logger.info("default INIT in BookService bean");
    }

    private void defaultDestroy() {
        logger.info("default DESTROY in BookService bean");
    }

}
