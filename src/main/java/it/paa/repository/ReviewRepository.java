package it.paa.repository;

import it.paa.model.entity.Review;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.NoContentException;

import java.time.LocalDate;
import java.util.List;

 public interface ReviewRepository {
     List<Review> getAll(Integer score, LocalDate startDate, LocalDate endDate) throws IllegalArgumentException, NoContentException;
     Review getById(Long id) throws NotFoundException;
     Review save(Review review) throws Exception;
     Review update(Review review) throws Exception;
     void delete(Long id) throws NotFoundException;
}
