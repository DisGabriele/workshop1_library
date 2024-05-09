package it.paa.model.entity.validation.book;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/*
Implementazione del validator delle date
 */
public class IsADateValidator implements ConstraintValidator<IsADate, String> {

    @Override
    public boolean isValid(String date, ConstraintValidatorContext context) {
        if(date == null || date.isEmpty() || date.isBlank())
            return false;

        //vari try catch per provare a passare la stringa contenente la data in vari formati
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate.parse(date, formatter);
            return true;
        } catch (DateTimeParseException e) {
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                LocalDate.parse(date, formatter);
                return true;
            } catch (DateTimeParseException ex) {
                return false;
            }
        }
    }
}
