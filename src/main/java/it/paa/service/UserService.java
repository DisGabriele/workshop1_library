package it.paa.service;

import it.paa.model.entity.User;
import it.paa.repository.UserRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.NoContentException;

import java.util.List;

@ApplicationScoped
public class UserService implements UserRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<User> getAll() throws NoContentException {
        String query = "SELECT u FROM User u";

        List<User> users = entityManager.createQuery(query, User.class).getResultList();

        if(users.isEmpty())
            throw new NotFoundException("no users found");

        return users;
    }

    @Override
    public User getById(Long id) throws NotFoundException {
        User user = entityManager.find(User.class, id);

        if(user == null)
            throw new NotFoundException("user not found");

        return user;
    }

    @Override
    public User add(User user) throws Exception {
        entityManager.persist(user);
        return user;
    }

    @Override
    public User update(User user) throws Exception {
        entityManager.merge(user);
        return user;
    }

    @Override
    public void delete(Long id) throws NotFoundException {
        User user = getById(id);
        entityManager.remove(user);
    }
}
