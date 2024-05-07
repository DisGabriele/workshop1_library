package it.paa.repository;

import it.paa.model.entity.Author;

import java.util.List;

public interface AuthorRepository {
    public List<Author> getAll(String name, String surname) throws Exception;
    public Author getById(Long id) throws Exception;
    public Author save(Author author) throws Exception;
    public Author update(Author author) throws Exception;
    public void delete(Long Id) throws Exception;
}
