package it.paa.model.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;

import java.time.LocalDate;

public class AuthorDTO {
    @NotBlank(message = "name cannot be empty")
    private String name;

    @NotBlank(message = "surname cannot be empty")
    private String surname;

    @Past(message = "birth date must be in the past")
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

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }
}
