package it.paa.service;

import it.paa.model.entity.Author;
import it.paa.repository.AuthorRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.ws.rs.NotFoundException;

import java.util.List;

@ApplicationScoped
public class AuthorService implements AuthorRepository {

    @PersistenceContext
    private EntityManager entityManager;


    @Override
    public List<Author> getAll(String name, String surname) throws Exception {
        String query = "SELECT a FROM Author";
        List<Author> authors;
        if (name != null && !name.isEmpty() && !name.isBlank() &&
                surname != null && !surname.isEmpty() && !surname.isBlank()) {
            authors = entityManager.createQuery(query.concat("WHERE LOWER(a.name) = LOWER(:name) AND LOWER(a.surname) = LOWER(:description)"),Author.class)
                    .setParameter("name", name)
                    .setParameter("surname", surname)
                    .getResultList();
        } else if(name != null && !name.isEmpty() && !name.isBlank()){
            authors = entityManager.createQuery(query.concat("WHERE LOWER(a.name) = LOWER(:name)"),Author.class)
                    .setParameter("name", name)
                    .getResultList();
        }
        else if (surname != null && !surname.isEmpty() && !surname.isBlank()) {
            authors = entityManager.createQuery(query.concat("WHERE LOWER(a.surname) = LOWER(:surname)"),Author.class)
                    .setParameter("surname", surname)
                    .getResultList();
        }
        else {
            authors = entityManager.createQuery(query.concat("WHERE LOWER(a.surname) = LOWER(:surname)"),Author.class)
                    .setParameter("surname", surname)
                    .getResultList();
        }
        
        if(authors.isEmpty()){
            throw new Exception("No authors found");
        }
        
        return authors;
    }

    @Override
    public Author getById(Long id) throws NotFoundException {
        Author author = entityManager.find(Author.class, id);
        
        if(author == null){
            throw new NotFoundException("author not found");
        }
        
        return author;
    }

    @Override
    public Author save(Author author){
        entityManager.persist(author);
        return author;
    }

    @Override
    public Author update(Author author) {
        entityManager.merge(author);
        return author;
    }

    @Override
    public void delete(Long Id) throws NotFoundException {
        Author author = getById(Id);
        entityManager.remove(author);
    }
}