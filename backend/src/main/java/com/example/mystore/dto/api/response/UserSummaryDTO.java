package com.example.mystore.dto.api.response;

/**
 * UserSummaryDTO represents a summary of the user's information
 * returned after successful registration, login, or session validation.
 *
 * Fields:
 * - userId: The unique identifier of the user.
 * - username: The username of the user.
 * - email: The email address of the user.
 * - sessionNumber: The current session identifier for the user (for authentication purposes).
 */
public class UserSummaryDTO {

    private Long userId;
    private String username;
    private String email;
    private String sessionNumber;


    public UserSummaryDTO(Long userId, String username, String email, String sessionNumber) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.sessionNumber = sessionNumber;
    }

    public String getSessionNumber() {
        return sessionNumber;
    }

    public void setSessionNumber(String sessionNumber) {
        this.sessionNumber = sessionNumber;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
