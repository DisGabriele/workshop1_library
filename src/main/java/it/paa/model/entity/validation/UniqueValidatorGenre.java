package it.paa.model.entity.validation;

import it.paa.model.entity.Genre;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class UniqueValidatorGenre implements ConstraintValidator<UniqueConstraintGenre, Genre> {

    @Inject
    EntityManager entityManager;

    @Override
    public void initialize(UniqueConstraintGenre constraintAnnotation) {
    }

    @Override
    public boolean isValid(Genre genre, ConstraintValidatorContext context) {
        if (genre.getId() != null) {
            Long count = entityManager.createQuery("SELECT COUNT(g) FROM Genre g WHERE g.name = :name AND g.id != :id", Long.class)
                    .setParameter("name", genre.getName())
                    .setParameter("id", genre.getId())
                    .getSingleResult();
            return count == 0;
        }
        Long count = entityManager.createQuery("SELECT COUNT(g) FROM Genre g WHERE g.name = :name", Long.class)
                .setParameter("name", genre.getName())
                .getSingleResult();
        return count == 0;
    }
}
