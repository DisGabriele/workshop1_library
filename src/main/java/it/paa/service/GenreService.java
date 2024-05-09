package it.paa.service;

import it.paa.model.entity.Genre;
import it.paa.repository.GenreRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.NoContentException;

import java.util.List;

@ApplicationScoped
public class GenreService implements GenreRepository {

    @PersistenceContext
    private EntityManager entityManager;

    /*
    GET all con possibilità di filtrare se viene passato qualche parametro, altrimenti ritorna la lista completa
     */
    @Override
    public List<Genre> getAll(String name, String description) throws NoContentException {
        String query = "SELECT g FROM Genre g";
        List<Genre> genres;
        if (name != null && !name.isEmpty() && !name.isBlank() &&
                description != null && !description.isEmpty() && !description.isBlank()
        ) {
            genres = entityManager.createQuery(query.concat(" WHERE LOWER(g.name) = LOWER(:name) AND LOWER(g.description) = LOWER(:description)"), Genre.class)
                    .setParameter("name", name)
                    .setParameter("description", description)
                    .getResultList();
        } else if (name != null && !name.isEmpty() && !name.isBlank()) {
            genres = entityManager.createQuery(query.concat(" WHERE LOWER(g.name) = LOWER(:name)"), Genre.class)
                    .setParameter("name", name)
                    .getResultList();
        } else if (description != null && !description.isEmpty() && !description.isBlank()) {
            genres = entityManager.createQuery(query.concat(" WHERE LOWER(g.description) = LOWER(:description)"), Genre.class)
                    .setParameter("description", description)
                    .getResultList();
        } else {
            genres = entityManager.createQuery(query, Genre.class).getResultList();
        }
        if (genres.isEmpty())
            throw new NoContentException("no genres found");

        return genres;
    }

    @Override
    public Genre getById(Long id) throws NotFoundException {
        Genre genre = entityManager.find(Genre.class, id);

        if (genre == null)
            throw new NotFoundException("genre not found");

        return genre;
    }


    @Override
    public Genre save(Genre genre) {
        entityManager.persist(genre);
        return genre;
    }

    @Override
    public Genre update(Genre genre) {
        entityManager.merge(genre);
        return genre;
    }

    @Override
    public void delete(Long id) throws NotFoundException{
        Genre genre = getById(id);
        entityManager.remove(genre);
    }

}
