package org.example.repo;

import org.apache.log4j.Logger;
import org.example.web.dto.Book;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.springframework.util.CollectionUtils.isEmpty;

@Repository
public class BookRepository implements ProjectRepository<Book> {

    private final Logger logger = Logger.getLogger(BookRepository.class);
    private final List<Book> repo = new ArrayList<>();

    @Override
    public boolean removeItemById(Integer bookIdRemove) {
        for (Book book : retrieveAll()) {
            if (book.getId().equals(bookIdRemove)) {
                logger.info("remove book completed: " + book);
                return repo.remove(book);
            }
        }
        return false;
    }

    @Override
    public boolean removeItemsByAuthor(String bookAuthorRemove) {
        boolean removeBook = false;
        for (Book book : retrieveAll()) {
            if (isMatched(bookAuthorRemove, book.getAuthor())) {
                logger.info("remove book completed: " + book);
                repo.remove(book);
                removeBook = true;
            }
        }
        return removeBook;
    }

    /***
     * Целенаправленно не стал добавлять регулярное выражение для удаления по кол-ву страниц, чтобы не удалялись лишние
     * книги
     *
     @param booksSizeRemove кол-во страниц
     @return удалились ли какие либо книги
     */
    @Override
    public boolean removeItemsBySize(Integer booksSizeRemove) {
        boolean removeBook = false;
        for (Book book : retrieveAll()) {
            if (book.getSize().equals(booksSizeRemove)) {
                logger.info("remove book completed: " + book);
                repo.remove(book);
                removeBook = true;
            }
        }
        return removeBook;
    }

    @Override
    public boolean removeItemsByTitle(String booksTitleRemove) {
        boolean removeBook = false;
        for (Book book : retrieveAll()) {
            if (isMatched(booksTitleRemove, book.getTitle())) {
                logger.info("remove book completed: " + book);
                repo.remove(book);
                removeBook = true;
            }
        }
        return removeBook;
    }

    @Override
    public List<Book> retrieveAll() {
        return new ArrayList<>(repo);
    }

    /***
     @param filter фильтр для отображения списка книг по автору
     @return список книг по Автору, в случае пустого значения filter либо не найденных значений вернет весь список
     */
    @Override
    public List<Book> retrieveAllByFilter(String filter) {
        List<Book> books = new ArrayList<>();
        if (filter.length() == 0) return new ArrayList<>(repo);
        for (Book book : retrieveAll()) {
            String bookValue = book.getAuthor() + " " + book.getTitle() + " " + book.getSize();
            if (isMatched(filter, bookValue)) {
                books.add(book);
            }
        }
        return isEmpty(books) ? new ArrayList<>(repo) : books;

    }

    @Override
    public void store(Book book) {
        book.setId(book.hashCode());
        logger.info("store new book:" + book);
        repo.add(book);
    }

    @Override
    public void addDefaultBook() {
        repo.add(new Book(1, "Ivanov", "mist", 244));
        repo.add(new Book(2, "Ivanov", "1984", 555));
        repo.add(new Book(3, "Ivanov", "arch", 1243));
        repo.add(new Book(4, "Petrov", "lesson", 666));
        repo.add(new Book(5, "Petrov", "arcade", 777));
        repo.add(new Book(6, "Sidorov", "test", 888));
        repo.add(new Book(7, "Sidorov", "test2", 999));

    }

    public boolean isMatched(String searchFilter, String text) {
        Pattern ptrn = Pattern.compile(".*(" + searchFilter.toLowerCase() + ").*");
        Matcher matcher = ptrn.matcher(text.toLowerCase());
        return matcher.lookingAt();
    }
}
