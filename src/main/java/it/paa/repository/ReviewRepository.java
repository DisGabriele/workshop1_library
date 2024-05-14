package it.paa.repository;

import it.paa.model.entity.Review;
import jakarta.persistence.PersistenceException;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.NoContentException;

import java.time.LocalDate;
import java.util.List;

 public interface ReviewRepository {
     List<Review> getAll(Integer score, LocalDate startDate, LocalDate endDate, String username) throws IllegalArgumentException, NoContentException;
     Review getById(Long id) throws NotFoundException;
     Review save(Review review) throws PersistenceException;
     Review update(Review review) throws Exception;
     void delete(Review review) throws PersistenceException;
}
