package it.paa.model.dto;

import it.paa.model.entity.validation.book.IsADate;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

//Utilizzato per la POST e la PUT in modo da non far inserire manualmente id e lista di Book
public class AuthorDTO {
    @NotBlank(message = "name cannot be empty")
    private String name;

    @NotBlank(message = "surname cannot be empty")
    private String surname;

    private String nationality;

    //usato stringa per questo custom validator per permettere di inserire una data nei formati "yyyy-MM-dd" e "dd-MM-yyyy"
    @IsADate
    private String birthDate;

    public AuthorDTO() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public LocalDate getBirthDate() {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            return LocalDate.parse(this.birthDate, formatter);
        } catch (DateTimeParseException e) {
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                return LocalDate.parse(this.birthDate, formatter);
            } catch (DateTimeParseException ex) {
                return null;
            }
        }
    }

}
