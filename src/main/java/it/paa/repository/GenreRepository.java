package it.paa.repository;

import it.paa.model.entity.Genre;

import java.util.List;

public interface GenreRepository {
    public List<Genre> getAll(String name, String description) throws Exception;
    public Genre getById(Long id) throws Exception;
    public Genre save(Genre genre) throws Exception;
    public Genre update(Genre genre) throws Exception;
    public void delete(Long id) throws Exception;
}
