package it.paa.repository;

import it.paa.model.entity.Role;
import jakarta.persistence.PersistenceException;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.NoContentException;

import java.util.List;

public interface RoleRepository {
    List<Role> getAll() throws NoContentException;
    Role getById(Long id) throws NotFoundException;
    Role save(Role role) throws PersistenceException;
    Role update(Role role) throws PersistenceException;
    void delete(Long id) throws NotFoundException;
}