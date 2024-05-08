package it.paa.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;

import java.time.LocalDate;

public class AuthorDTO {
    @NotBlank(message = "name cannot be empty")
    private String name;

    @NotBlank(message = "surname cannot be empty")
    private String surname;

    @NotBlank(message = "nationality cannot be empty")
    private String nationality;

    @PastOrPresent(message = "birth date must not be in the future")
    private LocalDate birthDate;

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
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }
}
