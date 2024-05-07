package it.paa.model.entity.validation.genre;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UniqueValidator.class)
@Documented
public @interface UniqueConstraint {
    String message() default "name must be unique";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}