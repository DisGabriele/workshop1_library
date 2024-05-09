package it.paa.model.dto;

import jakarta.validation.constraints.NotBlank;

//Utilizzato per la POST e la PUT in modo da non far inserire manualmente id e lista di Book
public class GenreDTO {
    @NotBlank(message = "name cannot be empty")
    private String name;
    private String description;

    public GenreDTO(){}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
