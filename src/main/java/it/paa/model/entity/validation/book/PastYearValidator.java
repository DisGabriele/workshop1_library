package it.paa.model.entity.validation.book;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.time.LocalDate;

public class PastYearValidator implements ConstraintValidator<PastYearCostraint, Integer> {

    @Override
    public boolean isValid(Integer year, ConstraintValidatorContext constraintValidatorContext) {
        if (year == null) {
            return false;
        }

        int currentYear = LocalDate.now().getYear();
        return year <= currentYear;
    }
}
