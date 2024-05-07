package it.paa.model.entity.validation.book;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PastYearValidator.class)
public @interface PastYearCostraint {
    String message() default "publishing date must in the past";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

