package com.simpleCRUD.app.readListCRUD.Models.Dto;

import com.simpleCRUD.app.readListCRUD.Services.Status;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import java.util.Objects;

@Entity(name = "books")
public class Book {
    @Id
    @GeneratedValue
    private Long id;
    private String author;
    private String name;
    private int pagesRead;
    private int pagesTotal;
    private Status status;

    public Book() {}

    public Book(String author, String name, int pagesRead, int pagesTotal, Status status){
        this.author = author;
        this.name = name;
        this.pagesTotal = pagesTotal;
        this.pagesRead = pagesRead;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPagesRead() {
        return pagesRead;
    }

    public void setPagesRead(int pagesRead) {
        this.pagesRead = pagesRead;
    }

    public int getPagesTotal() {
        return pagesTotal;
    }

    public void setPagesTotal(int pagesTotal) {
        this.pagesTotal = pagesTotal;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Book book)) return false;
        return pagesRead == book.pagesRead && pagesTotal == book.pagesTotal && Objects.equals(id, book.id) && Objects.equals(author, book.author) && Objects.equals(name, book.name) && status == book.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, author, name, pagesRead, pagesTotal, status);
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", author='" + author + '\'' +
                ", name='" + name + '\'' +
                ", pagesRead=" + pagesRead +
                ", pagesTotal=" + pagesTotal +
                ", status=" + status +
                '}';
    }
}
