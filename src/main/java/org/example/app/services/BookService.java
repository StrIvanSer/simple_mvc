package org.example.app.services;

import org.apache.log4j.Logger;
import org.example.repo.BookRepository;
import org.example.repo.ProjectRepository;
import org.example.web.dto.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.springframework.util.CollectionUtils.isEmpty;

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

    public boolean removeBookByField(String valueField) {
        List<Book> allBooks = bookRepo.retrieveAll();
        if (valueField.length() == 0) return false;
        for (Book book : allBooks) {
            String bookValue = book.getAuthor() + " " + book.getTitle() + " " + book.getSize();
            if (isMatched(valueField, bookValue)) {
                bookRepo.removeItemByField(book);
            }
        }
        return true;
    }

    public void addBook() {
        bookRepo.addDefaultBook();
    }

    public List<Book> getBooksWithFilter(String filter) {
        List<Book> filterBooks = new ArrayList<>();
        List<Book> allBooks = bookRepo.retrieveAll();
        if (filter.length() == 0) return allBooks;
        for (Book book : allBooks) {
            String bookValue = book.getAuthor() + " " + book.getTitle() + " " + book.getSize();
            if (isMatched(filter, bookValue)) {
                filterBooks.add(book);
            }
        }
        return isEmpty(filterBooks) ? allBooks : filterBooks;
    }

    private void defaultInit() {
        logger.info("default INIT in BookService bean");
    }

    private void defaultDestroy() {
        logger.info("default DESTROY in BookService bean");
    }

    public boolean isMatched(String searchFilter, String text) {
        Pattern ptrn = Pattern.compile(".*(" + searchFilter.toLowerCase() + ").*");
        Matcher matcher = ptrn.matcher(text.toLowerCase());
        return matcher.lookingAt();
    }

}
