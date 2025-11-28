package com.example.demo.dto;



public class UsuarioDTO {
    private String username;
    private String email;
    private String password;
    private Integer status;

    // Constructor
    public UsuarioDTO(String username, String email, String password, Integer status) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.status = status;
    }

    // Getters
    public String getUsername() { return username; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public Integer getStatus() { return status; }
}
