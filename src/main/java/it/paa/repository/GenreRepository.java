package it.paa.repository;

import it.paa.model.entity.Genre;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.NoContentException;

import java.util.List;

public interface GenreRepository {
    List<Genre> getAll(String name, String description) throws NoContentException;
    Genre getById(Long id) throws NotFoundException;
    Genre save(Genre genre) throws Exception;
    Genre update(Genre genre) throws Exception;
    void delete(Long id) throws NotFoundException;
}
