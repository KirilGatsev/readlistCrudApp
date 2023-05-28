package com.simpleCRUD.app.readListCRUD.Models.Dto;


public class BookNotFoundException extends RuntimeException{
    public BookNotFoundException(Long id){
        super("Book with id: " + id + " not in reading list.");
    }
}
