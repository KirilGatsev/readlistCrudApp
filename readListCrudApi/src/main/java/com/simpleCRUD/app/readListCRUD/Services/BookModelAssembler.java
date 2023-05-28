package com.simpleCRUD.app.readListCRUD.Services;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import com.simpleCRUD.app.readListCRUD.Controllers.BookController;
import com.simpleCRUD.app.readListCRUD.Models.Dto.Book;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

@Component
public class BookModelAssembler implements RepresentationModelAssembler<Book, EntityModel<Book>> {
    @Override
    public EntityModel<Book> toModel(Book book) {
        EntityModel<Book> bookEntity = EntityModel.of(book, linkTo(methodOn(BookController.class).one(book.getId())).withSelfRel(),
                linkTo(methodOn(BookController.class).all()).withRel("books"));

        if(book.getStatus() == Status.CURRENTLY_READING){
            bookEntity.add(linkTo(methodOn(BookController.class)
                    .allCurrentlyReading()).withRel("currently-reading-books"));
            bookEntity.add(linkTo(methodOn(BookController.class).changePagesRead(book.getId(), book.getPagesRead())).withRel("add-pages-read"));
            bookEntity.add(linkTo(methodOn(BookController.class).completeBook(book.getId())).withRel("complete"));
            bookEntity.add(linkTo(methodOn(BookController.class).dropBook(book.getId())).withRel("drop"));
        }

        if(book.getStatus() == Status.COMPLETED){
            bookEntity.add(linkTo(methodOn(BookController.class).allCompleted()).withRel("completed-books"));
        }

        if(book.getStatus() == Status.DROPPED){
            bookEntity.add(linkTo(methodOn(BookController.class).allDropped()).withRel("dropped-books"));
            bookEntity.add(linkTo(methodOn(BookController.class).pickBookFromDropped(book.getId())).withRel("pick-up-again"));
        }

        return bookEntity;
    }
}
