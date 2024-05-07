package it.paa.service;

import it.paa.model.entity.Book;
import it.paa.repository.BookRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.ws.rs.NotFoundException;

import java.util.List;

@ApplicationScoped
public class BookService implements BookRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Book> getAll(String title, Integer startDate, Integer endDate) throws Exception {
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
                throw new Exception("Start date and end date cannot must be both empty or filled");
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
            throw new Exception("no books found");

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
    public Book save(Book book) {
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
