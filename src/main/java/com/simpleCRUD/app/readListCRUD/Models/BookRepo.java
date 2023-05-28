package com.simpleCRUD.app.readListCRUD.Models;

import com.simpleCRUD.app.readListCRUD.Models.Dto.Book;
import com.simpleCRUD.app.readListCRUD.Services.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepo extends JpaRepository<Book, Long> {
    List<Book> findAllByStatus(Status status);
}
