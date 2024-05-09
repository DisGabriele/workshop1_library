package it.paa.model.entity.validation.book;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

//Validator che simula quello di Past or Present, però utilizzando un integer

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PastYearValidator.class)
public @interface PastYearConstraint {
    String message() default "date year must not be in the future";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

