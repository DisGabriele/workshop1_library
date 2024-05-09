package it.paa.model.entity.validation.book;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = IsADateValidator.class)
public @interface IsADate {
    String message() default "not a valid date";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}