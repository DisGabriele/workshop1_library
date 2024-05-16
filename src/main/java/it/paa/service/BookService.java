package it.paa.service;

import it.paa.model.entity.Book;
import it.paa.repository.BookRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.PersistenceException;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.NoContentException;

import java.util.List;

@ApplicationScoped
public class BookService implements BookRepository {

    @PersistenceContext
    private EntityManager entityManager;

    /*
    GET all con possibilità di filtrare se viene passato qualche parametro
    (per l'intervallo di tempo, implementata verifica per far inserire o entrambe le date o nessuna),
    altrimenti ritorna la lista completa
     */
    @Override
    public List<Book> getAll(String title, Integer startDate, Integer endDate) throws IllegalArgumentException, NoContentException {
        String query = "SELECT b FROM Book b";
        List<Book> books;

        if (title != null && !title.isEmpty() && !title.isBlank() &&
                startDate != null && endDate != null) {
            books = entityManager.createQuery(query.concat(" WHERE LOWER(b.title) = LOWER(:title) AND b.publishingDate BETWEEN :startDate AND :endDate"), Book.class)
                    .setParameter("title", title)
                    .setParameter("startDate", startDate)
                    .setParameter("endDate", endDate)
                    .getResultList();
        } else if (title != null && !title.isEmpty() && !title.isBlank()) {
            books = entityManager.createQuery(query.concat(" WHERE LOWER(b.title) = LOWER(:title)"), Book.class)
                    .setParameter("title", title)
                    .getResultList();
        } else if (startDate != null || endDate != null) {
            if (startDate == null || endDate == null) {
                throw new IllegalArgumentException("Start date and end date cannot must be both empty or filled");
            } else {
                books = entityManager.createQuery(query.concat(" WHERE b.publishingDate BETWEEN :startDate AND :endDate"), Book.class)
                        .setParameter("startDate", startDate)
                        .setParameter("endDate", endDate)
                        .getResultList();
            }
        } else {
            books = entityManager.createQuery(query, Book.class)
                    .getResultList();
        }

        if (books.isEmpty())
            throw new NoContentException("no books found");

        return books;
    }

    @Override
    public Book getById(Long id) throws NotFoundException {
        Book book = entityManager.find(Book.class, id);

        if(book == null)
            throw new NotFoundException("Book not found");

        return book;
    }

    @Override
    public Book save(Book book) throws PersistenceException {
        entityManager.persist(book);
        return book;
    }

    @Override
    public Book update(Book book) {
            entityManager.merge(book);
            return book;
    }

    @Override
    public void delete(Long id) throws NotFoundException {
        Book book = getById(id);
        entityManager.remove(book);
    }

}
