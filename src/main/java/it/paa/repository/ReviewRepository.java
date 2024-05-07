package it.paa.repository;

import it.paa.model.entity.Review;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.NoContentException;

import java.time.LocalDate;
import java.util.List;

public interface ReviewRepository {
    public List<Review> getAll(Integer score, LocalDate startDate, LocalDate endDate) throws IllegalArgumentException, NoContentException;
    public Review getById(Long id) throws NotFoundException;
    public Review save(Review review) throws Exception;
    public Review update(Review review) throws Exception;
    public void delete(Long id) throws NotFoundException;
}
