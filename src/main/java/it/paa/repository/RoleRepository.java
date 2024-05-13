package it.paa.repository;

import it.paa.model.entity.Role;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.NoContentException;

import java.util.List;

public interface RoleRepository {
    List<Role> getAll() throws NoContentException;
    Role getById(Long id) throws NotFoundException;
    Role save(Role role) throws Exception;
    Role update(Role role) throws Exception;
    void delete(Long id) throws NotFoundException;
}
