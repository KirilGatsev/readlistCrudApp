package com.simpleCRUD.app.readListCRUD.Controllers;

import com.simpleCRUD.app.readListCRUD.Models.Dto.BookNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionsControllerAdvice {

    @ExceptionHandler(BookNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String bookNotFoundExceptionHandler(BookNotFoundException ex){
        return ex.getMessage();
    }
}
