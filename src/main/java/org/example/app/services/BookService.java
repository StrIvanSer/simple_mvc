package org.example.app.services;

import org.example.repo.ProjectRepository;
import org.example.web.dto.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {

    private final ProjectRepository<Book> bookRepo;

    @Autowired
    public BookService(ProjectRepository<Book> bookRepo) {
        this.bookRepo = bookRepo;
    }

    public List<Book> getAllBooks() {
        return bookRepo.retrieveAll();
    }

    public List<Book> getBooksWithFilter(String filter) {
        return bookRepo.retrieveAllByFilter(filter);
    }

    public void saveBook(Book book) {
        bookRepo.store(book);
    }

    public boolean removeBookById(Integer bookIdRemove) {
        return bookRepo.removeItemById(bookIdRemove);
    }

    public boolean removeBooksByAuthor(String bookAuthorRemove) {
        return bookRepo.removeItemsByAuthor(bookAuthorRemove);
    }

    public boolean removeBooksBySize(Integer bookAuthorRemove) {
        return bookRepo.removeItemsBySize(bookAuthorRemove);
    }

    public boolean removeBooksByTitle(String bookTitleRemove) {
        return bookRepo.removeItemsByTitle(bookTitleRemove);
    }

    public void addBook() { bookRepo.addDefaultBook();
    }
}
