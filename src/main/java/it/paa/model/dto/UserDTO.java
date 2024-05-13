package it.paa.model.dto;

import jakarta.validation.constraints.NotBlank;

public class UserDTO {
    @NotBlank(message = "username cannot be empty")
    private String username;

    @NotBlank(message = "username cannot be empty")
    private String password;

    @NotBlank(message = "role cannot be empty")
    private String role;

    public UserDTO(){}

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
