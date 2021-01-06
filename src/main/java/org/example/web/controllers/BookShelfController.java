package org.example.web.controllers;


import org.apache.log4j.Logger;
import org.example.app.services.BookService;
import org.example.web.dto.Book;
import org.example.web.dto.BookIdToRemove;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

@Controller
@RequestMapping(value = "books")
@Scope("singleton")
public class BookShelfController {

    private final Logger logger = Logger.getLogger(BookShelfController.class);
    private final BookService bookService;

    @Value("${errorUpload.message}")
    private String errorUploadMessage;

    @Value("${errorEmptyField.message}")
    private String errorEmptyFieldMessage;

    @Autowired
    public BookShelfController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/shelf")
    public String books(Model model) {
        logger.info(this.toString());
        model.addAttribute("book", new Book());
        model.addAttribute("bookIdToRemove", new BookIdToRemove());
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
    public String saveBook(@Valid Book book, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("book", book);
            model.addAttribute("bookIdToRemove", new BookIdToRemove());
            model.addAttribute("bookList", bookService.getAllBooks());
            return "book_shelf";
        } else {
            bookService.saveBook(book);
            logger.info("current repository size: " + bookService.getAllBooks().size());
            return "redirect:/books/shelf";
        }
    }

    @PostMapping("/remove")
    public String removeBook(@Valid BookIdToRemove bookIdRemove, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("book", new Book());
            model.addAttribute("bookList", bookService.getAllBooks());
            return "book_shelf";
        } else {
            bookService.removeBookById(bookIdRemove.getId());
            logger.info("Remove book with ID: " + bookIdRemove);
            return "redirect:/books/shelf";
        }
    }

    @PostMapping("/removeByField")
    public String removeBookBy(@RequestParam String fieldValue, Model model) {
        if (!bookService.removeBookByField(fieldValue)) {
            errorEmptyFieldMessage = "Filed is empty";
            return errorMessagesPage("errorEmptyFieldMessage", errorEmptyFieldMessage, model);
        }

        return "redirect:/books/shelf";
    }

    @PostMapping("/filter")
    public String filter(@RequestParam String filter, Model model) {
        logger.info("filter by Author");
        model.addAttribute("book", new Book());
        model.addAttribute("bookIdToRemove", new BookIdToRemove());
        model.addAttribute("bookList", bookService.getBooksWithFilter(filter));
        return "book_shelf";
    }

    @PostMapping("/uploadFile")
    public String uploadFile(@RequestParam("file") MultipartFile file, Model model) throws Exception {
        if (file.isEmpty()) {
            errorUploadMessage = "File is not found";
            return errorMessagesPage("errorUploadMessage", errorUploadMessage, model);
        } else {
            String name = file.getOriginalFilename();
            byte[] bytes = file.getBytes();

            //create dir
            String rootPath = System.getProperty("catalina.home");
            File dir = new File(rootPath + File.separator + "external_uploads");
            if (!dir.exists()) {
                dir.mkdirs();
            }

            //create file
            File serverFile = new File(dir.getAbsolutePath() + File.separator + name);
            BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile));
            stream.write(bytes);// записывает данные
            stream.close();// закрывает соединение

            logger.info("new file saved: " + serverFile.getAbsolutePath());

            return "redirect:/books/shelf";

        }
    }

    @RequestMapping("/download")
    public ResponseEntity<Object> downloadFile(@RequestParam("downloadfile") File file) throws Exception {

        logger.info("Start download");
        String filename = System.getProperty("catalina.home");
        File downloadFile = new File(filename + File.separator + "external_uploads/" + file.getName());
        logger.info("Start Download" + downloadFile.getAbsolutePath());
        InputStreamResource resource = new InputStreamResource(new FileInputStream(downloadFile));
        HttpHeaders headers = new HttpHeaders();

        headers.add("Content-Disposition", String.format("attachment; filename=\"%s\"", downloadFile.getName()));
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");

        ResponseEntity<Object> responseEntity = ResponseEntity.ok().headers(headers).contentLength(downloadFile.length()).contentType(
                MediaType.parseMediaType("application/txt")).body(resource);
        logger.info("finish download");
        return responseEntity;
    }

    private String errorMessagesPage(String errorMessage, String errorField, Model model) {
        model.addAttribute("book", new Book());
        model.addAttribute("bookIdToRemove", new BookIdToRemove());
        model.addAttribute("bookList", bookService.getAllBooks());
        model.addAttribute(errorMessage, errorField);
        return "book_shelf";
    }

}
