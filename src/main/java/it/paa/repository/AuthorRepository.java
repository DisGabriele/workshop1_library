package it.paa.repository;

import it.paa.model.entity.Author;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.NoContentException;

import java.util.List;

public interface AuthorRepository {
    public List<Author> getAll(String name, String surname) throws NoContentException;
    public Author getById(Long id) throws NotFoundException;
    public Author save(Author author) throws Exception;
    public Author update(Author author) throws Exception;
    public void delete(Long Id) throws NotFoundException;
}
