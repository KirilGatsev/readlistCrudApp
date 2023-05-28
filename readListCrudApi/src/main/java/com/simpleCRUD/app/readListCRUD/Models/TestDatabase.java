package com.simpleCRUD.app.readListCRUD.Models;

import com.simpleCRUD.app.readListCRUD.Models.Dto.Book;
import com.simpleCRUD.app.readListCRUD.Services.Status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class TestDatabase {
    private static final Logger log = LoggerFactory.getLogger(TestDatabase.class);

    @Bean
    CommandLineRunner initDatabase(BookRepo bookRepo){
        return args -> {
            bookRepo.save(new Book("Anonymous", "Beowulf", 0, 272, Status.CURRENTLY_READING));
            bookRepo.save(new Book("Robert Louis Stevenson", "Strange Case of Dr Jekyll and Mr Hyde", 10, 141, Status.CURRENTLY_READING));

            bookRepo.save(new Book("Charles Dickens", "A Tale of Two Cities", 294, 294, Status.COMPLETED));
            bookRepo.save(new Book("Mario Puzo", "The Godfather",448, 448, Status.COMPLETED));

            bookRepo.save(new Book("Leo Tolstoy", "War and Peace",346, 1225, Status.DROPPED));
            bookRepo.save(new Book("William Faulkner", "As I Lay Dying",123, 208, Status.DROPPED));

            bookRepo.findAll().forEach(book -> log.info("Preloaded " + book));

        };
    }

}
