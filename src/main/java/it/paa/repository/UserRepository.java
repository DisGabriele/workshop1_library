package it.paa.repository;

import it.paa.model.entity.User;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceException;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.NoContentException;

import java.util.List;

public interface UserRepository {
    List<User> getAll() throws NoContentException;
    User getById(Long id) throws NotFoundException;
    public User getByName(String username) throws NoResultException;
    User save(User user) throws PersistenceException;
    User update(User user) throws PersistenceException;
    void delete(Long id) throws NotFoundException;
}
