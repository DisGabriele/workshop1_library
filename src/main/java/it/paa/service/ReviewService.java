package it.paa.service;

import it.paa.model.entity.Review;
import it.paa.repository.ReviewRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.NoContentException;

import java.time.LocalDate;
import java.util.List;

@ApplicationScoped
public class ReviewService implements ReviewRepository {

    @PersistenceContext
    private EntityManager entityManager;


    @Override
    public List<Review> getAll(Integer score, LocalDate startDate, LocalDate endDate) throws IllegalArgumentException, NoContentException {
        String query = "SELECT r FROM Review r";
        List<Review> reviews;
        if (score != null && startDate != null && endDate != null) {
            reviews = entityManager.createQuery(query.concat(" WHERE r.score = :score AND r.date BETWEEN :startDate AND :endDate"), Review.class)
                    .setParameter("score", score)
                    .setParameter("startDate", startDate)
                    .setParameter("endDate", endDate)
                    .getResultList();
        } else if (score != null) {
            reviews = entityManager.createQuery(query.concat(" WHERE r.score = :score"), Review.class)
                    .setParameter("score", score)
                    .getResultList();
        } else if (startDate != null || endDate != null) {
            if (startDate == null || endDate == null) {
                System.out.println("Start date or end date");
                throw new IllegalArgumentException("Start date and end date cannot must be both empty or filled");
            } else {
                System.out.println("Start date and end date");
                reviews = entityManager.createQuery(query.concat(" WHERE r.date BETWEEN :startDate AND :endDate"), Review.class)
                        .setParameter("startDate", startDate)
                        .setParameter("endDate", endDate)
                        .getResultList();
            }
        } else {
            reviews = entityManager.createQuery(query, Review.class)
                    .getResultList();
        }

        if (reviews.isEmpty())
            throw new NoContentException("no reviews found");

        return reviews;
    }

    @Override
    public Review getById(Long id) throws NotFoundException {
        Review review = entityManager.find(Review.class, id);

        if (review == null)
            throw new NotFoundException("Review not found");

        return review;
    }

    @Override
    public Review save(Review review) {
        entityManager.persist(review);
        return review;
    }

    @Override
    public Review update(Review review) {
        entityManager.merge(review);
        return review;
    }

    @Override
    public void delete(Long id) throws NotFoundException {
        Review review = getById(id);
        entityManager.remove(review);
    }
}
