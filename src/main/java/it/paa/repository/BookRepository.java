package it.paa.repository;

import it.paa.model.entity.Book;
import jakarta.ws.rs.NotFoundException;

import java.time.LocalDate;
import java.util.List;

public interface BookRepository {
    public List<Book> getAll(String title, Integer startDate, Integer endDate ) throws Exception;
    public Book getById(Long id) throws NotFoundException;
    public Book save(Book book) throws Exception;
    public Book update(Book book) throws Exception;
    public void delete(Long id) throws NotFoundException;
}
