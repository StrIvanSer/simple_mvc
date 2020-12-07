package org.example.web.controllers;


import org.apache.log4j.Logger;
import org.example.app.services.BookService;
import org.example.web.dto.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

@Controller
@RequestMapping(value = "books")
@Scope("singleton")
public class BookShelfController {

    private final Logger logger = Logger.getLogger(BookShelfController.class);
    private final BookService bookService;

    @Autowired
    public BookShelfController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/shelf")
    public String books(Model model) {
        logger.info(this.toString());
        model.addAttribute("book", new Book());
        model.addAttribute("bookList", bookService.getAllBooks());
        return "book_shelf";
    }

    @PostMapping("/addBook")
    public String addBook() {
        logger.info("add default book to the store");
        bookService.addBook();
        return "redirect:/books/shelf";
    }

    @PostMapping("/save")
    public String saveBook(Book book) {
        if (book.getAuthor().length() == 0 && isNull(book.getSize()) && book.getTitle().length() == 0) {
            return "redirect:/books/shelf";
        }
        bookService.saveBook(book);
        logger.info("current repository size: " + bookService.getAllBooks().size());
        return "redirect:/books/shelf";
    }

    @PostMapping("/removeById")
    public String removeBookById(@RequestParam(value = "bookIdToRemove") String bookIdRemove) {
        if (bookService.removeBookById(bookIdRemove)) {
            logger.info("Remove book with ID: " + bookIdRemove);
        }
        return "redirect:/books/shelf";
    }

}
