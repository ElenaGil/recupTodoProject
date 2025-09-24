package org.example.todoproject;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.NoSuchElementException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NoSuchElementException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleNoSuchElementException(NoSuchElementException e) {
        return "Not found! " + e.getMessage();
    }

    @ExceptionHandler(NullPointerException.class)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public String handleNullPointerException(NullPointerException e) {
        return "Null pointer!  " + e.getMessage();
    }
}
