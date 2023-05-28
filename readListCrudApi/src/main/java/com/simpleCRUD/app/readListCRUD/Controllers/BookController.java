package com.simpleCRUD.app.readListCRUD.Controllers;

import com.simpleCRUD.app.readListCRUD.Models.BookRepo;
import com.simpleCRUD.app.readListCRUD.Models.Dto.Book;
import com.simpleCRUD.app.readListCRUD.Models.Dto.BookNotFoundException;
import com.simpleCRUD.app.readListCRUD.Services.BookModelAssembler;
import com.simpleCRUD.app.readListCRUD.Services.BookService;
import com.simpleCRUD.app.readListCRUD.Services.Status;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.mediatype.problem.Problem;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Min;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import java.util.List;

@RestController
public class BookController {
    private final BookRepo bookRepo;
    private final BookModelAssembler bookModelAssembler;
    private final BookService bookService;

    BookController(BookRepo bookRepo, BookModelAssembler bookModelAssembler, BookService bookService){
        this.bookRepo = bookRepo;
        this.bookModelAssembler = bookModelAssembler;
        this.bookService = bookService;
    }

    @GetMapping("/books")
    public CollectionModel<EntityModel<Book>> all(){
        List<EntityModel<Book>> booksEntities = bookRepo.findAll().stream().map(
                bookModelAssembler::toModel).toList();

        return CollectionModel.of(booksEntities,
                linkTo(methodOn(BookController.class).all()).withSelfRel());
    }

    @GetMapping("/books/{id}")
    public EntityModel<Book> one(@PathVariable Long id){
        Book book = bookRepo.findById(id).orElseThrow(() -> new BookNotFoundException(id));

        return bookModelAssembler.toModel(book);
    }

    @GetMapping("books/completed")
    public CollectionModel<EntityModel<Book>> allCompleted(){
        List<EntityModel<Book>> completedBooks = bookRepo.findAllByStatus(Status.COMPLETED)
                .stream().map(bookModelAssembler::toModel).toList();

        return CollectionModel.of(completedBooks);
    }

    @GetMapping("books/currently-reading")
    public CollectionModel<EntityModel<Book>> allCurrentlyReading(){
        List<EntityModel<Book>> completedBooks = bookRepo.findAllByStatus(Status.CURRENTLY_READING)
                .stream().map(bookModelAssembler::toModel).toList();

        return CollectionModel.of(completedBooks);
    }

    @GetMapping("books/dropped")
    public CollectionModel<EntityModel<Book>> allDropped(){
        List<EntityModel<Book>> completedBooks = bookRepo.findAllByStatus(Status.DROPPED)
                .stream().map(bookModelAssembler::toModel).toList();

        return CollectionModel.of(completedBooks);
    }

    @PostMapping("/books")
    public ResponseEntity<?> newBook(@RequestBody Book book){
        if(bookService.validateBook(book)){
            book.setStatus(Status.CURRENTLY_READING);

            Book newBook = bookRepo.save(book);
            EntityModel<Book> entity = bookModelAssembler.toModel(book);

            return ResponseEntity.created(linkTo(methodOn(BookController.class).one(newBook.getId())).toUri())
                    .body(entity);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .header(HttpHeaders.CONTENT_TYPE, MediaTypes.HTTP_PROBLEM_DETAILS_JSON_VALUE)
                .body(Problem.create()
                        .withTitle("Unviable parameters.")
                        .withDetail("One or more parameters are not viable."));
    }

    @PatchMapping("/books/{id}/pick-up")
    public ResponseEntity<?> pickBookFromDropped(@PathVariable Long id){
        Book book = bookRepo.findById(id).orElseThrow(() -> new BookNotFoundException(id));

        if(book.getStatus() == Status.DROPPED){
            bookService.pickBookUpAgain(book);
            return ResponseEntity.ok(bookModelAssembler.toModel(book));
        }
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED)
                .header(HttpHeaders.CONTENT_TYPE, MediaTypes.HTTP_PROBLEM_DETAILS_JSON_VALUE)
                .body(Problem.create()
                        .withTitle("Method not allowed.")
                        .withDetail("Can't pick up book with " + book.getStatus() + " status."));
    }

    @PatchMapping("/books/{id}/complete")
    public ResponseEntity<?> completeBook(@PathVariable Long id){
        Book book = bookRepo.findById(id).orElseThrow(() -> new BookNotFoundException(id));

        if(book.getStatus() == Status.CURRENTLY_READING){
            bookService.completeBook(book);
            EntityModel<Book> bookEntity = bookModelAssembler.toModel(book);
            return ResponseEntity.ok(bookEntity);
        }

        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED)
                .header(HttpHeaders.CONTENT_TYPE, MediaTypes.HTTP_PROBLEM_DETAILS_JSON_VALUE)
                .body(Problem.create()
                        .withTitle("Method not allowed.")
                        .withDetail("Can't complete book with " + book.getStatus() + " status."));
    }

    @PatchMapping("/books/{id}/pages-read")
    public ResponseEntity<?> changePagesRead(@PathVariable Long id,
                                             @RequestParam(name = "read") @Min(0) int pages){
        Book book = bookRepo.findById(id).orElseThrow(() -> new BookNotFoundException(id));
        if(bookService.validatePageUpdate(book, pages)){
            return ResponseEntity.ok().body(bookModelAssembler.toModel(book));
        }

        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED)
                .header(HttpHeaders.CONTENT_TYPE, MediaTypes.HTTP_PROBLEM_DETAILS_JSON_VALUE)
                .body(Problem.create()
                        .withTitle("Method not allowed.")
                        .withDetail("Number must be between 0 and total pages of book("
                                + book.getPagesTotal() + ")"));
    }

    @PatchMapping("/books/{id}/drop")
    public ResponseEntity<?> dropBook(@PathVariable Long id){
        Book book = bookRepo.findById(id).orElseThrow(() -> new BookNotFoundException(id));
        if(book.getStatus() == Status.CURRENTLY_READING){
            bookService.dropBook(book);
            EntityModel<Book> bookEntity = bookModelAssembler.toModel(book);
            return ResponseEntity.ok(bookEntity);
        }

        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED)
                .header(HttpHeaders.CONTENT_TYPE, MediaTypes.HTTP_PROBLEM_DETAILS_JSON_VALUE)
                .body(Problem.create()
                        .withTitle("Method not allowed.")
                        .withDetail("Can't drop a book with " + book.getStatus() + " status."));
    }

    @DeleteMapping("/books/{id}/delete")
    ResponseEntity<?> deleteBook(@PathVariable Long id){
        bookRepo.deleteById(id);

        return ResponseEntity.noContent().build();
    }
}
