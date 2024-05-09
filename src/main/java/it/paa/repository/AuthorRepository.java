package it.paa.repository;

import it.paa.model.entity.Author;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.NoContentException;

import java.util.List;

public interface AuthorRepository {
    List<Author> getAll(String name, String surname) throws NoContentException;
    Author getById(Long id) throws NotFoundException;
    Author save(Author author) throws Exception;
    Author update(Author author) throws Exception;
    void delete(Long Id) throws NotFoundException;
}
