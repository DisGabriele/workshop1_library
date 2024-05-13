package it.paa.model.dto;

import jakarta.validation.constraints.NotBlank;

public class RoleDTO {

    @NotBlank(message = "role cannot be empty")
    private String name;

    public RoleDTO() {}

    public RoleDTO(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
