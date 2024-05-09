package it.paa.repository;

import it.paa.model.entity.Book;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.NoContentException;

import java.time.LocalDate;
import java.util.List;

public interface BookRepository {
    List<Book> getAll(String title, Integer startDate, Integer endDate ) throws NoContentException;
    Book getById(Long id) throws NotFoundException;
    Book save(Book book) throws Exception;
    Book update(Book book) throws Exception;
    void delete(Long id) throws NotFoundException;
}
