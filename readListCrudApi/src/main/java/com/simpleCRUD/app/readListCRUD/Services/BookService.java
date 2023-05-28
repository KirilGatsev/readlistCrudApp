package com.simpleCRUD.app.readListCRUD.Services;

import com.simpleCRUD.app.readListCRUD.Models.BookRepo;
import com.simpleCRUD.app.readListCRUD.Models.Dto.Book;
import org.springframework.stereotype.Service;

@Service
public class BookService {
    private BookRepo bookRepo;
    private BookValidation bookValidation;
    public BookService(BookRepo bookRepo, BookValidation bookValidation){
        this.bookRepo = bookRepo;
        this.bookValidation = bookValidation;
    }

    public boolean validateBook(Book book){
        return bookValidation.validReadPages(book.getPagesRead(), book.getPagesTotal())
                && bookValidation.validTotalPages(book.getPagesTotal())
                && bookValidation.validName(book.getName());
    }

    public boolean validatePageUpdate(Book book, int newPages){
        if(bookValidation.validReadPages(newPages, book.getPagesTotal())){
            book.setPagesRead(newPages);
            bookRepo.save(book);
            return true;
        }
        return false;
    }

    public void dropBook(Book book){
        book.setStatus(Status.DROPPED);
        bookRepo.save(book);
    }

    public void completeBook(Book book){
        book.setStatus(Status.COMPLETED);
        book.setPagesRead(book.getPagesTotal());
        bookRepo.save(book);
    }

    public void pickBookUpAgain(Book book){
        book.setStatus(Status.CURRENTLY_READING);
        bookRepo.save(book);
    }

}
