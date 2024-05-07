package it.paa.model.entity.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UniqueValidatorGenre.class)
@Documented
public @interface UniqueConstraintGenre {
    String message() default "name must be unique";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}