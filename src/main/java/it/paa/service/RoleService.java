package it.paa.service;

import it.paa.model.entity.Role;
import it.paa.repository.RoleRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.PersistenceException;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.NoContentException;

import java.util.List;

@ApplicationScoped
public class RoleService implements RoleRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Role> getAll() throws NoContentException {
        String query = "SELECT r FROM Role r";
        List<Role> roles;

        roles = entityManager.createQuery(query, Role.class).getResultList();

        if (roles.isEmpty())
            throw new NotFoundException("no roles found");

        return roles;
    }

    @Override
    public Role getById(Long id) throws NotFoundException {
        Role role = entityManager.find(Role.class, id);

        if (role == null)
            throw new NotFoundException("role not found");

        return role;
    }

    public Role getByName(String name) throws NotFoundException {
        String query = "SELECT r FROM Role r WHERE r.name = :name";

        Role role = entityManager.createQuery(query, Role.class)
                .setParameter("name", name)
                .getSingleResult();

        if (role == null)
            throw new NotFoundException("role with name " + name + " not found");

        return role;
    }

    @Override
    public Role save(Role role) throws Exception {
        try {
            entityManager.persist(role);
            entityManager.flush();

            return role;
        } catch (PersistenceException e) {
            throw new Exception("role with this name already exists");
        }
    }

    @Override
    public Role update(Role role) throws Exception {
        try {
            entityManager.merge(role);
            entityManager.flush();

            return role;
        } catch (PersistenceException e) {
            throw new Exception("role with this name already exists");
        }
    }

    @Override
    public void delete(Long id) throws NotFoundException {
        Role role = getById(id);
        entityManager.remove(role);
    }
}
