package com.example.mystore.dto.api.request;

/**
 * RegisterDTO represents the request body for user registration.
 *
 * Fields:
 * - email: The email address of the user.
 * - password: The password chosen by the user.
 * - username: The username chosen by the user.
 */
public class RegisterDTO {
    private String email;
    private String password;
    private String username;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
