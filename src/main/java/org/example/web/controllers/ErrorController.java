package org.example.web.controllers;


import org.example.app.exceptions.BookShelfLoginException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;

@Controller//проброс ошибок для всех контроллеров
public class ErrorController {

    @GetMapping("/404")
    public String notFoundError() {
        return "404";
    }

}
