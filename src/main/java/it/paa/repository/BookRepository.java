package it.paa.repository;

import it.paa.model.entity.Book;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceException;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.NoContentException;

import java.util.List;

public interface BookRepository {
    List<Book> getAll(String title, Integer startDate, Integer endDate ) throws NoContentException;
    Book getById(Long id) throws NotFoundException;
    Book getByTitle(String title) throws NoResultException;
    Book save(Book book) throws PersistenceException;
    Book update(Book book) throws PersistenceException;
    void delete(Long id) throws NotFoundException;
}
